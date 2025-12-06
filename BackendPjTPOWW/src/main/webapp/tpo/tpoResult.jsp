<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>AI 코디 추천 결과</title>
                <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
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
                                    <div class="mb-30">
                                        <h3 class="dashed-bottom">
                                            🌤️ 날씨 요약
                                        </h3>
                                        <p class="highlight-box" style="font-size: 1.1em; line-height: 1.6;">
                                            ${tpoResult.weatherSummary}
                                        </p>
                                    </div>

                                    <c:if test="${not empty tpoResult.reasonSummary}">
                                        <div class="mb-30">
                                            <h3 class="dashed-bottom">
                                                💡 선택 이유 요약
                                            </h3>
                                            <div class="result-box"
                                                style="text-align: left !important; white-space: pre-wrap;">
                                                ${tpoResult.reasonSummary}
                                            </div>
                                        </div>
                                    </c:if>

                                    <div class="mb-30">
                                        <h3 class="dashed-bottom">
                                            🤖 AI의 추천
                                        </h3>
                                        <div class="result-box">
                                            ${fn:trim(tpoResult.aiRecommend)}
                                        </div>
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