<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="c" uri="jakarta.tags.core" %>

		<!DOCTYPE html>
		<html>

		<head>
			<meta charset="UTF-8">
			<title>회원 가입</title>
			<link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
		</head>

		<body>
			<%@ include file="../header.jsp" %>

				<div class="container">
					<div class="content-card w-600">
						<h2 class="text-center mb-20">회원 가입</h2>

						<form method="post" action="<c:url value='/regist.do' />" class="cartoon-form">
							<div class="form-group">
								<label>ID</label>
								<input type="text" placeholder="아이디 입력" name="id" required class="form-input">
							</div>

							<div class="form-group">
								<label>Password</label>
								<input type="password" placeholder="패스워드 입력" name="pw" required class="form-input">
							</div>

							<div class="form-group">
								<label>이름</label>
								<input type="text" placeholder="이름 입력" name="uname" required class="form-input">
							</div>

							<div class="form-group">
								<label>나이</label>
								<input type="number" name="age" min="1" max="120" required class="form-input">
							</div>

							<div class="form-group">
								<label>성별</label>
								<div class="radio-group">
									<label><input type="radio" name="gender" value="male" required> 남</label>
									<label><input type="radio" name="gender" value="female"> 여</label>
								</div>
							</div>

							<div class="form-group">
								<label for="region">거주 지역(시·도)</label>
								<select id="region" name="region" required class="form-input">
									<option value="">-- 시·도 선택 --</option>
									<option value="Seoul">서울특별시 (Seoul)</option>
									<option value="Sejong">세종특별자치시 (Sejong)</option>
									<option value="Busan">부산광역시 (Busan)</option>
									<option value="Daegu">대구광역시 (Daegu)</option>
									<option value="Incheon">인천광역시 (Incheon)</option>
									<option value="Gwangju">광주광역시 (Gwangju)</option>
									<option value="Daejeon">대전광역시 (Daejeon)</option>
									<option value="Ulsan">울산광역시 (Ulsan)</option>
									<option value="Gyeonggi">경기도 (Gyeonggi-do)</option>
									<option value="Gangwon">강원도 (Gangwon-do)</option>
									<option value="Chungbuk">충청북도 (Chungcheongbuk-do)</option>
									<option value="Chungnam">충청남도 (Chungcheongnam-do)</option>
									<option value="Jeonbuk">전라북도 (Jeollabuk-do)</option>
									<option value="Jeonnam">전라남도 (Jeollanam-do)</option>
									<option value="Gyeongbuk">경상북도 (Gyeongsangbuk-do)</option>
									<option value="Gyeongnam">경상남도 (Gyeongsangnam-do)</option>
									<option value="Jeju">제주특별자치도 (Jeju)</option>
								</select>
							</div>

							<div class="form-group">
								<label for="sigungu">거주 지역(시·군·구)</label>
								<select id="sigungu" name="sigungu" required class="form-input">
									<option value="">-- 시·군·구 선택 --</option>
								</select>
							</div>

							<hr class="dashed-line">

							<h3>선호 정보</h3>
							<div class="form-group">
								<label>선호 브랜드</label>
								<input type="text" placeholder="브랜드 입력" name="brand" class="form-input">
							</div>

							<div class="form-group">
								<label>선호 색상</label>
								<input type="text" placeholder="색상 또는 계열 입력" name="color" class="form-input">
							</div>

							<div class="form-group">
								<label>퍼스널 컬러</label>
								<input type="text" placeholder="퍼스널 컬러 입력" name="pcolor" class="form-input">
							</div>

							<div class="button-group center mt-20">
								<input type="submit" value="회원 가입" class="btn-primary">
								<input type="reset" value="초기화" class="btn-secondary">
								<a href="<c:url value='/index.jsp' />" class="btn-secondary"
									style="text-decoration: none; display: flex; align-items: center;">메인으로</a>
							</div>
						</form>
					</div>
				</div>

				<%@ include file="../footer.jsp" %>
					<script src="../js/region.js"></script>
		</body>

		</html>