<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>히스토리 상세</title>
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/custom_grid.css'/>">
                <style>
                    .detail-section {
                        background: #f9f9f9;
                        padding: 20px;
                        border-radius: 10px;
                        margin-bottom: 20px;
                        text-align: left;
                    }

                    .detail-section h3 {
                        margin-top: 0;
                        color: #4A90E2;
                    }

                    .detail-content {
                        white-space: pre-wrap;
                        line-height: 1.6;
                    }
                </style>
            </head>

            <body>
                <%@ include file="../header.jsp" %>

                    <div class="container">
                        <div class="content-card w-800">
                            <h2 class="text-center mb-30">📋 히스토리 상세</h2>

                            <c:choose>
                                <c:when test="${empty history}">
                                    <div class="text-center p-50">
                                        <p>히스토리를 찾을 수 없습니다.</p>
                                        <button type="button" class="btn-secondary"
                                            onclick="location.href='<c:url value='/history.do'/>'">목록으로</button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- 날짜/시간 -->
                                    <div class="detail-section">
                                        <h3>📅 날짜 및 시간</h3>
                                        <p>${history.requestDate} ${history.requestTime}</p>
                                    </div>

                                    <!-- 활동 정보 -->
                                    <div class="detail-section">
                                        <h3>📍 활동 내용</h3>
                                        <p>${history.what}</p>
                                    </div>

                                    <!-- 날씨 정보 -->
                                    <c:if test="${not empty history.weatherSummary}">
                                        <div class="detail-section">
                                            <h3>🌤️ 날씨 요약</h3>
                                            <p>${history.weatherSummary}</p>
                                        </div>
                                    </c:if>

                                    <!-- 추천 이유 -->
                                    <c:if test="${not empty history.reasonSummary}">
                                        <div class="detail-section" style="background: #FFF9E6;">
                                            <h3>💡 추천 이유</h3>
                                            <div class="detail-content">${history.reasonSummary}</div>
                                        </div>
                                    </c:if>

                                    <!-- AI 추천 내용 -->
                                    <div class="detail-section">
                                        <h3>🤖 AI 추천 코디</h3>
                                        <div class="detail-content">${history.aiRecommend}</div>
                                    </div>

                                    <!-- 버튼 -->
                                    <div class="button-group center mt-40">
                                        <button type="button" class="btn-secondary"
                                            onclick="location.href='<c:url value='/history.do'/>'">목록으로</button>
                                        <button type="button" class="btn-tpo"
                                            onclick="location.href='<c:url value='/tpo.do'/>'">새 코디 추천받기</button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <%@ include file="../footer.jsp" %>
            </body>

            </html>