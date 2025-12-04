package com.dongyang.TPOWW.tpo;

import java.io.IOException;

import com.dongyang.TPOWW.ai.LlmClient;
import com.dongyang.TPOWW.weather.WeatherService;

public class TpoService {

    private final WeatherService weatherService;
    private final LlmClient llmClient;

    public TpoService(WeatherService weatherService, LlmClient llmClient) {
        this.weatherService = weatherService;
        this.llmClient = llmClient;
    }

    public TpoResult recommend(TpoRequest req) throws IOException {
        // [수정] 서블릿에서 만들어준 날씨 문자열을 그대로 가져옵니다.
        String weatherSummary = req.getWeatherInfo();
        if (weatherSummary == null || weatherSummary.isEmpty()) {
            weatherSummary = "날씨 정보 없음";
        }

        // 2. 프롬프트 생성 (날씨 정보 포함)
        String prompt = buildPrompt(req, weatherSummary);

        // 3. AI(LLM) 호출
        String aiText = llmClient.generate(prompt);

        // 4. 결과 포장
        TpoResult result = new TpoResult();
        result.setWeatherSummary(weatherSummary);
        result.setAiRecommend(aiText);
        result.setPrompt(prompt);

        return result;
    }

    private String buildPrompt(TpoRequest r, String weatherSummary) {
        StringBuilder sb = new StringBuilder();

        sb.append("너는 한국 사용자를 위한 패션 코디 전문 AI 스타일리스트야.\n");
        sb.append("아래 조건과 날씨에 맞는 코디를 추천해줘.\n\n");

        sb.append("## 기본 정보\n");
        sb.append("- 성별: ").append(nullTo(r.getGender(), "미상")).append("\n");
        sb.append("- 나이: ").append(r.getAge() > 0 ? r.getAge() + "세" : "미상").append("\n");
        sb.append("- 날짜/시간: ").append(r.getDate()).append(" ").append(r.getTime()).append("\n");
        sb.append("- 활동(무엇을): ").append(nullTo(r.getWhat(), "활동 정보 없음")).append("\n");
        sb.append("- 장소(어디서): ").append(nullTo(r.getRegion(), "지역 미상"))
                .append(" ").append(nullTo(r.getSigungu(), "")).append("\n");

        sb.append("- 실내/야외: ");
        if (r.isIndoor() && r.isOutdoor()) {
            sb.append("실내와 야외를 모두 오갈 예정\n");
        } else if (r.isIndoor()) {
            sb.append("주로 실내 활동\n");
        } else if (r.isOutdoor()) {
            sb.append("주로 야외 활동\n");
        } else {
            sb.append("정보 없음 (실내/야외 모두 고려)\n");
        }

        sb.append("\n## 날씨 정보\n");
        sb.append(nullTo(weatherSummary, "날씨 정보 없음")).append("\n");

        sb.append("\n## 선호 정보\n");
        sb.append("- 선호 브랜드: ").append(emptyTo(r.getBrand(), "특이사항 없음")).append("\n");
        sb.append("- 선호 색상: ").append(emptyTo(r.getColor(), "특이사항 없음")).append("\n");
        sb.append("- 퍼스널 컬러: ").append(emptyTo(r.getPcolor(), "정보 없음")).append("\n");

        sb.append("\n## 요구사항\n");
        sb.append("날씨와 TPO에 맞춰 다음 항목별로 한국어로 **간결하게 핵심만** 추천해줘.\n");
        sb.append("긴 설명은 생략하고, 구체적인 아이템 위주로 짧게 답변해줘.\n\n");
        sb.append("- 모자:\n");
        sb.append("- 액세서리:\n");
        sb.append("- 상의:\n");
        sb.append("- 하의:\n");
        sb.append("- 양말:\n");
        sb.append("- 신발:\n");
        sb.append("- 기타(아우터, 가방 등):\n\n");
        sb.append("답변은 위 항목 이름과 콜론(:) 형식을 그대로 사용하고, 불필요한 서술은 하지 마.\n");
        sb.append("각 줄의 시작에 공백이나 들여쓰기를 절대 하지 마.");

        return sb.toString();
    }

    private String nullTo(String v, String alt) {
        return v == null ? alt : v;
    }

    private String emptyTo(String v, String alt) {
        if (v == null || v.trim().isEmpty())
            return alt;
        return v;
    }
}
