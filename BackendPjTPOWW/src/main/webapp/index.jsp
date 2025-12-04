<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
if (request.getAttribute("t1h") == null || request.getAttribute("reh") == null) {
    response.sendRedirect("weather.do");
    return;
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
