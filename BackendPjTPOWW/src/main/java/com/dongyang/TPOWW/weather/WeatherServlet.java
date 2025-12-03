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

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // ---------------- 현재 날씨 (초단기실황) ----------------
        String ultraSrtNcstUrl = buildUrl("getUltraSrtNcst", baseDate, String.format("%02d00", now.getHour()));
        try {
            String jsonResponse = readUrl(ultraSrtNcstUrl);
            session.setAttribute("t1h", extractValue(jsonResponse, "T1H"));
            session.setAttribute("reh", extractValue(jsonResponse, "REH"));
            session.setAttribute("rn1", extractValue(jsonResponse, "RN1"));
            session.setAttribute("wsd", extractValue(jsonResponse, "WSD"));
        } catch (Exception e) {
            session.setAttribute("t1h", "오류 발생");
            session.setAttribute("reh", "오류 발생");
            session.setAttribute("rn1", "오류 발생");
            session.setAttribute("wsd", "오류 발생");
        }

        // --------------- 단기예보 (0500 + 1700 모두 불러오기) ---------------
        try {
            List<ForecastItem> forecast0500 = getForecast(baseDate, "0500");
            List<ForecastItem> forecast1700 = getForecast(baseDate, "1700");

            List<ForecastItem> merged = mergeForecast(forecast0500, forecast1700);

            session.setAttribute("forecastList", merged);
        } catch (Exception e) {
            session.setAttribute("forecastList", new ArrayList<ForecastItem>());
        }

        response.sendRedirect("index.jsp");
    }

    // ---------------- 단기예보 요청 ----------------
    private List<ForecastItem> getForecast(String baseDate, String baseTime) throws Exception {
        String url = buildUrl("getVilageFcst", baseDate, baseTime);
        String json = readUrl(url);
        return parseForecast(json);
    }

    // ---------------- URL 생성 ----------------
    private String buildUrl(String apiType, String baseDate, String baseTime) throws java.io.UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" + apiType);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=3659dfdc17c8d704ea3b676ae54690c6df40cfa660853eb227dbe7bd57d50747");
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=1000");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=55");
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=127");
        return urlBuilder.toString();
    }

    // ---------------- URL 읽기 ----------------
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

    // ---------------- 현재 날씨 추출 ----------------
    private String extractValue(String json, String category) {
        String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category + "\"[^}]*\"obsrValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
        Matcher m = Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : "정보 없음";
    }

    // ---------------- 단기예보 파싱 (TMP/REH 모두 가져오기) ----------------
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
                list.add(new ForecastItem(
                        m.group(2), // fcstDate
                        m.group(3), // fcstTime
                        m.group(1), // category
                        m.group(4)  // fcstValue
                ));
            }
        }

        return list;
    }

    // ---------------- 두 발표시각 데이터 병합 ----------------
    private List<ForecastItem> mergeForecast(List<ForecastItem> a, List<ForecastItem> b) {

        LinkedHashMap<String, ForecastItem> map = new LinkedHashMap<>();

        for (ForecastItem i : a) {
            String key = i.getFcstDate() + "_" + i.getFcstTime() + "_" + i.getCategory();
            map.put(key, i);
        }

        for (ForecastItem i : b) {
            String key = i.getFcstDate() + "_" + i.getFcstTime() + "_" + i.getCategory();
            map.putIfAbsent(key, i); // 0500에 없는 TMP만 추가됨
        }

        return new ArrayList<>(map.values());
    }
}