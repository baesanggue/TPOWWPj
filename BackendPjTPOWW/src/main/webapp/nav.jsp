<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>nav</title>
</head>
<body>

	<span>nav</span>

	<!-- 로그인 X (session에 loginUser 없음) -->
	<c:if test="${empty sessionScope.udto}">
		<form method="post" action="login.do">
			id: <input type="text" name="id" placeholder="ID">
			password: <input type="password" name="pw" placeholder="PASSWORD">
			<input type="submit" value="login">
			<input type="reset" value="reset">
			<a href="regist.jsp">회원 가입</a>
		</form>
	</c:if>

	<!-- 로그인 O -->
	<c:if test="${not empty sessionScope.udto}">
		<p>
			<strong>${sessionScope.udto.uname}</strong> 님 환영합니다! 
			<a href="mypage.do">마이페이지</a> 
			<c:if test="${sessionScope.udto.role == 'ADMIN' }">
				<a href="admin.do">관리자 페이지</a>
			</c:if>
			<a href="logout.do">로그아웃</a>
		</p>
	</c:if>

</body>
</html>
