<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // 세션에 날씨 정보가 없으면 WeatherServlet로 자동 redirect
    if (session.getAttribute("t1h") == null || session.getAttribute("reh") == null) {
        response.sendRedirect("weather.do");
        return; // redirect 후 JSP 실행 방지
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TPOWW</title>
</head>
<body>
    <%@ include file="header.jsp" %>
    <%@ include file="nav.jsp" %>    
    <%@ include file="main.jsp" %>
    <%@ include file="footer.jsp" %>
</body>
</html>
