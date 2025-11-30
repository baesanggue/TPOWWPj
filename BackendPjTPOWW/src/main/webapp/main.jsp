<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>main</title>
</head>
<body>
	<hr>
	<h1>MAIN</h1>
	<h2>📌 기상청 현재 날씨 정보</h2>

<%
    String t1h = (String) session.getAttribute("t1h"); // 기온
    String reh = (String) session.getAttribute("reh"); // 습도
    String rn1 = (String) session.getAttribute("rn1"); // 1시간 강수량
    String wsd = (String) session.getAttribute("wsd"); // 풍속
%>

	<p>기온(T1H): <%= t1h %> ℃</p>
	<p>습도(REH): <%= reh %> %</p>
	<p>강수량(RN1): <%= rn1 %> mm</p>
	<p>바람(WSD): <%= wsd %> m/s</p>
	
	<form action="tpo.do" method="get" style="display:inline;">
    	<button type="submit">TPO 코디 추천</button>
	</form>
</body>
</html>
