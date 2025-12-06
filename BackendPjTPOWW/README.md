# TPOWWPj

백엔드 기말 프로젝트:  TPOWWPj (날씨에 맞추어 TPO 복장 AI 추천 사이트)

## aitest branch
-- 작업 목록 -----------------------------------------------------------------

## 🚀 업데이트 내역 (2025.12.06)

### 1️⃣ UI/UX 개선
* **AI 추천 텍스트 정렬:** `cartoon_theme.css`에서 `.result-box` 스타일 추가하여 AI 추천 내용이 왼쪽 정렬되도록 개선.
* **버튼 높이 통일:** `main.jsp`의 마이페이지 버튼을 `btn-navigation` 클래스로 변경하고 인라인 스타일 제거하여 모든 버튼의 높이를 일관되게 통일.
* **선택 이유 표시:** `tpoResult.jsp`에 "선택 이유 요약" 섹션 추가하여 AI가 해당 코디를 추천한 이유를 명확하게 표시.

### 2️⃣ 날씨 기능 고도화
* **동적 날씨 이모지:**
    * `main.jsp`에서 강수형태(PTY), 하늘상태(SKY), 시간대를 고려한 정확한 날씨 이모지 표시 구현.
    * 비🌧️, 눈❄️, 맑음(낮☀️/밤🌙), 구름많음⛅, 흐림☁️ 등 다양한 상태 반영.
* **SKY 값 세션 저장:** `WeatherServlet.java`에서 초단기예보 API를 통해 SKY(하늘상태) 값을 세션에 저장하여 날씨 이모지가 정확하게 표시되도록 개선.
* **강수확률(POP) 표시:**
    * 단기예보 API에서 강수확률(POP) 데이터 추출 (`WeatherServlet.java`).
    * `main.jsp`에서 비/눈이 올 때 강수확률을 함께 표시 (예: "비 (60%)").
    * 맑을 때는 "강수확률 30%" 형태로 표시.
* **PTY 대소문자 통일:** `main.jsp`와 `WeatherServlet.java`에서 PTY 세션 속성명을 대문자로 통일하여 일관성 확보.

### 3️⃣ AI 추천 기능 개선
* **사용자 입력 날짜의 날씨 반영:**
    * `TpoRecommendServlet.java`에서 현재 날씨 대신 사용자가 선택한 날짜의 단기예보 데이터를 사용하도록 개선.
    * 3일 이내: 단기예보에서 입력 날짜의 최저/최고 기온 추출 및 표시.
    * 3일 이후: 중기예보 데이터 활용.
* **AI 응답 파싱 디버깅:** `TpoService.java`에 AI 응답 파싱 로직 디버그 출력 추가하여 `[이유]...[/이유]` 태그 검증 가능.

### 4️⃣ TPO 결과 페이지 UI 전면 개선
* **히스토리 상세보기 스타일 적용:**
    * `TpoResult.java`에 날짜, 시간, 활동, 장소 필드 추가.
    * `TpoRecommendServlet.java`에서 result 객체에 사용자 입력 정보 설정.
    * `custom_grid.css`에 `.detail-section` 스타일 추가.
    * `tpoResult.jsp`를 `historyDetail.jsp`와 동일한 깔끔한 박스 스타일로 완전 재구성.
* **섹션별 명확한 구분:**
    * 📅 날짜 및 시간
    * 📍 활동 내용
    * 🌤️ 날씨 요약
    * 💡 추천 이유 (노란색 배경으로 강조)
    * 🤖 AI 추천 코디



### 1️⃣ UI/UX 개선 및 안정화
* **드롭다운 방향 수정:** `mypage.jsp` 등에서 드롭다운이 위로 열리는 브라우저 기본 동작을 존중하되, 화면 울렁거림을 유발하는 과도한 스크롤 및 여백(Spacer) 제거하여 사용자 경험 개선.
* **날짜 선택 편의성:** `tpo.jsp`에서 날짜 입력창 전체 클릭 시 달력이 열리도록 개선 (`onclick="this.showPicker()"`).
* **TPO 결과 화면 정렬:**
    * 버튼 크기 불일치 문제 해결 (`flex: 1 1 0` 적용).
    * AI 추천 텍스트가 중앙 정렬되는 문제 해결 (CSS 클래스 분리 및 `text-align: left !important` 강제 적용).

### 2️⃣ 코드 리팩토링 및 최적화
* **유지보수성 강화:** 인라인 스타일을 `cartoon_theme.css`로 분리하여 코드 가독성 및 재사용성 향상.
* **불필요한 코드 정리:** `WeatherServlet.java` 등에서 사용하지 않는 import 제거 및 코드 정리.
* **안정성 확보:** UI 수정 과정에서 발생할 수 있는 기능 유실(회원 정보 수정, 히스토리 등)이 없도록 전수 검사 완료.

## 🚀 업데이트 내역 (2025.12.03 기준)

기존 기능에 **사용자 맞춤형 지역 날씨 연동**과 **AI 코디 추천 정교화** 작업을 추가하였습니다.

### 1️⃣ 날씨 API 고도화 (Weather)
* **기존:** 서울특별시(60, 127) 좌표로 고정되어 모든 사용자에게 동일한 날씨 정보 제공.
* **변경:**
    * **지역 좌표 데이터베이스화:** 전국 시/군/구의 기상청 격자 좌표(X, Y)를 매핑한 `LocationCoord` 클래스 구현.
    * **사용자 맞춤형 날씨:**
        * **비로그인 시:** 기본값(서울) 날씨 표시.
        * **로그인 시:** 회원가입 시 등록한 **지역(Region) 및 시군구(Sigungu)** 정보를 기반으로 해당 지역의 날씨를 자동으로 조회하여 표시.
    * **세션 연동:** 날씨 정보를 `Session`에 저장하여 메인 화면뿐만 아니라 AI 추천 기능에서도 동일한 날씨 데이터를 활용하도록 개선.

### 2️⃣ AI 코디 추천 기능 강화 (TPO Recommendation)
* **기존:** 단순한 사용자 입력(성별, 나이 등)만으로 추천.
* **변경:**
    * **날씨 정보 반영:** `TpoRecommendServlet`에서 현재 날씨(기온, 습도 등)와 단기 예보 데이터를 **프롬프트(Prompt)**에 포함하여 AI에게 전송.
    * **상황별 추천:** 사용자가 입력한 날짜와 시간의 **예보 데이터**를 우선적으로 분석하여, "비 오는 날", "일교차가 큰 날" 등 구체적인 상황에 맞는 옷차림 추천.

### 3️⃣ 회원 관리 및 데이터베이스 (Member & DB)
* **DB 구조 변경:** `user` 테이블에 세부 지역 정보를 저장하기 위한 `sigungu` 컬럼 추가.
* **회원가입 프로세스:** 회원가입 시 시/도뿐만 아니라 **시/군/구**까지 선택하도록 UI 및 로직(`UserDAO`, `UserDTO`) 개선.
* **로그인 프로세스:** 로그인 성공 시 즉시 `WeatherServlet`을 호출하여, 사용자 지역 기반으로 날씨 정보를 최신화한 후 메인 화면으로 이동하도록 플로우 수정.

-----------------------------------------------------------------------------
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
  - 회원가입 없이도 로그인되는 문제 → LoginServlet / UserDAO의 인증 로직 점검 필요
- 마이페이지
  - 로그인 후 마이페이지에서 값 입력 후 수정해도 DB에 반영되지 않는 문제 → MyPageServlet, UserDAO, UserPrefDAO 수정 필요
- 관리자
  - 관리자 페이지에서 메인으로 돌아가는 링크 없음
  - 관리자 화면에서 role 수정이 실제 DB에 반영되지 않음
  - 관리자가 자기 자신을 삭제할 수 있는 문제
- 마이페이지 UI
  - 특별시/도 및 시/군/구 수정 부분을 회원가입과 동일한 드롭다운 + JS 방식으로 변경 예정
- TPO 결과 화면
  - 디버깅용 프롬프트 출력 부분은 개발 중에는 유지, 제출/배포 시에는 숨길 계획


## ✅ 최종 점검 완료 (2025.12.04)
이전에 "작업 필요 목록"으로 분류되었던 모든 항목이 구현 완료되었습니다.

### 1️⃣ 보안 및 인증 (Security & Auth)
- [x] **로그인 로직 강화:** DB 기반 ID/PW 인증 필수 적용 완료 (`LoginServlet`, `UserDAO`).
- [x] **접근 제어 (Access Control):** 비로그인 사용자의 주요 페이지(`/mypage.do`, `/tpo.do`, `/tpoRecommend.do`) 접근 차단 및 리다이렉트 처리 완료.
- [x] **세션 관리:** 전반적인 세션 `null` 체크 및 권한 제어 패턴 통일.

### 2️⃣ 마이페이지 (My Page)
- [x] **데이터 처리 구조 개선:** `GET`(조회)과 `POST`(수정) 로직 분리 및 `updateUser`/`updateUserPref` 연동 완료.
- [x] **UI/UX 개선:** 지역/시군구 선택 기능을 `regist.jsp`와 동일한 드롭다운 + JS(`region.js`) 방식으로 통일하여 사용자 편의성 증대.

### 3️⃣ 관리자 기능 (Admin)
- [x] **편의성:** 관리자 페이지에서 메인 화면으로 이동하는 링크 추가 (`admin.jsp`).
- [x] **권한 관리:** 관리자 페이지에서 회원 Role 수정 기능 정상화 (`AdminEditServlet`).
- [x] **안전장치:** 관리자 본인 삭제 방지 및 다른 관리자 삭제 방지 로직 추가 (`AdminDeleteServlet`).

### 4️⃣ 기타 (Others)
- [x] **배포 준비:** `tpoResult.jsp`의 디버깅용 프롬프트 출력 숨김 처리 완료.

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

