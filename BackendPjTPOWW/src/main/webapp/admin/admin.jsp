<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
</head>
<body>
<h2>관리자 페이지 - 회원 목록</h2>

<table border="1">
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

    <c:forEach var="u" items="${userList}">
        <tr>
            <td>${u.un}</td>
            <td>${u.id}</td>
            <td>${u.uname}</td>
            <td>${u.age}</td>
            <td>${u.gender}</td>
            <td>${u.region}</td>
            <td>${u.sigungu}</td>
            <td>${u.role}</td>
            <td>${u.brand}</td>
            <td>${u.color}</td>
            <td>${u.pcolor}</td>
            <td>
                <!-- 강제 탈퇴 -->
                <form action="adminDelete.do" method="post" style="display:inline;"
                      onsubmit="return confirm('정말 이 회원을 삭제하시겠습니까?');">
                    <input type="hidden" name="un" value="${u.un}">
                    <input type="submit" value="탈퇴">
                </form>

                <!-- 여기 나중에 "수정" 버튼도 붙일 수 있음 -->
                <!-- 관리자 수정 페이지로 이동 -->
    			<form action="<c:url value='/adminEdit.do' />" method="get" style="display:inline;">
        			<input type="hidden" name="un" value="${u.un}">
        			<input type="submit" value="수정">
   				</form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
