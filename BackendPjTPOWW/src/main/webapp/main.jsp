<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container" style="text-align: center; padding: 20px;">
    <h1>오늘의 날씨와 옷차림</h1>

    <div class="region-info" style="margin-bottom: 20px;">
        <h3 style="color: #333;">
            📍 현재 위치: 
            <span style="color: #007bff;">
                ${not empty currentRegion ? currentRegion : '서울특별시 (기본)'}
            </span>
        </h3>
        <c:if test="${empty sessionScope.udto}">
            <p style="color: gray; font-size: 0.9em;">
                (로그인하시면 회원가입 시 등록한 지역의 날씨를 볼 수 있습니다)
            </p>
        </c:if>
    </div>

    <div class="weather-section" style="background-color: #f0f8ff; padding: 20px; border-radius: 10px; margin: 0 auto; max-width: 800px;">
        <h2>현재 날씨</h2>
        
        <c:choose>
            <c:when test="${not empty t1h and t1h != '-'}">
                <div style="font-size: 1.2em; margin: 15px 0;">
                    <span style="margin: 0 15px;">기온: <strong>${t1h}℃</strong></span>
                    <span style="margin: 0 15px;">습도: <strong>${reh}%</strong></span>
                    <span style="margin: 0 15px;">강수량: <strong>${rn1}mm</strong></span>
                    <span style="margin: 0 15px;">바람: <strong>${wsd}m/s</strong></span>
                </div>
            </c:when>
            <c:otherwise>
                <p>날씨 정보를 불러오는 중입니다... <a href="weather.do">새로고침</a></p>
            </c:otherwise>
        </c:choose>

        <hr style="margin: 20px 0; border-top: 1px solid #ddd;">

        <h3>단기 예보 (오전/오후)</h3>
        <c:if test="${not empty dailyList}">
            <table border="1" style="margin: 0 auto; width: 70%; border-collapse: collapse; text-align: center; background-color: white;">
                <thead style="background-color: #e6e6fa;">
                    <tr>
                        <th style="padding: 8px;">날짜</th>
                        <th style="padding: 8px;">오전 기온</th>
                        <th style="padding: 8px;">오후 기온</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="entry" items="${dailyList}">
                        <tr>
                            <td style="padding: 8px;">${entry.key}</td>
                            <td style="padding: 8px;">
                                <c:out value="${entry.value['morningTemp'] != null ? entry.value['morningTemp'] : '-'}" /> ℃
                            </td>
                            
                            <td style="padding: 8px;">
                                <c:out value="${entry.value['afternoonTemp'] != null ? entry.value['afternoonTemp'] : '-'}" /> ℃
                            </td>
                            
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>

    <div class="btn-area" style="margin-top: 40px;">
        <button type="button" onclick="location.href='tpo.do'" 
                style="background-color: #ff7f50; color: white; padding: 15px 40px; font-size: 1.3em; border: none; border-radius: 50px; cursor: pointer; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
            👗 AI 코디 추천받기
        </button>
    </div>
</div>
