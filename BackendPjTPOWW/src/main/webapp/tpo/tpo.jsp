<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>TPO 코디 추천</title>
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
        </head>

        <body>
            <%@ include file="../header.jsp" %>

                <div class="container">
                    <div class="content-card w-800">
                        <c:if test="${empty sessionScope.udto}">
                            <p class="text-center">로그인 후 이용 가능합니다.</p>
                            <div class="text-center mt-20">
                                <a href="${pageContext.request.contextPath}/index.jsp" class="btn-secondary">메인으로</a>
                            </div>
                        </c:if>

                        <c:if test="${not empty sessionScope.udto}">
                            <h2 class="text-center mb-10">TPO 코디 추천 요청</h2>
                            <p class="text-center color-gray mb-30">
                                로그인한 회원 정보 + 추가 입력값을 기반으로 AI에게 코디를 추천받습니다.
                            </p>

                            <form action="<c:url value='/tpoRecommend.do' />" method="post" class="cartoon-form">
                                <!-- 0. 기본 정보 표시 -->
                                <div class="user-info-summary">
                                    <strong>ID:</strong> ${sessionScope.udto.id} &nbsp;|&nbsp;
                                    <strong>이름:</strong> ${sessionScope.udto.uname}
                                </div>

                                <!-- 1. 성별 -->
                                <div class="form-group">
                                    <label>성별</label>
                                    <div class="radio-group">
                                        <label><input type="radio" name="gender" value="male" <c:if
                                                test="${sessionScope.udto.gender == 'male'}">checked
                        </c:if>> 남</label>
                        <label><input type="radio" name="gender" value="female" <c:if
                                test="${sessionScope.udto.gender == 'female'}">checked</c:if>> 여</label>
                    </div>
                </div>

                <!-- 2. 나이 -->
                <div class="form-group">
                    <label for="age">나이</label>
                    <input type="number" id="age" name="age" value="${sessionScope.udto.age}" min="1" max="120" required
                        class="form-input">
                </div>

                <!-- 3. 어디서 -->
                <div class="form-group">
                    <label>어디서 (장소/지역)</label>
                    <div style="display: flex; gap: 10px;">
                        <select id="region" name="region" required class="form-input" style="flex: 1;">
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
                        <select id="sigungu" name="sigungu" required class="form-input" style="flex: 1;">
                            <option value="">-- 시·군·구 선택 --</option>
                        </select>
                    </div>
                </div>

                <!-- 4. 실내/야외 -->
                <div class="form-group">
                    <label>실내 / 야외</label>
                    <div class="checkbox-group">
                        <label><input type="checkbox" name="indoor" value="Y"> 실내</label>
                        <label><input type="checkbox" name="outdoor" value="Y"> 야외</label>
                    </div>
                    <p class="form-hint">※ 둘 다 체크하면 실내/야외 모두 고려합니다.</p>
                </div>

                <!-- 5. 언제 -->
                <div class="form-group">
                    <label>언제 (날짜/시간)</label>
                    <div style="display: flex; gap: 10px;">
                        <input type="date" id="whenDate" name="whenDate" required class="form-input"
                            style="flex: 1; cursor: pointer;" onclick="this.showPicker()">
                        <select id="whenTime" name="whenTime" required class="form-input" style="flex: 1;">
                            <c:forEach var="i" begin="0" end="23">
                                <option value="${i < 10 ? '0' : ''}${i}:00">${i < 10 ? '0' : '' }${i}시</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- 6. 무엇을 -->
                <div class="form-group">
                    <label for="what">무엇을 (활동 내용)</label>
                    <input type="text" id="what" name="what" placeholder="예: 친구랑 카페에서 만남, 회사 면접, 데이트 등" required
                        class="form-input full-width">
                </div>

                <hr class="dashed-line">

                <!-- 7. 선호 정보 -->
                <h3>선호 정보 (이번 추천에만 적용)</h3>
                <div class="form-group">
                    <label>선호 브랜드</label>
                    <input type="text" name="brand" value="${empty pdto ? '' : pdto.brand}"
                        class="form-input full-width">
                </div>
                <div class="form-group">
                    <label>선호 색상</label>
                    <input type="text" name="color" value="${empty pdto ? '' : pdto.color}"
                        class="form-input full-width">
                </div>
                <div class="form-group">
                    <label>퍼스널 컬러</label>
                    <input type="text" name="pcolor" value="${empty pdto ? '' : pdto.pcolor}"
                        class="form-input full-width">
                </div>

                <!-- 8. 전송 버튼 -->
                <div class="button-group center mt-30">
                    <button type="submit" class="btn-tpo large">AI에게 코디 추천 받기</button>
                </div>
                <div class="text-center mt-15">
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn-secondary">메인으로</a>
                </div>
                </form>
                </c:if>
                </div>
                </div>

                <!-- Spacer for Dropdowns -->
                <div class="spacer-600"></div>

                <%@ include file="../footer.jsp" %>

                    <script src="${pageContext.request.contextPath}/js/region.js"></script>
                    <script>
                        document.addEventListener('DOMContentLoaded', function () {
                            // Region pre-selection
                            var regionSelect = document.getElementById('region');
                            var sigunguSelect = document.getElementById('sigungu');
                            var savedRegion = "${sessionScope.udto.region}";
                            var savedSigungu = "${sessionScope.udto.sigungu}";

                            if (savedRegion && regionSelect) {
                                regionSelect.value = savedRegion;
                                regionSelect.dispatchEvent(new Event('change'));

                                if (savedSigungu && sigunguSelect) {
                                    sigunguSelect.value = savedSigungu;
                                }
                            }

                            // Date min/max setting
                            var today = new Date();
                            var dd = String(today.getDate()).padStart(2, '0');
                            var mm = String(today.getMonth() + 1).padStart(2, '0');
                            var yyyy = today.getFullYear();
                            var todayStr = yyyy + '-' + mm + '-' + dd;

                            var maxDate = new Date();
                            maxDate.setDate(maxDate.getDate() + 10);
                            var ddMax = String(maxDate.getDate()).padStart(2, '0');
                            var mmMax = String(maxDate.getMonth() + 1).padStart(2, '0');
                            var yyyyMax = maxDate.getFullYear();
                            var maxDateStr = yyyyMax + '-' + mmMax + '-' + ddMax;

                            var dateInput = document.getElementById('whenDate');
                            // 날짜 선택 시 중기예보 데이터 미리 로드
                            dateInput.addEventListener('change', function () {
                                var selectedRegion = document.getElementById('region').value;
                                if (selectedRegion && this.value) {
                                    var url = '${pageContext.request.contextPath}/weather.do?region=' + selectedRegion;
                                    fetch(url, { method: 'POST' })
                                        .then(response => console.log('중기예보 데이터 로드 완료'))
                                        .catch(error => console.error('weather.do 호출 실패:', error));
                                }
                            });
                            if (dateInput) {
                                dateInput.setAttribute('min', todayStr);
                                dateInput.setAttribute('max', maxDateStr);
                            }
                        })
                    </script>
        </body>

        </html>