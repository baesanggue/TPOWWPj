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
                        <div class="big-icon">
                            <c:choose>
                                <c:when test="${pty eq '1' or pty eq '4' or pty eq '5'}">🌧️</c:when>
                                <c:when test="${pty eq '2' or pty eq '3' or pty eq '6' or pty eq '7'}">☃️</c:when>
                                <c:otherwise>☀️</c:otherwise>
                            </c:choose>
                        </div>

                        <div class="big-temp">
                            ${not empty t1h ? t1h : '-'}°
                        </div>

                        <div class="weather-desc">
                            <c:choose>
                                <c:when test="${pty eq '1' or pty eq '4' or pty eq '5'}">비</c:when>
                                <c:when test="${pty eq '2' or pty eq '3' or pty eq '6' or pty eq '7'}">눈/비</c:when>
                                <c:otherwise>맑음 (강수없음)</c:otherwise>
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
                    <button type="button" class="btn-secondary" onclick="location.href='mypage.do'"
                        style="margin-top: 20px; padding: 15px 30px; font-size: 1.2em; border-radius: 30px;">
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