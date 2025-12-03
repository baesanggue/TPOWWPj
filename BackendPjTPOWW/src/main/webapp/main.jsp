<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container">
    <h1>오늘의 날씨와 옷차림</h1>

    <div class="weather-section">
        <h2>현재 날씨</h2>
        
        <c:choose>
            <c:when test="${not empty t1h}">
                <div>
                    <span>기온: <strong>${t1h}℃</strong></span>
                    <span>습도: <strong>${reh}%</strong></span>
                    <span>강수량: <strong>${rn1}mm</strong></span>
                    <span>바람: <strong>${wsd}m/s</strong></span>
                </div>
            </c:when>
            <c:otherwise>
                <p>날씨 정보가 없습니다. <a href="weather">날씨 정보 불러오기</a></p>
            </c:otherwise>
        </c:choose>

        <hr>

        <h3>단기 예보</h3>
        <c:if test="${not empty forecastList}">
            <table border="1">
                <thead>
                    <tr>
                        <th>날짜</th>
                        <th>시간</th>
                        <th>항목</th>
                        <th>예보값</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${forecastList}" end="5">
                        <tr>
                            <td>${item.fcstDate}</td>
                            <td>${item.fcstTime}</td>
                            <td>${item.category}</td>
                            <td>
                                ${item.fcstValue}
                                <c:if test="${item.category eq 'TMP'}">℃</c:if>
                                <c:if test="${item.category eq 'REH'}">%</c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>

    <div class="btn-area">
        <button type="button" onclick="location.href='tpo.do'">
            👗 AI 코디 추천받기
        </button>
    </div>
</div>