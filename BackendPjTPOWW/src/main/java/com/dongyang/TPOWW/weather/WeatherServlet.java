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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

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
        System.out.println("[WeatherServlet] Session ID: " + session.getId());

        UserDTO udto = (UserDTO) session.getAttribute("udto");
        if (udto == null) {
            System.out.println("[WeatherServlet] udto is NULL in session");
        } else {
            System.out.println("[WeatherServlet] udto found. ID: " + udto.getId() + ", Region: " + udto.getRegion());
        }

        // 1. 기본값 설정 (로그인 안 했을 때: 서울 종로구)
        String nx = "60";
        String ny = "127";
        String displayLocation = "서울특별시 (기본)";
        String regionCodeForMid = "Seoul"; // 중기예보용 지역 코드 키

        // 2. 로그인 상태 체크 및 좌표 변경
        if (udto != null) {
            String region = udto.getRegion(); // 예: "Seoul"
            regionCodeForMid = region; // 중기예보용 키 업데이트
            String sigungu = udto.getSigungu(); // 예: "강남구"

            // LocationCoord 클래스를 이용해 좌표 찾기
            Point pt = LocationCoord.getCoordinate(region, sigungu);
            nx = pt.x;
            ny = pt.y;

            // 화면에 보여줄 문구 ("서울특별시 강남구")
            displayLocation = getKoreanRegionName(region) + " " + sigungu;
        }

        // 3. 세션에 정보 저장
        session.setAttribute("nx", nx);
        session.setAttribute("ny", ny);
        session.setAttribute("currentRegion", displayLocation);

        // 4. API 호출 준비
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 초단기실황(Live)용 시간 계산: 매시 40분경 발표 -> 40분 이전이면 1시간 전 데이터 사용
        LocalDateTime liveTime = now;
        if (now.getMinute() < 40) {
            liveTime = now.minusHours(1);
        }
        String liveBaseDate = liveTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String liveBaseTime = String.format("%02d00", liveTime.getHour());

        // 단기예보(Forecast)용 시간 계산: 02, 05, 08, 11, 14, 17, 20, 23시 발표
        // 현재 시간보다 이전의 가장 가까운 발표 시각 찾기
        String[] baseTimes = { "0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300" };
        String fcstBaseTime = "0200";
        String fcstBaseDate = baseDate;

        // 현재 시각 HHmm
        int currentHHmm = Integer.parseInt(now.format(DateTimeFormatter.ofPattern("HHmm")));

        // 가장 가까운 이전 발표 시각 찾기
        boolean found = false;
        for (int i = baseTimes.length - 1; i >= 0; i--) {
            int bt = Integer.parseInt(baseTimes[i]);
            // API 제공 시간 고려 (발표 후 10분 뒤부터 안정적이라 가정)
            if (currentHHmm > bt + 10) {
                fcstBaseTime = baseTimes[i];
                found = true;
                break;
            }
        }

        if (!found) {
            // 02:10 이전인 경우 -> 어제 23:00 데이터 사용
            fcstBaseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            fcstBaseTime = "2300";
        }

        // ---------------- 현재 날씨 (초단기실황) ----------------
        try {
            String ultraSrtNcstUrl = buildUrl("getUltraSrtNcst", liveBaseDate, liveBaseTime, nx, ny);
            String jsonResponse = readUrl(ultraSrtNcstUrl);
            String t1h = extractValue(jsonResponse, "T1H");
            if ("-".equals(t1h)) {
                throw new Exception("Weather API returned empty data");
            }
            session.setAttribute("t1h", t1h);
            session.setAttribute("reh", extractValue(jsonResponse, "REH"));
            session.setAttribute("rn1", extractValue(jsonResponse, "RN1"));
            session.setAttribute("wsd", extractValue(jsonResponse, "WSD"));
            session.setAttribute("PTY", extractValue(jsonResponse, "PTY"));

            // SKY는 초단기예보에서 가져오기
            String fcstUrl = buildUrl("getUltraSrtFcst", liveBaseDate, liveBaseTime, nx, ny);
            String fcstJson = readUrl(fcstUrl);
            String skyValue = extractFcstValue(fcstJson, "SKY");
            session.setAttribute("SKY", skyValue);

            System.out.println("=== 날씨 디버그 ===");
            System.out.println("PTY: " + extractValue(jsonResponse, "PTY"));
            System.out.println("SKY: " + skyValue);
            System.out.println("==================");

            session.removeAttribute("weatherError");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("t1h", "-");
            session.setAttribute("PTY", "0");
            session.setAttribute("SKY", "-");
            session.setAttribute("weatherError", "기상청 정보를 불러올 수 없습니다.");
        }

        // --------------- 단기예보 (최근 발표 기준) ---------------
        try {
            String fcstUrl = buildUrl("getVilageFcst", fcstBaseDate, fcstBaseTime, nx, ny);
            String fcstJson = readUrl(fcstUrl);

            // 디버그: API 응답 일부 출력
            System.out.println("=== 단기예보 API 응답 (처음 500자) ===");
            System.out.println(fcstJson.substring(0, Math.min(500, fcstJson.length())));
            System.out.println("========================================");

            // 현재 시각의 POP 찾기
            String currentDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String currentTime = String.format("%02d00", now.getHour());

            String popValue = extractFcstValueByTime(fcstJson, "POP", currentDate, currentTime);
            session.setAttribute("POP", popValue);
            System.out.println("POP(강수확률): " + popValue + "%");

            List<ForecastItem> forecastList = parseForecast(fcstJson);
            session.setAttribute("forecastList", forecastList);
            // 3일치 요약 정보 생성
            if (forecastList != null && !forecastList.isEmpty()) {
                List<DailyWeatherDTO> threeDayForecast = processDailyWeather(forecastList);
                session.setAttribute("threeDayForecast", threeDayForecast);
            } else {
                session.setAttribute("threeDayForecast", new ArrayList<DailyWeatherDTO>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("forecastList", new ArrayList<ForecastItem>());
            session.setAttribute("threeDayForecast", new ArrayList<DailyWeatherDTO>());
        }

        // --------------- 중기예보 (Mid-term) ---------------
        try {
            // 1. 발표시각 (tmFc) 계산: 0600 or 1800
            String tmFc = getTmFc(now);

            // 2. 지역 코드 조회
            String landCode = MidTermLocation.getLandCode(regionCodeForMid);
            String tempCode = MidTermLocation.getTempCode(regionCodeForMid);

            // 3. API 호출
            String midLandJson = readUrl(buildMidUrl("getMidLandFcst", tmFc, landCode));
            String midTaJson = readUrl(buildMidUrl("getMidTa", tmFc, tempCode));

            // 4. 세션 저장
            session.setAttribute("midLandJson", midLandJson);
            session.setAttribute("midTaJson", midTaJson);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("midLandJson", "{}");
            session.setAttribute("midTaJson", "{}");
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
    private String buildUrl(String apiType, String baseDate, String baseTime, String nx, String ny)
            throws java.io.UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(
                "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" + apiType);
        // 서비스키는 본인 키로 꼭 확인하세요!
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")
                + "=3659dfdc17c8d704ea3b676ae54690c6df40cfa660853eb227dbe7bd57d50747");
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=1000");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");

        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));
        return urlBuilder.toString();
    }

    // ---------------- URL 읽기 ----------------
    private String readUrl(String apiUrl) throws Exception {
        // [변경] URL 생성자 대신 URI.create().toURL() 사용 (Deprecated 해결)
        URL url = java.net.URI.create(apiUrl).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

    // ---------------- 시간별 예보 값 추출 ----------------
    private String extractFcstValueByTime(String json, String category, String fcstDate, String fcstTime) {
        // 특정 날짜/시간의 값을 찾음
        String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category + "\""
                + "[^}]*\"fcstDate\"\\s*:\\s*\"" + fcstDate + "\""
                + "[^}]*\"fcstTime\"\\s*:\\s*\"" + fcstTime + "\""
                + "[^}]*\"fcstValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
        Matcher m = Pattern.compile(pattern).matcher(json);
        if (m.find()) {
            return m.group(1);
        }

        // 못 찾으면 첫 번째 POP 값이라도 반환
        String simplePattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category + "\""
                + "[^}]*\"fcstValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
        Matcher m2 = Pattern.compile(simplePattern).matcher(json);
        return m2.find() ? m2.group(1) : "-";
    }

    // ---------------- 값 추출 ----------------
    private String extractValue(String json, String category) {
        String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category
                + "\"[^}]*\"obsrValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
        Matcher m = Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : "-";
    }

    // ---------------- 예보 값 추출 (fcstValue 사용) ----------------
    private String extractFcstValue(String json, String category) {
        String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category
                + "\"[^}]*\"fcstValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
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
                    "\\{.*?\"category\"\\s*:\\s*\"([^\"]+)\".*?"
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
        for (ForecastItem i : a)
            map.put(i.getFcstDate() + "_" + i.getFcstTime() + "_" + i.getCategory(), i);
        for (ForecastItem i : b)
            map.putIfAbsent(i.getFcstDate() + "_" + i.getFcstTime() + "_" + i.getCategory(), i);
        return new ArrayList<>(map.values());
    }

    // ---------------- [추가] 지역명 한글 변환 ----------------
    private String getKoreanRegionName(String regionCode) {
        if (regionCode == null)
            return "";
        switch (regionCode) {
            case "Seoul":
                return "서울특별시";
            case "Busan":
                return "부산광역시";
            case "Daegu":
                return "대구광역시";
            case "Incheon":
                return "인천광역시";
            case "Gwangju":
                return "광주광역시";
            case "Daejeon":
                return "대전광역시";
            case "Ulsan":
                return "울산광역시";
            case "Sejong":
                return "세종특별자치시";
            case "Gyeonggi":
                return "경기도";
            case "Gangwon":
                return "강원특별자치도";
            case "Chungbuk":
                return "충청북도";
            case "Chungnam":
                return "충청남도";
            case "Jeonbuk":
                return "전북특별자치도";
            case "Jeonnam":
                return "전라남도";
            case "Gyeongbuk":
                return "경상북도";
            case "Gyeongnam":
                return "경상남도";
            case "Jeju":
                return "제주특별자치도";
            default:
                return regionCode;
        }
    }

    // ---------------- [추가] 중기예보 URL 생성 ----------------
    private String buildMidUrl(String apiType, String tmFc, String regId) throws java.io.UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/" + apiType);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8")
                + "=3659dfdc17c8d704ea3b676ae54690c6df40cfa660853eb227dbe7bd57d50747");
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=10");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
        urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(tmFc, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
        return urlBuilder.toString();
    }

    // ---------------- [추가] 발표시각 계산 (0600, 1800) ----------------
    private String getTmFc(LocalDateTime now) {
        int hour = now.getHour();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (hour < 6) {
            // 어제 18:00
            return now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";
        } else if (hour < 18) {
            // 오늘 06:00
            return dateStr + "0600";
        } else {
            // 오늘 18:00
            return dateStr + "1800";
        }
    }

    // ---------------- [추가] 3일치 날씨 요약 가공 ----------------
    private List<DailyWeatherDTO> processDailyWeather(List<ForecastItem> forecastList) {
        Map<String, List<ForecastItem>> dailyMap = new LinkedHashMap<>();

        // 날짜별로 그룹화
        for (ForecastItem item : forecastList) {
            dailyMap.computeIfAbsent(item.getFcstDate(), k -> new ArrayList<>()).add(item);
        }

        List<DailyWeatherDTO> result = new ArrayList<>();
        int count = 0;

        for (String date : dailyMap.keySet()) {
            if (count >= 3)
                break; // 최대 3일치만

            List<ForecastItem> items = dailyMap.get(date);

            // 최저/최고 기온 찾기 (TMP 기준)
            double min = 100;
            double max = -100;
            boolean foundTemp = false;

            // 날씨 상태 판별 (PTY 우선, 그 다음 SKY)
            // PTY: 0(없음), 1(비), 2(비/눈), 3(눈), 4(소나기)
            // SKY: 1(맑음), 3(구름많음), 4(흐림)
            int maxPty = 0;
            int maxSky = 0;

            for (ForecastItem item : items) {
                if ("TMP".equals(item.getCategory())) {
                    double val = Double.parseDouble(item.getFcstValue());
                    if (val < min)
                        min = val;
                    if (val > max)
                        max = val;
                    foundTemp = true;
                }
                if ("PTY".equals(item.getCategory())) {
                    int pty = Integer.parseInt(item.getFcstValue());
                    if (pty > maxPty)
                        maxPty = pty;
                }
                if ("SKY".equals(item.getCategory())) {
                    int sky = Integer.parseInt(item.getFcstValue());
                    if (sky > maxSky)
                        maxSky = sky;
                }
            }

            if (!foundTemp)
                continue; // 기온 정보 없으면 스킵

            // 요일 구하기
            LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
            String dayOfWeek = ld.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

            // 상태 결정
            String state = "sunny";
            String desc = "맑음";

            if (maxPty > 0) {
                if (maxPty == 1 || maxPty == 4) {
                    state = "rainy";
                    desc = "비";
                } else if (maxPty == 2 || maxPty == 3) {
                    state = "snowy";
                    desc = "눈/비";
                }
            } else {
                if (maxSky == 3) {
                    state = "cloudy";
                    desc = "구름많음";
                } else if (maxSky == 4) {
                    state = "cloudy";
                    desc = "흐림";
                } else {
                    // 기본값 (maxSky == 1 또는 알 수 없는 경우)
                    state = "sunny";
                    desc = "맑음";
                }
            }

            // 디버그: 최종 state 값 확인
            System.out.println("=== processDailyWeather 디버그 ===");
            System.out.println("날짜: " + date + " (" + dayOfWeek + ")");
            System.out.println("maxPty: " + maxPty + ", maxSky: " + maxSky);
            System.out.println("최종 state: " + state + ", desc: " + desc);
            System.out.println("===================================");

            result.add(new DailyWeatherDTO(
                    date,
                    dayOfWeek,
                    String.format("%.0f", min),
                    String.format("%.0f", max),
                    state,
                    desc));
            count++;
        }
        return result;
    }
}