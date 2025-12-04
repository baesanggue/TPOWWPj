<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>나의 코디 추천 히스토리</title>
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
        </head>

        <body>
            <%@ include file="../header.jsp" %>

                <div class="container">
                    <div class="content-card" style="max-width: 1000px; margin: 0 auto;">
                        <h2 style="text-align: center; margin-bottom: 30px;">📜 나의 코디 추천 히스토리</h2>

                        <c:choose>
                            <c:when test="${empty historyList}">
                                <div style="text-align: center; padding: 50px; color: #666;">
                                    <p style="font-size: 1.2em;">아직 추천 받은 내역이 없습니다.</p>
                                    <button type="button" class="btn-tpo"
                                        onclick="location.href='<c:url value='/tpo.do' />'" style="margin-top: 20px;">코디
                                        추천 받으러 가기</button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="history-list" style="display: flex; flex-direction: column; gap: 20px;">
                                    <c:forEach var="h" items="${historyList}">
                                        <div class="cartoon-form" style="background: #fff; border: 2px solid #eee;">
                                            <div
                                                style="display: flex; justify-content: space-between; border-bottom: 2px dashed #ddd; padding-bottom: 10px; margin-bottom: 10px;">
                                                <span
                                                    style="font-weight: bold; font-size: 1.1em; color: #4facfe;">${h.requestDate}
                                                    ${h.requestTime}</span>
                                                <span style="color: #888; font-size: 0.9em;">요청일시: ${h.createdAt}</span>
                                            </div>
                                            <div style="margin-bottom: 10px;">
                                                <strong>상황:</strong> ${h.what}
                                            </div>
                                            <div style="margin-bottom: 10px;">
                                                <strong>날씨:</strong> ${h.weatherSummary}
                                            </div>
                                            <div
                                                style="background: #f9f9f9; padding: 10px; border-radius: 10px; white-space: pre-wrap; font-size: 0.95em; color: #555;">
                                                ${h.aiRecommend}</div>
                                        </div>
                                    </c:forEach>
                                </div>

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