<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
</head>
<body>

<c:if test="${empty sessionScope.udto}">
    <p>로그인 후 이용 가능합니다.</p>
    <a href="index.jsp">메인으로</a>
</c:if>

<c:if test="${not empty sessionScope.udto}">
    <h2>${sessionScope.udto.uname} 님의 마이페이지</h2>

    <form action="mypage.do" method="post">
        <!-- 회원 기본 정보 -->
        ID: <input type="text" name="id" value="${sessionScope.udto.id}" readonly><br>
        이름: <input type="text" name="uname" value="${sessionScope.udto.uname}"><br>
        나이: <input type="number" name="age" value="${sessionScope.udto.age}" min="1" max="120"><br>
        
        성별:
        <input type="radio" name="gender" value="male"
            <c:if test="${sessionScope.udto.gender == 'male'}">checked</c:if>> 남
        <input type="radio" name="gender" value="female"
            <c:if test="${sessionScope.udto.gender == 'female'}">checked</c:if>> 여
        <br>

        지역:
        <input type="text" name="region" value="${sessionScope.udto.region}"><br>
		<input type="text" name="sigungu" value="${sessionScope.udto.sigungu}"><br>
        <hr>

        <!-- 선호 정보 (user_pref) -->
        선호 브랜드:
        <input type="text" name="brand" value="${pdto.brand}"><br>

        선호 색상:
        <input type="text" name="color" value="${pdto.color}"><br>

        퍼스널 컬러:
        <input type="text" name="pcolor" value="${pdto.pcolor}"><br>

        <input type="submit" value="정보 수정하기">
    </form>
    <hr>
    <form action="withdraw.do" method="post"
      onsubmit="return confirm('정말 탈퇴하시겠습니까?');">
    <input type="submit" value="회원 탈퇴">
	</form>
    <a href="index.jsp">메인으로</a>
</c:if>

</body>
</html>
