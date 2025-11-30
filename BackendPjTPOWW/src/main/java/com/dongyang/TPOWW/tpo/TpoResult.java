package com.dongyang.TPOWW.tpo;

public class TpoResult {
	 private String weatherSummary; // 날씨 요약 문장
	 private String aiRecommend;    // Gemini가 준 코디 추천 텍스트
	 private String prompt;         // 디버깅/로그용 (사용자에겐 선택적으로 보여줌)
	 public String getWeatherSummary() {
		 return weatherSummary;
	 }
	 public void setWeatherSummary(String weatherSummary) {
		 this.weatherSummary = weatherSummary;
	 }
	 public String getAiRecommend() {
		 return aiRecommend;
	 }
	 public void setAiRecommend(String aiRecommend) {
		 this.aiRecommend = aiRecommend;
	 }
	 public String getPrompt() {
		 return prompt;
	 }
	 public void setPrompt(String prompt) {
		 this.prompt = prompt;
	 }
	 
}
