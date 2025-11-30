package com.dongyang.TPOWW.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 현재 시간 기준 baseDate, baseTime
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = String.format("%02d00", now.getHour());

        // API URL 구성
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=3659dfdc17c8d704ea3b676ae54690c6df40cfa660853eb227dbe7bd57d50747");
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=100");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=55");
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=127");

        String apiUrl = urlBuilder.toString();

        String jsonResponse = "";
        HttpSession session = request.getSession();

        try (BufferedReader rd = new BufferedReader(
                new InputStreamReader(
                        ((HttpURLConnection) new URL(apiUrl).openConnection()).getInputStream(), "UTF-8"))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            jsonResponse = sb.toString();

            // 필요한 값만 추출
            session.setAttribute("t1h", extractValue(jsonResponse, "T1H")); // 기온
            session.setAttribute("reh", extractValue(jsonResponse, "REH")); // 습도
            session.setAttribute("rn1", extractValue(jsonResponse, "RN1")); // 강수량
            session.setAttribute("wsd", extractValue(jsonResponse, "WSD")); // 바람

        } catch (Exception e) {
            // 오류 발생 시 기본 메시지
            session.setAttribute("t1h", "오류 발생");
            session.setAttribute("reh", "오류 발생");
            session.setAttribute("rn1", "오류 발생");
            session.setAttribute("wsd", "오류 발생");
        }

        // index.jsp로 redirect
        response.sendRedirect("index.jsp");
    }

    // category가 일치하는 항목의 obsrValue 추출
    private String extractValue(String json, String category) {
        String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category + "\"[^}]*\"obsrValue\"\\s*:\\s*\"([^\"]+)\"[^}]*\\}";
        Matcher m = Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : "정보 없음";
    }
}
