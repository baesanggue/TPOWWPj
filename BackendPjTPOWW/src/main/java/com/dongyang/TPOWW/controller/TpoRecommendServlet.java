package com.dongyang.TPOWW.controller;

import java.io.IOException;
import java.util.List;

import com.dongyang.TPOWW.ai.GeminiClient;
import com.dongyang.TPOWW.ai.LlmClient;
import com.dongyang.TPOWW.member.UserDTO;
import com.dongyang.TPOWW.member.UserPrefDAO;
import com.dongyang.TPOWW.member.UserPrefDTO;
import com.dongyang.TPOWW.tpo.TpoRequest;
import com.dongyang.TPOWW.tpo.TpoResult;
import com.dongyang.TPOWW.tpo.TpoService;
import com.dongyang.TPOWW.weather.ForecastItem;
import com.dongyang.TPOWW.weather.KmaWeatherService;
import com.dongyang.TPOWW.weather.WeatherService;
import com.dongyang.TPOWW.tpo.TpoHistoryDAO;
import com.dongyang.TPOWW.tpo.TpoHistoryDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/tpoRecommend.do")
public class TpoRecommendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TpoService tpoService;

    @Override
    public void init() throws ServletException {
        // WeatherService는 이제 직접 호출 안 하므로 null 또는 더미 객체 전달
        // LlmClient 구현체(GeminiClient)를 주입
        tpoService = new TpoService(null, new GeminiClient());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // Fix: Enforce login
        UserDTO sessionUser = (UserDTO) session.getAttribute("udto");
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 1. [핵심] 세션에서 날씨 데이터 꺼내기
        String t1h = (String) session.getAttribute("t1h"); // 현재 기온
        String reh = (String) session.getAttribute("reh"); // 습도
        String regionName = (String) session.getAttribute("currentRegion"); // 지역명
        List<ForecastItem> forecastList = (List<ForecastItem>) session.getAttribute("forecastList"); // 예보 리스트

        // 2. 날씨 정보를 하나의 문장으로 만들기
        StringBuilder weatherInfo = new StringBuilder();
        weatherInfo.append("위치: ").append(regionName != null ? regionName : "알 수 없음").append(", ");

        // 날짜 차이 계산
        String whenDateStr = request.getParameter("whenDate");
        long daysDiff = 0;
        if (whenDateStr != null && !whenDateStr.isEmpty()) {
            try {
                LocalDate today = LocalDate.now();
                LocalDate targetDate = LocalDate.parse(whenDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                daysDiff = ChronoUnit.DAYS.between(today, targetDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (daysDiff > 2) { // 3일 후부터는 중기예보 사용 (단기예보는 모레까지)
            String midLandJson = (String) session.getAttribute("midLandJson");
            String midTaJson = (String) session.getAttribute("midTaJson");

            // 간단하게 JSON이 있다는 것만 알려주고 AI가 알아서 해석하게 하거나,
            // 여기서 파싱해서 해당 날짜의 날씨를 추출해야 함.
            // AI에게 전체 JSON을 넘기기엔 너무 길 수 있으므로, "중기예보 데이터 참조"라고 하고
            // 실제로는 TpoService에서 프롬프트에 녹이는 게 좋음.
            // 하지만 여기서는 편의상 "중기예보: (대략적인 정보)" 형태로 넣거나
            // 그냥 "날씨: 맑음 (예상)" 처럼 퉁칠 수도 있음.
            // 가장 좋은 건 AI에게 "3일 뒤 날씨는 중기예보 데이터를 참고해"라고 하는 것.

            weatherInfo.append(" (중기예보 구간: ").append(daysDiff).append("일 후). ");
            if (midLandJson != null)
                weatherInfo.append("중기육상예보 데이터 보유. ");
            if (midTaJson != null)
                weatherInfo.append("중기기온예보 데이터 보유. ");

            // 실제 프롬프트 구성은 TpoService에서 할 수도 있지만,
            // 여기서는 weatherInfo에 다 때려박는 구조이므로
            // AI가 JSON을 해석할 수 있다고 가정하고 일부를 텍스트로 넣어줄 수도 있음.
            // 일단은 "중기예보 데이터를 참고하여 추천해줘"라는 뉘앙스로 전달.
        } else {
            // 단기예보 (기존 로직)
            weatherInfo.append("현재 기온: ").append(t1h != null ? t1h + "도" : "정보 없음").append(", ");
            weatherInfo.append("습도: ").append(reh != null ? reh + "%" : "정보 없음").append(". ");
            if (forecastList != null && !forecastList.isEmpty()) {
                weatherInfo.append(" (단기예보 데이터 보유)");
            }
        }

        // 3. TpoRequest 객체 생성 및 데이터 채우기
        TpoRequest req = new TpoRequest();

        // 사용자 입력값 (tpo.jsp 폼 데이터)
        req.setWhat(request.getParameter("what")); // 상황 (예: 데이트)
        req.setDate(request.getParameter("whenDate")); // 날짜
        req.setTime(request.getParameter("whenTime")); // 시간

        // 체크박스 처리 (값이 있으면 true)
        req.setIndoor(request.getParameter("indoor") != null);
        req.setOutdoor(request.getParameter("outdoor") != null);

        // 사용자 정보 (세션 및 DB)
        UserDTO udto = (UserDTO) session.getAttribute("udto");
        if (udto != null) {
            req.setGender(udto.getGender());
            req.setAge(udto.getAge());

            // 선호도 정보 조회
            UserPrefDAO pdao = new UserPrefDAO();
            UserPrefDTO pdto = pdao.getUserPref(udto.getUn());
            if (pdto != null) {
                req.setBrand(pdto.getBrand());
                req.setColor(pdto.getColor());
                req.setPcolor(pdto.getPcolor());
            }
        }

        // [중요] 완성된 날씨 정보를 Request 객체에 담기
        req.setWeatherInfo(weatherInfo.toString());

        // 4. AI 서비스 호출
        TpoResult result = tpoService.recommend(req);

        // 5. [추가] 히스토리 저장
        TpoHistoryDTO hDto = new TpoHistoryDTO();
        hDto.setUn(udto.getUn());
        hDto.setRequestDate(req.getDate());
        hDto.setRequestTime(req.getTime());
        hDto.setWhat(req.getWhat());
        hDto.setWeatherSummary(result.getWeatherSummary()); // TpoResult에 날씨 요약이 있다고 가정
        hDto.setAiRecommend(result.getAiRecommend());

        TpoHistoryDAO hDao = new TpoHistoryDAO();
        hDao.insertHistory(hDto);

        // 6. 결과 페이지로 이동
        request.setAttribute("tpoResult", result);
        // 결과 화면(tpo_result.jsp)으로 포워딩 (파일이 없다면 만들어야 함)
        request.getRequestDispatcher("/tpo/tpoResult.jsp").forward(request, response);
    }

    private TpoRequest buildTpoRequest(HttpServletRequest request) {
        TpoRequest r = new TpoRequest();

        r.setGender(request.getParameter("gender"));

        String ageStr = request.getParameter("age");
        try {
            r.setAge(Integer.parseInt(ageStr));
        } catch (NumberFormatException e) {
            r.setAge(0);
        }

        r.setRegion(request.getParameter("region"));
        r.setSigungu(request.getParameter("sigungu"));
        r.setIndoor(request.getParameter("indoor") != null);
        r.setOutdoor(request.getParameter("outdoor") != null);
        r.setDate(request.getParameter("whenDate"));
        r.setTime(request.getParameter("whenTime"));
        r.setWhat(request.getParameter("what"));

        r.setBrand(request.getParameter("brand"));
        r.setColor(request.getParameter("color"));
        r.setPcolor(request.getParameter("pcolor"));

        return r;
    }
}
