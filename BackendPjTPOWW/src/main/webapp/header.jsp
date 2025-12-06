<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/custom_grid.css'/>">

        <header>
            <div class="logo">
                <a href="${pageContext.request.contextPath}/index.jsp">TPOWW</a>
            </div>

            <div class="header-right">
                <c:choose>
                    <c:when test="${empty sessionScope.udto}">
                        <form method="post" action="${pageContext.request.contextPath}/login.do"
                            style="margin: 0; display: flex; gap: 10px; align-items: center;">
                            <input type="text" name="id" class="header-input" placeholder="ID">
                            <input type="password" name="pw" class="header-input" placeholder="비밀번호">
                            <button type="submit" class="btn-primary">로그인</button>
                            <button type="button" class="btn-navigation"
                                onclick="location.href='${pageContext.request.contextPath}/member/regist.jsp'">회원가입</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <span style="font-weight: bold; margin-right: 10px;">${sessionScope.udto.uname}님</span>
                        <button type="button" class="btn-navigation"
                            onclick="location.href='${pageContext.request.contextPath}/mypage.do'">마이페이지</button>
                        <c:if test="${sessionScope.udto.role == 'ADMIN'}">
                            <button type="button" class="btn-danger"
                                onclick="location.href='${pageContext.request.contextPath}/admin.do'">관리자</button>
                        </c:if>
                        <button type="button" class="btn-secondary"
                            onclick="location.href='${pageContext.request.contextPath}/logout.do'">로그아웃</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <!-- Error Message Display -->
        <c:if test="${not empty requestScope.errorMsg}">
            <div style="text-align: center; color: red; margin-top: 10px; font-weight: bold;">
                ${requestScope.errorMsg}
            </div>
        </c:if>