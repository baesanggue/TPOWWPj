package com.dongyang.TPOWW.util;

public class AppConfig {
	public static String getGeminiApikey() {
		 // 1순위: 환경변수 (리눅스/윈도우, IntelliJ/Eclipse run config에서 설정)
        String key = System.getenv("GEMINI_API_KEY");
        
        // 2순위: JVM 옵션 -DGEMINI_API_KEY=...
        if (key == null || key.isBlank()) {
            key = System.getProperty("GEMINI_API_KEY");
        }
        // 3순위: (선택) 없으면 null 던져버리기
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY가 설정되어 있지 않습니다.");
        }

        return key;
	}
	
	private AppConfig() {} // 유틸 클래스이므로 인스턴스화 방지
}
