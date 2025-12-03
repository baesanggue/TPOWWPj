<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.LinkedHashMap, java.time.LocalDate, java.time.format.DateTimeFormatter, java.time.DayOfWeek" %>
<%@ page import="com.dongyang.TPOWW.weather.ForecastItem" %>

<h2>📌 현재 날씨</h2>
<%
    String t1h = (String) session.getAttribute("t1h");
    String reh = (String) session.getAttribute("reh");
    String rn1 = (String) session.getAttribute("rn1");
    String wsd = (String) session.getAttribute("wsd");

    // 오늘 날짜와 요일
    LocalDate today = LocalDate.now();
    DayOfWeek todayWeek = today.getDayOfWeek();
    String[] korWeek = {"월","화","수","목","금","토","일"};
%>

<p>기온(T1H): <%= t1h %> ℃</p>
<p>습도(REH): <%= reh %> %</p>
<p>강수량(RN1): <%= rn1 %> mm</p>
<p>바람(WSD): <%= wsd %> m/s</p>
<p>오늘 요일: <%= korWeek[todayWeek.getValue()-1] %>요일</p>

<hr>

<h2>📌 단기예보 (오전/오후 TMP)</h2>
<%
    List<ForecastItem> forecastList =
        (List<ForecastItem>) session.getAttribute("forecastList");

    if (forecastList != null && !forecastList.isEmpty()) {

        LinkedHashMap<String, String[]> dailyTMP = new LinkedHashMap<>();

        for (ForecastItem item : forecastList) {
            String date = item.getFcstDate();
            String time = item.getFcstTime();
            String category = item.getCategory();
            String value = item.getFcstValue();

            if (!category.equals("TMP")) continue;

            if (!dailyTMP.containsKey(date)) {
                dailyTMP.put(date, new String[2]);
            }

            int hour = Integer.parseInt(time.substring(0, 2));

            if (hour >= 0 && hour < 12 && dailyTMP.get(date)[0] == null) {
                dailyTMP.get(date)[0] = value;
            }

            if (hour >= 12 && hour <= 23 && dailyTMP.get(date)[1] == null) {
                dailyTMP.get(date)[1] = value;
            }
        }

        int printed = 0;
        for (String date : dailyTMP.keySet()) {
            if (printed >= 6) break;
            printed++;

            String[] temps = dailyTMP.get(date);

            // 요일 계산
            LocalDate fcstDay = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
            DayOfWeek dayOfWeek = fcstDay.getDayOfWeek();
            String dayKor = korWeek[dayOfWeek.getValue()-1];
%>
<p><strong><%= date %> (<%= dayKor %>요일)</strong>
    오전 TMP: <%= temps[0] != null ? temps[0] : "-" %> ℃,
    오후 TMP: <%= temps[1] != null ? temps[1] : "-" %> ℃
</p>
<%
        }
    } else {
%>
<p>단기예보 정보 없음</p>
<%
    }
%>