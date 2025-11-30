<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
</head>
<body>
 <%@ include file="../header.jsp" %>
	<h3>회원 가입</h3>
	<form method="post" action="<c:url value='/regist.do' />">
		id : <input type="text" placeholder="아이디 입력" name="id" required > <br>
		password : <input type="password" placeholder="패스워드 입력" name="pw" required > <br>
		이름 : <input type="text" placeholder="이름 입력" name="uname" required > <br>
		나이 : <input type="number" name="age" min="1" max="120" required> <br>
		성별 : <input type="radio" name="gender" value="male" required> 남 <input type="radio" name="gender" value="female"> 여 <br>
		<!-- 거주지 : <input type="text" name="province" placeholder="ex: 서울"> 도/특별시 <input type="text" name="city" placeholder=""> --> 
		<label for="region">거주 지역(시·도)을 선택하세요:</label>
		  <select id="region" name="region" required> 
		    <option value="">-- 시·도 선택 --</option>
		
		    <!-- 특별시 / 특별자치시 -->
		    <option value="Seoul">서울특별시 (Seoul)</option>
		    <option value="Sejong">세종특별자치시 (Sejong)</option>
		
		    <!-- 광역시 -->
		    <option value="Busan">부산광역시 (Busan)</option>
		    <option value="Daegu">대구광역시 (Daegu)</option>
		    <option value="Incheon">인천광역시 (Incheon)</option>
		    <option value="Gwangju">광주광역시 (Gwangju)</option>
		    <option value="Daejeon">대전광역시 (Daejeon)</option>
		    <option value="Ulsan">울산광역시 (Ulsan)</option>
		
		    <!-- 도 (Province) -->
		    <option value="Gyeonggi">경기도 (Gyeonggi-do)</option>
		    <option value="Gangwon">강원도 (Gangwon-do)</option>
		    <option value="Chungbuk">충청북도 (Chungcheongbuk-do)</option>
		    <option value="Chungnam">충청남도 (Chungcheongnam-do)</option>
		    <option value="Jeonbuk">전라북도 (Jeollabuk-do)</option>
		    <option value="Jeonnam">전라남도 (Jeollanam-do)</option>
		    <option value="Gyeongbuk">경상북도 (Gyeongsangbuk-do)</option>
		    <option value="Gyeongnam">경상남도 (Gyeongsangnam-do)</option>
		    <option value="Jeju">제주특별자치도 (Jeju)</option>
		  </select> <br>
		  
		<!-- 새로 추가: 시·군·구 선택 -->
        <label for="sigungu">거주 지역(시·군·구)을 선택하세요:</label>
        <select id="sigungu" name="sigungu" required>
            <option value="">-- 시·군·구 선택 --</option>
            <!-- region 선택에 따라 자바스크립트에서 동적으로 채움 -->
        </select>
        <br>
		  
		  <h3>선호 정보</h3>
		  선호 브랜드 : <input type="text" placeholder="브랜드 입력" name="brand" ><br>
		  선호 색상 : <input type="text" placeholder="색상 또는 계열 입력" name="color" ><br>
		  퍼스널 컬러 : <input type="text" placeholder="퍼스널 컬러 입력" name="pcolor" ><br>
		  
		  <input type="submit" value="회원 가입"><input type="reset" value="모두 초기화">
	</form>
	<%@ include file="../footer.jsp" %>
	<!-- 시/도 - 시/군/구 연동 자바스크립트 (외부 파일) -->
    <script src="../js/region.js"></script>
</body>
</html>