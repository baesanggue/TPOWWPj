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

                // 디버그: 날짜 계산 확인
                System.out.println("=== 날짜 계산 디버그 ===");
                System.out.println("오늘: " + today);
                System.out.println("선택 날짜: " + targetDate);
                System.out.println("일수 차이: " + daysDiff);
                System.out.println("daysDiff > 2: " + (daysDiff > 2));
                System.out.println("======================");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (daysDiff > 2) { // 3일 후부터는 중기예보 사용
            String midLandJson = (String) session.getAttribute("midLandJson");
            String midTaJson = (String) session.getAttribute("midTaJson");
            weatherInfo.append(daysDiff).append("일 후 예상 날씨: ");
            // 디버그: JSON 데이터 확인
            System.out.println("=== 중기예보 디버그 ===");
            System.out.println("midTaJson: "
                    + (midTaJson != null ? midTaJson.substring(0, Math.min(200, midTaJson.length())) : "null"));
            System.out.println("midLandJson: "
                    + (midLandJson != null ? midLandJson.substring(0, Math.min(200, midLandJson.length())) : "null"));
            System.out.println("targetDay: " + daysDiff);
            System.out.println("======================");
            boolean foundData = false;

            // 중기 기온 예보 파싱
            if (midTaJson != null && !midTaJson.isEmpty()) {
                int targetDay = (int) daysDiff;
                String tempKey = "taMin" + targetDay;
                String tempMaxKey = "taMax" + targetDay;
                String minTemp = extractJsonValue(midTaJson, tempKey);
                String maxTemp = extractJsonValue(midTaJson, tempMaxKey);
                if (minTemp != null && maxTemp != null) {
                    weatherInfo.append("최저 ").append(minTemp).append("°C, ");
                    weatherInfo.append("최고 ").append(maxTemp).append("°C");
                    foundData = true;
                }
            }

            // 중기 육상 예보 파싱
            if (midLandJson != null && !midLandJson.isEmpty()) {
                int targetDay = (int) daysDiff;
                String wfKey = "wf" + targetDay + "Am";
                String wf = extractJsonValue(midLandJson, wfKey);
                if (wf != null && !wf.isEmpty()) {
                    if (foundData) {
                        weatherInfo.append(". ");
                    }
                    weatherInfo.append("날씨: ").append(wf);
                    foundData = true;
                }
            }

            if (!foundData) {
                weatherInfo.append("중기예보 분석 중");
            }
        } else {
            // 3일 이내: 단기예보 사용
            if (whenDateStr != null && forecastList != null && !forecastList.isEmpty()) {
                try {
                    LocalDate targetDate = LocalDate.parse(whenDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String targetDateStr = targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                    String minTemp = null, maxTemp = null;
                    for (ForecastItem item : forecastList) {
                        if (item.getFcstDate().equals(targetDateStr) && "TMP".equals(item.getCategory())) {
                            int temp = Integer.parseInt(item.getFcstValue());
                            if (minTemp == null || temp < Integer.parseInt(minTemp)) {
                                minTemp = item.getFcstValue();
                            }
                            if (maxTemp == null || temp > Integer.parseInt(maxTemp)) {
                                maxTemp = item.getFcstValue();
                            }
                        }
                    }

                    weatherInfo.append(daysDiff).append("일 후: 최저 ")
                            .append(minTemp != null ? minTemp : "-")
                            .append("°C, 최고 ")
                            .append(maxTemp != null ? maxTemp : "-")
                            .append("°C");
                } catch (Exception e) {
                    weatherInfo.append("날씨 정보 없음");
                }
            } else {
                weatherInfo.append("날씨 정보 없음");
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
        // 날짜/시간/활동/장소 정보 설정
        result.setDate(whenDateStr);
        result.setTime(request.getParameter("whenTime"));
        result.setActivity(request.getParameter("what"));
        result.setLocation(regionName);
        // 5. [추가] 히스토리 저장
        TpoHistoryDTO hDto = new TpoHistoryDTO();
        hDto.setUn(udto.getUn());
        hDto.setRequestDate(req.getDate());
        hDto.setRequestTime(req.getTime());
        hDto.setWhat(req.getWhat());
        hDto.setWeatherSummary(result.getWeatherSummary()); // TpoResult에 날씨 요약이 있다고 가정
        hDto.setAiRecommend(result.getAiRecommend());
        hDto.setReasonSummary(result.getReasonSummary());

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

    /**
     * JSON 문자열에서 특정 키의 값을 추출하는 헬퍼 메서드
     * 간단한 파싱으로 "키":"값" 형태를 찾아서 반환
     * 
     * @param jsonString JSON 문자열
     * @param key        찾을 키
     * @return 키에 해당하는 값 (없으면 null)
     */
    private String extractJsonValue(String jsonString, String key) {
        if (jsonString == null || key == null) {
            return null;
        }
        // "key": 패턴 찾기 (공백 고려)
        String searchPattern = "\"" + key + "\"";
        int keyIndex = jsonString.indexOf(searchPattern);

        if (keyIndex == -1) {
            return null;
        }

        // 콜론 위치 찾기
        int colonIndex = jsonString.indexOf(":", keyIndex);
        if (colonIndex == -1) {
            return null;
        }

        // 콜론 다음 공백 건너뛰기
        int valueStart = colonIndex + 1;
        while (valueStart < jsonString.length() &&
                (jsonString.charAt(valueStart) == ' ' ||
                        jsonString.charAt(valueStart) == '\t')) {
            valueStart++;
        }

        if (valueStart >= jsonString.length()) {
            return null;
        }

        // 값이 문자열인 경우 (따옴표로 시작)
        if (jsonString.charAt(valueStart) == '"') {
            valueStart++; // 시작 따옴표 건너뛰기
            int valueEnd = jsonString.indexOf('"', valueStart);
            if (valueEnd == -1) {
                return null;
            }
            return jsonString.substring(valueStart, valueEnd);
        }
        // 값이 숫자인 경우
        else {
            int valueEnd = valueStart;
            while (valueEnd < jsonString.length()) {
                char c = jsonString.charAt(valueEnd);
                if (!Character.isDigit(c) && c != '.' && c != '-') {
                    break;
                }
                valueEnd++;
            }
            return jsonString.substring(valueStart, valueEnd).trim();
        }
    }
}
