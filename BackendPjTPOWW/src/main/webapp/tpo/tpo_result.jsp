<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AI 코디 추천 결과</title>
</head>
<body>
    <jsp:include page="../header.jsp" />
    <jsp:include page="../nav.jsp" />

    <div class="container" style="width: 80%; margin: 0 auto; padding: 20px;">
        <h2>👗 AI 코디 추천 결과</h2>
        
        <div style="background: #f9f9f9; padding: 20px; border-radius: 10px; margin-bottom: 20px;">
            <h3>🌤️ 날씨 요약</h3>
            <p>${tpoResult.weatherSummary}</p>
        </div>

        <div style="background: #fff; border: 1px solid #ddd; padding: 20px; border-radius: 10px;">
            <h3>🤖 AI의 추천</h3>
            <div style="white-space: pre-wrap; line-height: 1.6;">${tpoResult.aiRecommend}</div>
        </div>
        
        <div style="text-align: center; margin-top: 30px;">
            <a href="tpo.do"><button>다시 추천받기</button></a>
            <a href="${pageContext.request.contextPath}/index.jsp"><button>메인으로</button></a>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />
</body>
</html>