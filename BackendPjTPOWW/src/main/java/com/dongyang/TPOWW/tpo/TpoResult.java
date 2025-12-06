package com.dongyang.TPOWW.tpo;

public class TpoResult {
	private String weatherSummary; // 날씨 요약 문장
	private String aiRecommend; // Gemini가 준 코디 추천 텍스트
	private String reasonSummary; // AI가 추천한 이유
	private String prompt; // 디버깅/로그용 (사용자에겐 선택적으로 보여줌)
	private String date; // 날짜
	private String time; // 시간
	private String activity; // 활동
	private String location; // 장소

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

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

	public String getReasonSummary() {
		return reasonSummary;
	}

	public void setReasonSummary(String reasonSummary) {
		this.reasonSummary = reasonSummary;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
