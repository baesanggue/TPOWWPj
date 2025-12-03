<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 - 회원 정보 수정</title>
</head>
<body>
<h2>관리자 - 회원 정보 수정 (UN: ${udto.un})</h2>

<form action="<c:url value='/adminEdit.do' />" method="post">
    <input type="hidden" name="un" value="${udto.un}">

    ID: <input type="text" name="id" value="${udto.id}" readonly><br>
    이름: <input type="text" name="uname" value="${udto.uname}"><br>
    나이: <input type="number" name="age" value="${udto.age}"><br>

    성별:
    <input type="radio" name="gender" value="male"
        <c:if test="${udto.gender == 'male'}">checked</c:if>> 남
    <input type="radio" name="gender" value="female"
        <c:if test="${udto.gender == 'female'}">checked</c:if>> 여
    <br>

    지역(시/도) : <input type="text" name="region" value="${udto.region}"><br>
	 지역(시·군·구):
    <input type="text" name="sigungu" value="${udto.sigungu}"><br> <!-- ⭐ 추가 -->
    <br>
    ROLE:
    <select name="role">
        <option value="USER"  ${udto.role == 'USER'  ? 'selected' : ''}>USER</option>
        <option value="ADMIN" ${udto.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
    </select>
    <br><br>

    선호 브랜드: <input type="text" name="brand" value="${pdto.brand}"><br>
    선호 색상: <input type="text" name="color" value="${pdto.color}"><br>
    퍼스널 컬러: <input type="text" name="pcolor" value="${pdto.pcolor}"><br>

    <input type="submit" value="수정 저장">
    <a href="<c:url value='/admin.do' />">목록으로</a>
</form>

</body>
</html>
