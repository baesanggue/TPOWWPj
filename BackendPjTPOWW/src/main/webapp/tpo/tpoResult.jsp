<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>AI 코디 추천 결과</title>
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/custom_grid.css'/>">
            </head>

            <body>
                <%@ include file="../header.jsp" %>

                    <div class="container">
                        <div class="content-card w-800">
                            <h2 class="text-center mb-30">👗 AI 코디 추천 결과</h2>

                            <c:choose>
                                <c:when test="${empty tpoResult}">
                                    <div class="text-center p-50">
                                        <p>추천 결과가 없습니다. 다시 시도해주세요.</p>
                                        <a href="tpo.do" class="btn-tpo">다시 추천받기</a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- 날짜/시간 -->
                                    <div class="detail-section">
                                        <h3>📅 날짜 및 시간</h3>
                                        <p>${tpoResult.date} ${tpoResult.time}</p>
                                    </div>
                                    <!-- 활동 정보 -->
                                    <div class="detail-section">
                                        <h3>📍 활동 내용</h3>
                                        <p>${tpoResult.activity}</p>
                                    </div>
                                    <!-- 날씨 정보 -->
                                    <c:if test="${not empty tpoResult.weatherSummary}">
                                        <div class="detail-section">
                                            <h3>🌤️ 날씨 요약</h3>
                                            <p>${tpoResult.weatherSummary}</p>
                                        </div>
                                    </c:if>
                                    <!-- 추천 이유 -->
                                    <c:if test="${not empty tpoResult.reasonSummary}">
                                        <div class="detail-section" style="background: #FFF9E6;">
                                            <h3>💡 추천 이유</h3>
                                            <div class="detail-content">${tpoResult.reasonSummary}</div>
                                        </div>
                                    </c:if>
                                    <!-- AI 추천 내용 -->
                                    <div class="detail-section">
                                        <h3>🤖 AI 추천 코디</h3>
                                        <div class="detail-content">${fn:trim(tpoResult.aiRecommend)}</div>
                                    </div>

                                    <div class="button-group center mt-40">
                                        <button type="button" class="btn-secondary"
                                            onclick="location.href='${pageContext.request.contextPath}/mypage.do'">마이페이지로</button>
                                        <button type="button" class="btn-secondary"
                                            onclick="location.href='${pageContext.request.contextPath}/history.do'">히스토리
                                            보기</button>
                                        <button type="button" class="btn-tpo" onclick="location.href='tpo.do'">새 코디
                                            추천받기</button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <%@ include file="../footer.jsp" %>
            </body>

            </html>