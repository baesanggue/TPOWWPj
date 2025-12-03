package com.dongyang.TPOWW.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.dongyang.TPOWW.member.UserDTO;
import com.dongyang.TPOWW.weather.LocationCoord.Point;

@WebServlet("/weather.do")
public class WeatherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        UserDTO udto = (UserDTO) session.getAttribute("udto");

        // 1. 기본값 설정 (로그인 안 했을 때: 서울 종로구)
        String nx = "60";
        String ny = "127";
        String displayLocation = "서울특별시 (기본)";

        // 2. 로그인 상태 체크 및 좌표 변경
        if (udto != null) {
            String region = udto.getRegion();   // 예: "Seoul"
            String sigungu = udto.getSigungu(); // 예: "강남구"
            
            // LocationCoord 클래스를 이용해 좌표 찾기
            Point pt = LocationCoord.getCoordinate(region, sigungu);
            nx = pt.x;
            ny = pt.y;
            
            // 화면에 보여줄 문구 ("서울특별시 강남구")
            displayLocation = getKoreanRegionName(region) + " " + sigungu;
        }

        // 3. 세션에 정보 저장 (main.jsp 및 AI 추천에서 사용)
        session.setAttribute("nx", nx);
        session.setAttribute("ny", ny);
        session.setAttribute("currentRegion", displayLocation);

        // 4. API 호출 준비
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // ---------------- 현재 날씨 (초단기실황) ----------------
        // buildUrl에 nx, ny를 전달합니다.
        String ultraSrtNcstUrl = buildUrl("getUltraSrtNcst", baseDate, String.format("%02d00", now.getHour()), nx, ny);
        try {
            String jsonResponse = readUrl(ultraSrtNcstUrl);
            session.setAttribute("t1h", extractValue(jsonResponse, "T1H"));
            session.setAttribute("reh", extractValue(jsonResponse, "REH"));
            session.setAttribute("rn1", extractValue(jsonResponse, "RN1"));
            session.setAttribute("wsd", extractValue(jsonResponse, "WSD"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("t1h", "-"); // 오류 시 대시(-) 표시
        }

        // --------------- 단기예보 (0500 + 1700) ---------------
        try {
            List<ForecastItem> forecast0500 = getForecast(baseDate, "0500", nx, ny);
            List<ForecastItem> forecast1700 = getForecast(baseDate, "1700", nx, ny);

            List<ForecastItem> merged = mergeForecast(forecast0500, forecast1700);
            session.setAttribute("forecastList", merged);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("forecastList", new ArrayList<ForecastItem>());
        }

        response.sendRedirect("index.jsp");
    }

    // ---------------- 단기예보 요청 (nx, ny 파라미터 추가) ----------------
    private List<ForecastItem> getForecast(String baseDate, String baseTime, String nx, String ny) throws Exception {
        String url = buildUrl("getVilageFcst", baseDate, baseTime, nx, ny);
        String json = readUrl(url);
        return parseForecast(json);
    }

    // ---------------- URL 생성 (nx, ny 적용) ----------------
    private String buildUrl(String apiType, String baseDate, String baseTime, String nx, String ny) throws java.io.UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" + apiType);
        // 서비스키는 본인 키로 꼭 확인하세요!
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=3659dfdc17c8d704ea3b676ae54690c6df40cfa660853eb227dbe7bd57d50747");
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=1000");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        
        // [중요] 고정값 55, 127을 지우고 변수 nx, ny를 넣습니다.
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        
        return urlBuilder.toString();
    }

    // ---------------- URL 읽기 (기존 동일) ----------------
    private String readUrl(String apiUrl) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
    }

    // ---------------- 값 추출 (기존 동일) ----------------
    private String extractValue(String json, String category) {
        String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category + "\"[^}]*\"obsrValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
        Matcher m = Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : "-";
    }

    // ---------------- 파싱 (기존 동일) ----------------
    private List<ForecastItem> parseForecast(String json) {
        List<ForecastItem> list = new ArrayList<>();
        Pattern itemArrayPattern = Pattern.compile("\"item\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
        Matcher itemArrayMatcher = itemArrayPattern.matcher(json);
        if (itemArrayMatcher.find()) {
            String itemsContent = itemArrayMatcher.group(1);
            Pattern itemPattern = Pattern.compile(
                "\\{.*?\"category\"\\s*:\\s*\"(TMP|REH)\".*?"
                        + "\"fcstDate\"\\s*:\\s*\"(\\d{8})\".*?"
                        + "\"fcstTime\"\\s*:\\s*\"(\\d{4})\".*?"
                        + "\"fcstValue\"\\s*:\\s*\"([^\"]+)\".*?}",
                Pattern.DOTALL);
            Matcher m = itemPattern.matcher(itemsContent);
            while (m.find()) {
                list.add(new ForecastItem(m.group(2), m.group(3), m.group(1), m.group(4)));
            }
        }
        return list;
    }

    // ---------------- 병합 (기존 동일) ----------------
    private List<ForecastItem> mergeForecast(List<ForecastItem> a, List<ForecastItem> b) {
        LinkedHashMap<String, ForecastItem> map = new LinkedHashMap<>();
        for (ForecastItem i : a) map.put(i.getFcstDate() + "_" + i.getFcstTime() + "_" + i.getCategory(), i);
        for (ForecastItem i : b) map.putIfAbsent(i.getFcstDate() + "_" + i.getFcstTime() + "_" + i.getCategory(), i);
        return new ArrayList<>(map.values());
    }

    // ---------------- [추가] 지역명 한글 변환 ----------------
    private String getKoreanRegionName(String regionCode) {
        if (regionCode == null) return "";
        switch (regionCode) {
            case "Seoul": return "서울특별시";
            case "Busan": return "부산광역시";
            case "Daegu": return "대구광역시";
            case "Incheon": return "인천광역시";
            case "Gwangju": return "광주광역시";
            case "Daejeon": return "대전광역시";
            case "Ulsan": return "울산광역시";
            case "Sejong": return "세종특별자치시";
            case "Gyeonggi": return "경기도";
            case "Gangwon": return "강원특별자치도";
            case "Chungbuk": return "충청북도";
            case "Chungnam": return "충청남도";
            case "Jeonbuk": return "전북특별자치도";
            case "Jeonnam": return "전라남도";
            case "Gyeongbuk": return "경상북도";
            case "Gyeongnam": return "경상남도";
            case "Jeju": return "제주특별자치도";
            default: return regionCode;
        }
    }
}