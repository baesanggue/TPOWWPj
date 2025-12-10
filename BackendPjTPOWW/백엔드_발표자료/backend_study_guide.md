# 백엔드 발표 준비 학습 자료

## 📚 목차
1. [백엔드 아키텍처 패턴](#1-백엔드-아키텍처-패턴)
2. [API 연동 기술](#2-api-연동-기술)
3. [외부 API 연동 실습](#3-외부-api-연동-실습)
4. [세션 관리와 보안](#4-세션-관리와-보안)
5. [데이터베이스 패턴](#5-데이터베이스-패턴)
6. [예상 질문 및 답변](#6-예상-질문-및-답변)

---

## 1. 백엔드 아키텍처 패턴

### 1.1 MVC 패턴 (Model-View-Controller)

**개념**: 애플리케이션을 세 가지 역할로 분리하여 관리하는 디자인 패턴

- **Model**: 데이터와 비즈니스 로직 처리 (DAO, DTO, Service)
- **View**: 사용자 인터페이스 (JSP)
- **Controller**: 사용자 요청 처리 및 흐름 제어 (Servlet)

**TPOWWPj 프로젝트 적용 예시**:
```
Controller: TpoRecommendServlet.java
    ↓
Service: TpoService.java
    ↓
DAO: TpoHistoryDAO.java, UserDAO.java
    ↓
Model: TpoHistoryDTO.java, UserDTO.java
    ↓
View: tpoResult.jsp
```

### 1.2 계층화 구조 (Layered Architecture)

**3계층 구조**:
1. **Presentation Layer**: Servlet, JSP (사용자 인터페이스)
2. **Business Layer**: Service 클래스 (비즈니스 로직)
3. **Data Layer**: DAO, DTO (데이터 접근)

**장점**:
- 각 계층의 독립적인 개발 및 유지보수
- 테스트 용이성
- 코드 재사용성

---

## 2. API 연동 기술

### 2.1 REST API 기본 개념

**REST (Representational State Transfer)**:
- HTTP 프로토콜을 활용한 웹 서비스 아키텍처
- HTTP 메서드 (GET, POST, PUT, DELETE) 사용
- JSON, XML 등의 데이터 형식

### 2.2 HttpURLConnection을 이용한 API 호출

**기본 패턴**:
```java
// 1. URL 생성
URL url = java.net.URI.create(apiUrl).toURL();

// 2. 연결 열기
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");
conn.setRequestProperty("Content-type", "application/json");

// 3. 응답 읽기
BufferedReader rd = new BufferedReader(
    new InputStreamReader(conn.getInputStream(), "UTF-8"));
StringBuilder sb = new StringBuilder();
String line;
while ((line = rd.readLine()) != null) {
    sb.append(line);
}

// 4. 연결 종료
rd.close();
conn.disconnect();
```

### 2.3 JSON 파싱

**Gson 라이브러리 사용**:
```java
Gson gson = new Gson();

// JSON → Java Object
JsonObject json = gson.fromJson(jsonString, JsonObject.class);

// Java Object → JSON
String jsonStr = gson.toJson(object);
```

**정규표현식을 이용한 파싱** (간단한 경우):
```java
String pattern = "\\{[^}]*\"category\"\\s*:\\s*\"" + category 
    + \"[^}]*\"obsrValue\"\\s*:\\s*\"([^\"]+)\"[^}]*}";
Matcher m = Pattern.compile(pattern).matcher(json);
return m.find() ? m.group(1) : "-";
```

---

## 3. 외부 API 연동 실습

### 3.1 기상청 날씨 API

**TPOWWPj에서 사용한 3가지 API**:
1. **초단기실황조회**: 현재 날씨 (기온, 습도, 강수량 등)
2. **단기예보조회**: 3일 이내 예보 (기온, 하늘상태, 강수확률 등)
3. **중기예보조회**: 3~10일 후 예보 (기온, 날씨 상태)

**API 호출 URL 생성 예시**:
```java
private String buildUrl(String apiType, String baseDate, 
                       String baseTime, String nx, String ny) {
    StringBuilder urlBuilder = new StringBuilder(
        "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" 
        + apiType);
    
    urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") 
        + "=" + API_KEY);
    urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
    urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=1000");
    urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
    urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") 
        + "=" + URLEncoder.encode(baseDate, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") 
        + "=" + URLEncoder.encode(baseTime, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") 
        + "=" + URLEncoder.encode(nx, "UTF-8"));
    urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") 
        + "=" + URLEncoder.encode(ny, "UTF-8"));
    
    return urlBuilder.toString();
}
```

**핵심 포인트**:
- `URLEncoder.encode()`로 한글 및 특수문자 인코딩
- 발표 시각에 맞춰 적절한 `base_time` 계산
- 격자 좌표(nx, ny) 매핑

### 3.2 Google Gemini AI API

**JSON 요청 생성**:
```java
JsonObject root = new JsonObject();
JsonArray contents = new JsonArray();
JsonObject content = new JsonObject();
JsonArray parts = new JsonArray();
JsonObject partText = new JsonObject();

partText.addProperty("text", prompt);
parts.add(partText);
content.add("parts", parts);
contents.add(content);
root.add("contents", contents);

String jsonRequest = gson.toJson(root);
```

**POST 요청 전송**:
```java
URL url = new URL(ENDPOINT + "?key=" + apiKey);
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("POST");
conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
conn.setDoOutput(true);

try (OutputStream os = conn.getOutputStream()) {
    os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
}
```

**응답 파싱**:
```java
JsonObject json = gson.fromJson(response, JsonObject.class);
JsonArray candidates = json.getAsJsonArray("candidates");
JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
JsonObject contentObj = firstCandidate.getAsJsonObject("content");
JsonArray respParts = contentObj.getAsJsonArray("parts");
JsonObject firstPart = respParts.get(0).getAsJsonObject();
String aiText = firstPart.get("text").getAsString().trim();
```

---

## 4. 세션 관리와 보안

### 4.1 세션 기반 인증

**로그인 처리**:
```java
HttpSession session = request.getSession();
UserDTO udto = userDAO.login(id, pw);

if (udto != null) {
    session.setAttribute("udto", udto);
    response.sendRedirect("main.jsp");
} else {
    response.sendRedirect("login.jsp?error=1");
}
```

**접근 제어**:
```java
UserDTO sessionUser = (UserDTO) session.getAttribute("udto");
if (sessionUser == null) {
    response.sendRedirect(request.getContextPath() + "/index.jsp");
    return;
}
```

### 4.2 API 키 관리

**환경변수 활용**:
```java
public class AppConfig {
    public static String getGeminiApikey() {
        // 1. 환경변수에서 읽기
        String key = System.getenv("GEMINI_API_KEY");
        
        // 2. 시스템 프로퍼티에서 읽기
        if (key == null) {
            key = System.getProperty("gemini.api.key");
        }
        
        return key;
    }
}
```

**보안 모범 사례**:
- API 키를 코드에 하드코딩하지 않기
- `.gitignore`에 환경설정 파일 추가
- 서버 환경변수로 관리

---

## 5. 데이터베이스 패턴

### 5.1 DAO 패턴 (Data Access Object)

**개념**: 데이터베이스 접근 로직을 별도의 클래스로 분리

**예시 - TpoHistoryDAO**:
```java
public class TpoHistoryDAO {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    // 히스토리 저장
    public void insertHistory(TpoHistoryDTO dto) {
        try {
            conn = DBManager.getConnection();
            String sql = "INSERT INTO tpo_history (...) VALUES (...)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getUn());
            ps.setString(2, dto.getRequestDate());
            // ...
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.close(conn, ps, rs);
        }
    }
}
```

### 5.2 PreparedStatement 사용

**SQL Injection 방지**:
```java
// ❌ 취약한 코드
String sql = "SELECT * FROM user WHERE id = '" + id + "'";

// ✅ 안전한 코드
String sql = "SELECT * FROM user WHERE id = ?";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1, id);
```

---

## 6. 예상 질문 및 답변

### Q1. MVC 패턴을 왜 사용하나요?

**답변**: MVC 패턴을 사용하면 역할을 명확히 분리할 수 있어 유지보수가 쉽고, 여러 개발자가 동시에 작업할 수 있습니다. 예를 들어 저희 프로젝트에서는 Servlet(Controller)이 사용자 요청을 받아 Service 계층에서 비즈니스 로직을 처리하고, DAO를 통해 데이터베이스에 접근한 후 JSP(View)로 결과를 보여줍니다.

### Q2. 기상청 API의 좌표는 어떻게 매핑했나요?

**답변**: 기상청 API는 격자 좌표(nx, ny)를 사용하는데, 저희는 `LocationCoord` 클래스에 전국 시/군/구별 좌표를 HashMap으로 매핑해두었습니다. 사용자가 회원가입 시 선택한 지역 정보를 키로 사용하여 해당 좌표를 조회합니다. 약 250개 이상의 시군구 좌표를 매핑했습니다.

### Q3. AI 추천은 어떻게 구현했나요?

**답변**: Google Gemini API를 사용했습니다. 사용자의 TPO(시간, 장소, 활동), 날씨 정보, 선호도를 하나의 프롬프트로 구성하여 AI에게 전송합니다. AI는 이 정보를 바탕으로 모자, 상의, 하의, 신발 등 항목별 코디를 추천하며, 추천 이유도 함께 제공합니다.

### Q4. 단기예보와 중기예보의 차이는?

**답변**: 단기예보는 3일 이내의 상세한 시간대별 날씨를 제공하며, 중기예보는 3~10일 후의 날씨를 제공합니다. 단, 중기예보는 8일 이후부터는 기온 정보만 제공되고 날씨 상태와 강수확률은 제공되지 않습니다. 저희는 사용자가 선택한 날짜를 기준으로 자동으로 적절한 API를 선택하도록 구현했습니다.

### Q5. 세션을 왜 사용하나요?

**답변**: 세션을 사용하면 HTTP의 무상태(Stateless) 특성을 보완할 수 있습니다. 저희 프로젝트에서는 로그인 정보, 날씨 데이터, 예보 정보 등을 세션에 저장하여 페이지 이동 시에도 데이터를 유지하고, 불필요한 API 재호출을 방지합니다.

### Q6. JSON 파싱은 어떻게 했나요?

**답변**: 주로 Gson 라이브러리를 사용했지만, 간단한 값 추출의 경우 정규표현식을 사용하기도 했습니다. Gson은 복잡한 구조의 JSON을 Java 객체로 변환할 때 유용하고, 정규표현식은 특정 키의 값만 빠르게 추출할 때 효율적입니다.

### Q7. API 오류는 어떻게 처리했나요?

**답변**: try-catch 블록으로 예외를 처리하고, 사용자에게는 "-" 또는 기본값을 표시하며, 콘솔에는 상세한 에러 로그를 출력합니다. 또한 세션에 `weatherError` 같은 속성을 설정하여 JSP에서 사용자 친화적인 에러 메시지를 표시합니다.

### Q8. 데이터베이스 연결은 어떻게 관리했나요?

**답변**: DBManager 클래스를 만들어 Connection Pool을 관리합니다. `getConnection()`으로 연결을 가져오고, 작업 완료 후 반드시 `close()`로 자원을 반환하여 메모리 누수를 방지합니다. finally 블록에서 close를 호출하여 예외 발생 시에도 반드시 실행되도록 했습니다.

---

## 🎯 발표 팁

1. **코드보다 개념 우선**: 코드 전체를 설명하기보다는 핵심 개념과 흐름을 설명
2. **실제 예시 활용**: "예를 들어 사용자가 서울 강남구를 선택하면..."
3. **문제 해결 과정 강조**: "처음에는 이런 문제가 있었는데, 이렇게 해결했습니다"
4. **숫자 활용**: "250개 이상의 좌표 매핑", "3가지 API 연동"
5. **시각 자료**: 아키텍처 다이어그램, API 호출 흐름도 준비

---

## 📌 핵심 용어 정리

- **MVC**: Model-View-Controller 디자인 패턴
- **DAO**: Data Access Object, 데이터베이스 접근 계층
- **DTO**: Data Transfer Object, 데이터 전송 객체
- **REST API**: HTTP 프로토콜 기반 웹 서비스
- **JSON**: JavaScript Object Notation, 데이터 교환 형식
- **Session**: 사용자별 상태 정보 저장
- **PreparedStatement**: SQL Injection 방지를 위한 안전한 SQL 실행
- **Connection Pool**: 데이터베이스 연결 재사용을 위한 기법
