package com.dongyang.TPOWW.weather;

public interface WeatherService {
	/**
     * 특정 날짜/시간, 시/도, 시/군/구 기준으로
     * 기상청 API들을 이용해 날씨 요약 문장을 리턴한다.
     *
     * 예: "맑고 낮 최고 25도, 최저 15도, 강수확률 10%"
     */
    String getWeatherSummary(
            String date,  // yyyy-MM-dd
            String time,  // HH:mm (필요 없으면 null 허용)
            String region,
            String sigungu
    );
}
