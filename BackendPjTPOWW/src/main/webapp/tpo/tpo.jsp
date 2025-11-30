<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TPO 코디 추천</title>
</head>
<body>

<%@ include file="../header.jsp" %>

<c:if test="${empty sessionScope.udto}">
    <p>로그인 후 이용 가능합니다.</p>
    <a href="index.jsp">메인으로</a>
</c:if>

<c:if test="${not empty sessionScope.udto}">
    <h2>TPO 코디 추천 요청</h2>
    <p>로그인한 회원 정보 + 추가 입력값을 기반으로 AI에게 코디를 추천받습니다.</p>

    <!-- 
        TPO 정보 입력 폼
        나중에 tpoRecommend.do 서블릿에서 이 값들을 받아서 
        기상청 API + AI API 에 넘길 예정
    -->
    <form action="<c:url value='/tpoRecommend.do' />" method="post">
        <!-- 0. 기본 정보 표시 (id, 이름 정도) -->
        <p>
            <strong>ID:</strong> ${sessionScope.udto.id}<br>
            <strong>이름:</strong> ${sessionScope.udto.uname}
        </p>
        <hr>

        <!-- 1. 성별 (radio) -->
        <div>
            <span>성별</span><br>
            <label>
                <input type="radio" name="gender" value="male"
                    <c:if test="${sessionScope.udto.gender == 'male'}">checked</c:if>> 남
            </label>
            <label>
                <input type="radio" name="gender" value="female"
                    <c:if test="${sessionScope.udto.gender == 'female'}">checked</c:if>> 여
            </label>
        </div>
        <br>

        <!-- 2. 나이 -->
        <div>
            <label for="age">나이</label><br>
            <input type="number" id="age" name="age"
                   value="${sessionScope.udto.age}"
                   min="1" max="120" required>
        </div>
        <br>

        <!-- 3. 어디서: 시/도 + 시/군/구 (텍스트로, 수정 가능) -->
        <div>
            <span>어디서 (장소/지역)</span><br>

            <label for="region">시/도</label><br>
            <input type="text" id="region" name="region"
                   value="${sessionScope.udto.region}" required>
            <br><br>

            <label for="sigungu">시/군/구</label><br>
            <input type="text" id="sigungu" name="sigungu"
                   value="${sessionScope.udto.sigungu}" required>
        </div>
        <br>

        <!-- 4. 실내/야외 체크박스 -->
        <div>
            <span>실내 / 야외</span><br>
            <!-- 둘 다 체크되면 나중에 AI 프롬프트에서 '실내와 야외 모두 고려' 로 처리 -->
            <label>
                <input type="checkbox" name="indoor" value="Y">
                실내
            </label>
            <label>
                <input type="checkbox" name="outdoor" value="Y">
                야외
            </label>
            <p style="font-size:0.9em; color:gray;">
                ※ 실내/야외 둘 다 체크하면 AI가 실내와 야외를 모두 고려해서 추천하게 만들 수 있습니다.
            </p>
        </div>
        <br>

        <!-- 5. 언제: 날짜 + 시간 -->
        <div>
            <span>언제 (날짜/시간)</span><br>
            <label for="whenDate">날짜</label><br>
            <input type="date" id="whenDate" name="whenDate" required>
            <br><br>
            <label for="whenTime">시간</label><br>
            <input type="time" id="whenTime" name="whenTime" required>
        </div>
        <br>

        <!-- 6. 무엇을 (활동) -->
        <div>
            <label for="what">무엇을 (활동 내용)</label><br>
            <input type="text" id="what" name="what"
                   placeholder="예: 친구랑 카페에서 만남, 회사 면접, 데이트 등"
                   required style="width:300px;">
        </div>
        <br>

        <hr>

        <!-- 7. 선호 정보 (user_pref) -->
        <h3>선호 정보 (필요시 수정 가능, DB는 수정 안 함)</h3>

        <div>
            선호 브랜드:<br>
            <input type="text" name="brand"
                   value="${empty pdto ? '' : pdto.brand}">
        </div>
        <br>

        <div>
            선호 색상:<br>
            <input type="text" name="color"
                   value="${empty pdto ? '' : pdto.color}">
        </div>
        <br>

        <div>
            퍼스널 컬러:<br>
            <input type="text" name="pcolor"
                   value="${empty pdto ? '' : pdto.pcolor}">
        </div>
        <br>

        <p style="font-size:0.9em; color:gray;">
            ※ 여기에서 수정한 선호 정보는 <strong>DB에는 반영되지 않고</strong>,<br>
            이번 TPO 코디 추천에만 사용됩니다. (타인 코디 추천에도 사용 가능)
        </p>

        <hr>

        <!-- 8. 전송 버튼 -->
        <div>
            <button type="submit">AI에게 코디 추천 받기</button>
            <a href="<c:url value='/index.jsp' />">메인으로</a>
        </div>
    </form>
</c:if>

</body>
</html>
