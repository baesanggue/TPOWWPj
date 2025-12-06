<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <div class="container">
            <div class="current-weather-display">
                <h2 class="border-none mb-10">
                    ${not empty currentRegion ? currentRegion : '서울특별시 (기본)'}
                </h2>

                <c:choose>
                    <c:when test="${not empty weatherError or t1h eq '-'}">
                        <div class="weather-error-box">
                            <p class="emoji-large">⚠️</p>
                            <p>날씨 정보를 불러올 수 없습니다.</p>
                            <p class="font-small">(기상청 API 응답 없음)</p>
                            <button type="button" class="btn-secondary small mt-10"
                                onclick="location.href='weather.do'">다시 시도</button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <% String pty=(String) session.getAttribute("PTY"); String sky=(String)
                            session.getAttribute("SKY"); String weatherIcon="☀️" ; if (pty !=null && !pty.equals("0")) {
                            if (pty.equals("1")) weatherIcon="🌧️" ; else if (pty.equals("2")) weatherIcon="🌧️❄️" ;
                            else if (pty.equals("3")) weatherIcon="❄️" ; else if (pty.equals("4") || pty.equals("5"))
                            weatherIcon="🌧️" ; else if (pty.equals("6") || pty.equals("7")) weatherIcon="❄️🌧️" ; }
                            else if (sky !=null) { String hour=new java.text.SimpleDateFormat("HH").format(new
                            java.util.Date()); int currentHour=Integer.parseInt(hour); boolean isNight=(currentHour>= 18
                            || currentHour < 6); if (sky.equals("1")) weatherIcon=isNight ? "🌙" : "☀️" ; else if
                                (sky.equals("3")) weatherIcon=isNight ? "☁️🌙" : "⛅" ; else if (sky.equals("4"))
                                weatherIcon="☁️" ; } %>
                                <div class="big-icon">
                                    <%= weatherIcon %>
                                </div>

                                <div class="big-temp">
                                    ${not empty t1h ? t1h : '-'}°
                                </div>

                                <div class="weather-desc">
                                    <c:choose>
                                        <c:when test="${PTY eq '1' or PTY eq '4' or PTY eq '5'}">
                                            비<c:if test="${not empty POP and POP ne '-'}"> (${POP}%)</c:if>
                                        </c:when>
                                        <c:when test="${PTY eq '2' or PTY eq '3' or PTY eq '6' or PTY eq '7'}">
                                            눈/비<c:if test="${not empty POP and POP ne '-'}"> (${POP}%)</c:if>
                                        </c:when>
                                        <c:when test="${not empty POP and POP ne '-'}">강수확률 ${POP}%</c:when>
                                        <c:otherwise>맑음</c:otherwise>
                                    </c:choose>
                                    <span class="font-small text-gray">
                                        습도 ${reh}% / 바람 ${wsd}m/s
                                    </span>
                                </div>
                    </c:otherwise>
                </c:choose>

                <div class="button-group center mt-20">
                    <button type="button" class="btn-tpo" onclick="location.href='tpo.do'">
                        TPO 추천
                    </button>
                    <button type="button" class="btn-navigation" onclick="location.href='mypage.do'">
                        마이페이지
                    </button>
                </div>
            </div>

            <!-- 3-Day Forecast -->
            <c:if test="${not empty threeDayForecast}">
                <div class="forecast-container">
                    <c:forEach var="day" items="${threeDayForecast}">
                        <div class="daily-card">
                            <div class="daily-day">${day.dayOfWeek}</div>
                            <div class="daily-date font-small text-dark-gray">${day.date}</div>

                            <div class="weather-icon">
                                <c:choose>
                                    <c:when test="${day.weatherState eq 'sunny'}">☀️</c:when>
                                    <c:when test="${day.weatherState eq 'cloudy'}">☁️</c:when>
                                    <c:when test="${day.weatherState eq 'rainy'}">🌧️</c:when>
                                    <c:when test="${day.weatherState eq 'snowy'}">☃️</c:when>
                                    <c:otherwise>❓</c:otherwise>
                                </c:choose>
                            </div>

                            <div class="temp-range">
                                <span class="temp-min">${day.minTemp}°</span> /
                                <span class="temp-max">${day.maxTemp}°</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>