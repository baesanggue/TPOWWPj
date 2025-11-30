package com.dongyang.TPOWW.weather;

public class KmaWeatherService implements WeatherService {
	 @Override
	    public String getWeatherSummary(String date, String time, String region, String sigungu) {
	        // TODO: 팀원 구현부 - 기상청 API 연동
	        // 1. region + sigungu -> 위도/경도 or 격자 좌표 변환
	        // 2. 단기/초단기/중기 예보 중에서 date/time에 맞는 값 선택
	        // 3. 예: "맑음, 최고 24도, 최저 16도, 강수확률 10%"
	        return "날씨 데이터 준비 중 (임시): " + date + " " + region + " " + sigungu;
	    }
}
