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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // 1. [핵심] 세션에서 날씨 데이터 꺼내기
        String t1h = (String) session.getAttribute("t1h");   // 현재 기온
        String reh = (String) session.getAttribute("reh");   // 습도
        String regionName = (String) session.getAttribute("currentRegion"); // 지역명
        List<ForecastItem> forecastList = (List<ForecastItem>) session.getAttribute("forecastList"); // 예보 리스트

        // 2. 날씨 정보를 하나의 문장으로 만들기
        StringBuilder weatherInfo = new StringBuilder();
        weatherInfo.append("위치: ").append(regionName != null ? regionName : "알 수 없음").append(", ");
        weatherInfo.append("현재 기온: ").append(t1h != null ? t1h + "도" : "정보 없음").append(", ");
        weatherInfo.append("습도: ").append(reh != null ? reh + "%" : "정보 없음").append(". ");

        // (옵션) 단기 예보 정보도 추가 (오늘 최고/최저 기온 등을 분석해서 넣으면 더 좋음)
        if (forecastList != null && !forecastList.isEmpty()) {
            weatherInfo.append(" (예보 데이터 보유)");
        }

        // 3. TpoRequest 객체 생성 및 데이터 채우기
        TpoRequest req = new TpoRequest();
        
        // 사용자 입력값 (tpo.jsp 폼 데이터)
        req.setWhat(request.getParameter("what"));      // 상황 (예: 데이트)
        req.setDate(request.getParameter("whenDate"));  // 날짜
        req.setTime(request.getParameter("whenTime"));  // 시간
        
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

        // 5. 결과 페이지로 이동
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
