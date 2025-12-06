<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>나의 코디 추천 히스토리</title>
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/custom_grid.css'/>">
            </head>

            <body>
                <%@ include file="../header.jsp" %>

                    <div class="container">
                        <div class="content-card">
                            <h2 class="text-center mb-30">📝 나의 코디 추천 히스토리</h2>

                            <c:choose>
                                <c:when test="${empty historyList}">
                                    <div class="text-center p-50">
                                        <p>아직 추천받은 코디가 없습니다.</p>
                                        <button type="button" class="btn-tpo"
                                            onclick="location.href='<c:url value='/tpo.do' />'">코디 추천 받으러 가기</button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- 히스토리 3열 그리드 -->
                                    <div class="history-grid">
                                        <c:forEach var="h" items="${historyList}">
                                            <div class="history-card" style="cursor: pointer;"
                                                onclick="location.href='<c:url value='/historyDetail.do'/>?hId=${h.hId}'">
                                                <!-- 날짜/시간 -->
                                                <div class="history-date">📅 ${h.requestDate} ${h.requestTime}</div>

                                                <!-- 활동 정보 -->
                                                <div class="history-what">📍 ${h.what}</div>

                                                <!-- 날씨 요약 (있을 경우) -->
                                                <c:if test="${not empty h.weatherSummary}">
                                                    <div
                                                        style="background: #E3F2FD; padding: 10px; border-radius: 8px; margin-bottom: 10px; font-size: 0.9em;">
                                                        🌤️ ${h.weatherSummary}
                                                    </div>
                                                </c:if>

                                                <!-- AI 추천 미리보기 -->
                                                <div class="history-recommend">
                                                    <c:choose>
                                                        <c:when test="${fn:length(h.aiRecommend) > 100}">
                                                            ${fn:substring(h.aiRecommend, 0, 100)}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${h.aiRecommend}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>

                                                <div
                                                    style="text-align: right; margin-top: 10px; color: #4A90E2; font-size: 0.9em;">
                                                    클릭하여 상세 보기 →
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <!-- 페이지 하단 버튼 -->
                                    <div class="button-group center" style="margin-top: 30px;">
                                        <button type="button" class="btn-secondary"
                                            onclick="location.href='<c:url value='/mypage.do' />'">마이페이지로</button>
                                        <button type="button" class="btn-tpo"
                                            onclick="location.href='<c:url value='/tpo.do' />'">새 코디 추천받기</button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <%@ include file="../footer.jsp" %>
            </body>

            </html>