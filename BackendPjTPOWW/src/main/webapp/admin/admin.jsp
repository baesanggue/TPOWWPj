<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>관리자 페이지 - 회원 목록</title>
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
        </head>

        <body>
            <%@ include file="../header.jsp" %>

                <div class="container">
                    <div class="content-card" style="max-width: 1200px; margin: 0 auto;">
                        <h2 style="text-align: center; margin-bottom: 30px;">👥 관리자 페이지 - 회원 목록</h2>

                        <div style="overflow-x: auto;">
                            <table class="cartoon-table">
                                <thead>
                                    <tr>
                                        <th>UN</th>
                                        <th>ID</th>
                                        <th>이름</th>
                                        <th>나이</th>
                                        <th>성별</th>
                                        <th>시/도</th>
                                        <th>시/군/구</th>
                                        <th>ROLE</th>
                                        <th>브랜드</th>
                                        <th>색상</th>
                                        <th>퍼스널컬러</th>
                                        <th>관리</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="u" items="${userList}">
                                        <tr>
                                            <td>${u.un}</td>
                                            <td>${u.id}</td>
                                            <td>${u.uname}</td>
                                            <td>${u.age}</td>
                                            <td>${u.gender}</td>
                                            <td>${u.region}</td>
                                            <td>${u.sigungu}</td>
                                            <td>
                                                <span
                                                    style="font-weight: bold; color: ${u.role == 'ADMIN' ? '#ff9a9e' : '#4facfe'}">
                                                    ${u.role}
                                                </span>
                                            </td>
                                            <td>${u.brand}</td>
                                            <td>${u.color}</td>
                                            <td>${u.pcolor}</td>
                                            <td>
                                                <div style="display: flex; gap: 5px; justify-content: center;">
                                                    <!-- 관리자 수정 페이지로 이동 -->
                                                    <form action="<c:url value='/adminEdit.do' />" method="get"
                                                        style="margin:0;">
                                                        <input type="hidden" name="un" value="${u.un}">
                                                        <button type="submit" class="btn-primary"
                                                            style="padding: 5px 10px; font-size: 0.9em;">수정</button>
                                                    </form>

                                                    <!-- 탈퇴 버튼 조건부 표시 -->
                                                    <c:if test="${sessionScope.udto.un != u.un && u.role != 'ADMIN'}">
                                                        <form action="adminDelete.do" method="post" style="margin:0;"
                                                            onsubmit="return confirm('정말 이 회원을 삭제하시겠습니까?');">
                                                            <input type="hidden" name="un" value="${u.un}">
                                                            <button type="submit" class="btn-secondary"
                                                                style="padding: 5px 10px; background: #ff6b6b; color: white; border: none; border-radius: 20px; cursor: pointer;">탈퇴</button>
                                                        </form>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div style="text-align: center; margin-top: 30px;">
                            <a href="<c:url value='/index.jsp' />" class="btn-secondary">메인 페이지로 이동</a>
                        </div>
                    </div>
                </div>

                <%@ include file="../footer.jsp" %>
        </body>

        </html>