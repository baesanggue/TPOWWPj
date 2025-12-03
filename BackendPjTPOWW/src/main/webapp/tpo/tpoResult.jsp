<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TPO 코디 추천 결과</title>
</head>
<body>
<%@ include file="../header.jsp" %>

<c:if test="${not empty error}">
    <h3 style="color:red;">${error}</h3>
</c:if>

<c:if test="${not empty tpoResult}">
    <h2>TPO 코디 추천 결과</h2>

    <h3>날씨 요약</h3>
    <p>${tpoResult.weatherSummary}</p>

    <h3>AI 추천</h3>
    <pre style="white-space: pre-wrap; font-family: inherit;">
${tpoResult.aiRecommend}
    </pre>

    <details>
        <summary>프롬프트 보기 (디버깅용)</summary>
        <pre style="white-space: pre-wrap; font-family: inherit;">
${tpoResult.prompt}
        </pre>
    </details>
</c:if>

<a href="<c:url value='/tpo.do' />">다시 TPO 입력하기</a>
<a href="<c:url value='/index.jsp' />">메인으로</a>

<%@ include file="../footer.jsp" %>
</body>
</html>
