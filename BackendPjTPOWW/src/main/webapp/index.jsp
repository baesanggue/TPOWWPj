<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <% // Check if weather data exists in session. If not, redirect to WeatherServlet.
            if(session.getAttribute("t1h")==null || session.getAttribute("reh")==null) {
            response.sendRedirect("weather.do"); return; } %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>TPO Weather Wear</title>
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
            </head>

            <body>
                <%@ include file="header.jsp" %>

                    <!-- Main Content -->
                    <%@ include file="main.jsp" %>

                        <%@ include file="footer.jsp" %>
            </body>

            </html>