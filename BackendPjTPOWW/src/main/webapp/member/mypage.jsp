<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>마이페이지</title>
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
        </head>

        <body>

            <c:if test="${empty sessionScope.udto}">
                <script>
                    alert("로그인 후 이용 가능합니다.");
                    location.href = "index.jsp";
                </script>
            </c:if>

            <%@ include file="../header.jsp" %>

                <div class="container">
                    <div class="content-card w-1000">
                        <c:if test="${not empty sessionScope.udto}">
                            <h2 class="text-center mb-30">${sessionScope.udto.uname} 님의 마이페이지</h2>

                            <div class="mypage-layout">
                                <!-- Left Column: User Info Form -->
                                <div class="mypage-left mypage-column">
                                    <h3>내 정보 수정</h3>
                                    <form action="mypage.do" method="post" class="cartoon-form">
                                        <div class="form-group">
                                            <label>ID</label>
                                            <input type="text" name="id" value="${sessionScope.udto.id}" readonly
                                                class="form-input readonly">
                                        </div>
                                        <div class="form-group">
                                            <label>이름</label>
                                            <input type="text" name="uname" value="${sessionScope.udto.uname}"
                                                class="form-input">
                                        </div>
                                        <div class="form-group">
                                            <label>나이</label>
                                            <input type="number" name="age" value="${sessionScope.udto.age}" min="1"
                                                max="120" class="form-input">
                                        </div>
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

                <!-- Region Dropdowns -->
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
                    <input type="text" name="brand" value="${pdto.brand}" class="form-input">
                </div>
                <div class="form-group">
                    <label>선호 색상</label>
                    <input type="text" name="color" value="${pdto.color}" class="form-input">
                </div>
                <div class="form-group">
                    <label>퍼스널 컬러</label>
                    <input type="text" name="pcolor" value="${pdto.pcolor}" class="form-input">
                </div>

                <div class="button-group center mt-20">
                    <button type="submit" class="btn-primary">정보 수정</button>
                    <a href="<c:url value='/index.jsp' />" class="btn-secondary"
                        style="text-decoration: none; display: flex; align-items: center;">메인으로</a>
                </div>
                </form>
                </div>

                <!-- Right Column: History Summary -->
                <div class="mypage-right mypage-column">
                    <h3>최근 코디 추천 히스토리</h3>
                    <c:choose>
                        <c:when test="${not empty historyList}">
                            <ul class="history-summary-list list-none">
                                <c:forEach var="h" items="${historyList}" begin="0" end="4">
                                    <li class="history-item">
                                        <div class="history-date">${h.requestDate} ${h.requestTime}</div>
                                        <div class="history-what">${h.what}</div>
                                        <div class="history-recommend">${h.aiRecommend}</div>
                                    </li>
                                </c:forEach>
                            </ul>
                            <div class="text-center mt-20">
                                <a href="<c:url value='/history.do' />" class="btn-secondary">히스토리 전체 보기</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center p-50 color-gray">
                                <p>아직 추천 받은 내역이 없습니다.</p>
                                <a href="<c:url value='/tpo.do' />" class="btn-tpo"
                                    style="font-size: 1em; padding: 10px 20px;">코디 추천 받기</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                </div>
                </c:if>
                </div>
                </div>

                <!-- Spacer for Dropdowns -->
                <div class="spacer-600"></div>

                <%@ include file="../footer.jsp" %>

                    <script src="${pageContext.request.contextPath}/js/region.js"></script>
                    <script>
                        document.addEventListener('DOMContentLoaded', function () {
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
                        });
                    </script>
        </body>

        </html>