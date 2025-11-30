<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<h2>📌 기상청 현재 날씨 정보</h2>

<%
    String t1h = (String) session.getAttribute("t1h");
    String reh = (String) session.getAttribute("reh");
%>

<p>기온(T1H): <%= t1h %> ℃</p>
<p>습도(REH): <%= reh %> %</p>
