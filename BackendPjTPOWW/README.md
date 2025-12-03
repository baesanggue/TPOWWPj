# TPOWWPj

백엔드 기말 프로젝트:  TPOWWPj (날씨에 맞추어 TPO 복장 AI 추천 사이트)

## aiApi branch
-- 작업 목록 --
## 2025-11-30 작업 내용 정리

### 1. AI 코디 추천 구조 설계 및 구현 (Gemini + 날씨 + TPO)

- 객체지향 / 계층 구조를 고려하여 백엔드 구조화
  - `com.dongyang.TPOWW.util.AppConfig`
    - Gemini API 키를 코드에 하드코딩하지 않고, 환경변수/시스템 프로퍼티에서 읽어오는 설정 유틸 클래스 구현
  - `com.dongyang.TPOWW.ai.LlmClient`
    - LLM(대형 언어모델) 연동 인터페이스 정의 (`generate(String prompt)` 메서드)
  - `com.dongyang.TPOWW.ai.GeminiClient`
    - `LlmClient` 구현체로 Google Gemini API 호출 구현
    - `HttpURLConnection` + `Gson`을 사용하여 JSON 요청/응답 처리
    - 오류 상태코드에 대한 예외 처리 추가
  - `com.dongyang.TPOWW.weather.WeatherService`
    - 날씨 정보를 가져오기 위한 인터페이스 정의
  - `com.dongyang.TPOWW.weather.KmaWeatherService`
    - 기상청(KMA) API 연동을 위한 스텁 구현
    - 현재는 `"날씨 데이터 준비 중: ..."` 형식의 임시 문자열을 반환하도록 구현
  - `com.dongyang.TPOWW.tpo.TpoRequest`
    - TPO(언제/어디서/무엇을) + 유저 선호 정보(브랜드, 색상, 퍼스널 컬러)를 담는 요청 DTO
  - `com.dongyang.TPOWW.tpo.TpoResult`
    - 날씨 요약 + AI 추천 결과 + 프롬프트를 담는 결과 DTO
  - `com.dongyang.TPOWW.tpo.TpoService`
    - `WeatherService`와 `LlmClient`를 조합하여 실제 코디 추천을 수행하는 서비스 레이어
    - TPO + 날씨 + 선호 정보를 기반으로 Gemini에 보낼 프롬프트 생성 로직 작성

- TPO 추천 서블릿 구현
  - `com.dongyang.TPOWW.controller.TpoRecommendServlet`
    - `init()`에서 `WeatherService(KmaWeatherService)`와 `LlmClient(GeminiClient)`를 주입하여 `TpoService` 생성
    - `doPost()`에서 `HttpServletRequest`를 `TpoRequest`로 변환 후 `TpoService.recommend()` 호출
    - `TpoResult`를 `tpoResult.jsp`로 전달하여 화면에 추천 결과 출력

- JSP 연동
  - `/tpo/tpo.jsp`
    - 로그인 사용자 정보를 기반으로 TPO + 선호 정보 입력 폼 구성
    - `action="tpoRecommend.do"` 경로 수정 및 파라미터 이름 정리
  - `/tpo/tpoResult.jsp`
    - AI 코디 추천 결과 및 날씨 요약 출력
    - 디버깅을 위해 실제 프롬프트를 `<details>`로 접어서 확인할 수 있도록 구현 (추후 배포 시 제거 예정)

---

### 2. 보안 / 설정 관련 작업

- `AppConfig`를 통해 API 키를 환경변수/시스템 프로퍼티에서 읽어오도록 구현
  - 코드에 직접 키를 하드코딩하지 않고, 서버/개발 환경 설정에서 관리
- `GeminiClient`에서 상태코드가 200~299가 아닐 경우 `IOException`을 던지도록 처리하여
  - 외부 API 오류 시 사용자에게는 일반적인 오류 메시지,
  - 내부 로그에는 예외 스택을 남길 수 있도록 구조화

---

### 3. 경로 / JSTL 관련 수정

- `member/regist.jsp`에서 `form action`이 잘못된 부분을 JSTL `<c:url>` + 태그라이브러리 추가로 해결
  - 상단에 JSTL 태그라이브러리 선언 추가:
    - `<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>`
  - `action`을 절대경로로 수정:
    - `action="<c:url value='/regist.do' />"`
- 그 외 JSP 파일에서 상대경로/절대경로 혼동으로 인한 404 문제를 정리할 계획 수립
  - `nav.jsp`의 회원가입 링크
  - `mypage.jsp`, `admin.jsp`, `adminEdit.jsp`, `tpo.jsp`, `tpoResult.jsp` 등

---

### 4. 기능 점검 및 발견된 이슈 정리 (추후 수정 예정)

- 로그인
  - 회원가입 없이도 로그인되는 문제 → LoginServlet / UserDAO의 인증 로직 점검 필요 (수정완료)
- 마이페이지
  - 로그인 후 마이페이지에서 값 입력 후 수정해도 DB에 반영되지 않는 문제 → MyPageServlet, UserDAO, UserPrefDAO 수정 필요 (수정완료)
- 관리자
  - 관리자 페이지에서 메인으로 돌아가는 링크 없음 (수정완료)
  - 관리자 화면에서 role 수정이 실제 DB에 반영되지 않음 (수정완료)
  - 관리자가 자기 자신을 삭제할 수 있는 문제 (수정완료)
  - 관리자가 다른 관리자의 role을 수정하거나, 자신의 role을 수정할수 있는 문제 (수정완료)
- 마이페이지 UI
  - 특별시/도 및 시/군/구 수정 부분을 회원가입과 동일한 드롭다운 + JS 방식으로 변경 예정
- TPO 결과 화면
  - 디버깅용 프롬프트 출력 부분은 개발 중에는 유지, 제출/배포 시에는 숨길 계획
  - TPO 결과 화면에서 날씨가 입력한 날짜의 날씨가 아닌 현재 날씨가 출력됨
- 메인페이지
  - 단기예보 날씨가 하루치 기온, 습도만 여러개 출력하고 있는 문제 -> 오늘을 포함한 후의 4일까지의 날씨 기온, 습도를 출력해야함
  - 단기 예보를 중기예보로 변경


-- 작업 필요 목록 --
 로그인 로직: DB 인증 필수로 변경 (LoginServlet, UserDAO)

 마이페이지: GET/POST 분리 및 updateUser/updateUserPref 연동 (MyPageServlet, UserDAO, UserPrefDAO)

 비로그인 사용자의 /mypage.do, /tpo.do, /tpoRecommend.do 접근 차단

 관리자 페이지: 메인으로 이동 링크 추가 (admin.jsp)

 관리자 role 수정 기능 정상화 (AdminEditServlet, UserDAO UPDATE 쿼리)

 관리자 계정 자기 자신 삭제 방지 로직 추가 (AdminDeleteServlet)

 마이페이지에서 지역/시군구도 regist.jsp와 동일한 드롭다운 + JS 공통화

 tpoResult.jsp의 디버깅용 프롬프트 출력은 개발용 / 배포 시 숨김 처리

 공통: 세션 null 체크 및 권한 제어 패턴 통일
=======

**백엔드 기말 프로젝트:** TPOWWPj (날씨에 맞추어 TPO 복장 AI 추천 사이트)

**작성일:** 2025-11-30

---

## 프로젝트 개요
TPOWWPj는 **날씨 정보를 기반으로 사용자의 복장을 추천**해주는 웹 서비스입니다.  
현재 날씨와 단기예보 정보를 제공하며, 기온과 날씨 상태에 맞추어 복장 AI 추천 기능을 구현하는 것이 핵심 목표입니다.

---

## 주요 기능

### 1. 현재 날씨 조회
- 초단기실황조회 API 사용 (기온, 습도, 강수량, 바람)  
- JSP 페이지에서 실시간 출력  
- 요일 표시 포함

### 2. 단기예보 조회
- 단기예보조회 API 사용 (TMP, REH)  
- 오전/오후 기온 출력  
- 5시 발표 기준, 17시 발표까지 확장하여 모든 시간대 데이터 확보  
- 각 날짜별 요일 표시  

### 3. 데이터 처리 및 안전장치
- null 값 발생 시 안전하게 처리하여 화면에 항상 값 출력  
- 최대 6일치 예보 표시  
- 오전/오후 TMP 데이터가 누락되는 경우를 최소화  

---

## 사용 기술
- Java, JSP, Servlet  
- HttpURLConnection을 통한 REST API 호출  
- Session을 이용한 데이터 유지  
- LinkedHashMap과 List를 이용한 데이터 가공 및 정렬  

---

## 구현 포인트
1. **초단기실황조회**
   - `t1h`, `reh`, `rn1`, `wsd` 항목 추출  
   - null 또는 오류 발생 시 “오류 발생” 표시

2. **단기예보조회**
   - 오전/오후 TMP 분리 출력  
   - 05시 발표와 17시 발표를 합쳐서 데이터 확장  
   - 요일 계산 및 표시  

3. **JSP 페이지**
   - `main.jsp`에서 현재 날씨와 단기예보 출력  
   - 날짜별 요일 표시, 오전/오후 TMP 출력  
   - null 값 처리 후 `-` 대신 항상 값 표시

---

## 예시 화면
- 현재날씨: 기온, 습도, 강수량, 바람, 오늘 요일  
- 단기예보: 날짜(요일), 오전 TMP, 오후 TMP

---

## 향후 개선점
- 상대 날짜 표시 (오늘/내일/모레)  
- 복장 추천 AI 기능과 연동  
- UX 개선 및 예외처리 강화

