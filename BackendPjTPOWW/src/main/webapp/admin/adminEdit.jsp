<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>관리자 - 회원 정보 수정</title>
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartoon_theme.css'/>">
        </head>

        <body>
            <%@ include file="../header.jsp" %>

                <div class="container">
                    <div class="content-card" style="max-width: 600px; margin: 0 auto;">
                        <h2 style="text-align: center; margin-bottom: 30px;">✏️ 관리자 - 회원 정보 수정</h2>
                        <p style="text-align: center; color: #666; margin-bottom: 20px;">UN: ${udto.un} / ID: ${udto.id}
                        </p>

                        <form action="<c:url value='/adminEdit.do' />" method="post" class="cartoon-form">
                            <input type="hidden" name="un" value="${udto.un}">

                            <div class="form-group">
                                <label>ID</label>
                                <input type="text" name="id" value="${udto.id}" readonly class="form-input readonly">
                            </div>

                            <div class="form-group">
                                <label>이름</label>
                                <input type="text" name="uname" value="${udto.uname}" class="form-input">
                            </div>

                            <div class="form-group">
                                <label>나이</label>
                                <input type="number" name="age" value="${udto.age}" class="form-input">
                            </div>

                            <div class="form-group">
                                <label>성별</label>
                                <div class="radio-group">
                                    <label><input type="radio" name="gender" value="male" <c:if
                                            test="${udto.gender == 'male'}">checked</c:if>> 남</label>
                                    <label><input type="radio" name="gender" value="female" <c:if
                                            test="${udto.gender == 'female'}">checked</c:if>> 여</label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>지역(시/도)</label>
                                <input type="text" name="region" value="${udto.region}" class="form-input">
                            </div>

                            <div class="form-group">
                                <label>지역(시·군·구)</label>
                                <input type="text" name="sigungu" value="${udto.sigungu}" class="form-input">
                            </div>

                            <div class="form-group">
                                <label>ROLE</label>
                                <c:choose>
                                    <c:when test="${sessionScope.udto.un == udto.un || udto.role == 'ADMIN'}">
                                        <input type="text" value="${udto.role}" readonly class="form-input readonly">
                                        <input type="hidden" name="role" value="${udto.role}">
                                    </c:when>
                                    <c:otherwise>
                                        <select name="role" class="form-input">
                                            <option value="USER" ${udto.role=='USER' ? 'selected' : '' }>USER</option>
                                            <option value="ADMIN" ${udto.role=='ADMIN' ? 'selected' : '' }>ADMIN
                                            </option>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
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

                            <div class="button-group center" style="margin-top: 30px;">
                                <button type="submit" class="btn-primary">수정 저장</button>
                                <a href="<c:url value='/admin.do' />" class="btn-secondary"
                                    style="text-decoration: none; padding: 10px 20px; background: #ddd; color: #333; border-radius: 20px;">목록으로</a>
                            </div>
                        </form>

                        <!-- 탈퇴 버튼 조건부 표시: 본인 및 다른 관리자 삭제 불가 -->
                        <c:if test="${sessionScope.udto.un != udto.un && udto.role != 'ADMIN'}">
                            <div
                                style="text-align: center; margin-top: 20px; border-top: 1px solid #eee; padding-top: 20px;">
                                <form action="<c:url value='/adminDelete.do' />" method="post"
                                    onsubmit="return confirm('정말 이 회원을 삭제하시겠습니까?');">
                                    <input type="hidden" name="un" value="${udto.un}">
                                    <button type="submit" class="btn-secondary"
                                        style="background: #ff6b6b; color: white; border: none;">회원 강제 탈퇴</button>
                                </form>
                            </div>
                        </c:if>
                    </div>
                </div>

                <%@ include file="../footer.jsp" %>
        </body>

        </html>