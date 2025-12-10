package com.dongyang.TPOWW.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                System.out.println("[AppConfig] config.properties 파일 로드 성공");
            } else {
                System.out.println("[AppConfig] config.properties 파일을 찾을 수 없습니다. 환경변수를 사용합니다.");
            }
        } catch (IOException ex) {
            System.err.println("[AppConfig] config.properties 로드 실패: " + ex.getMessage());
        }
    }

    public static String getGeminiApikey() {
        // 1순위: 환경변수
        String key = System.getenv("GEMINI_API_KEY");
        if (key != null && !key.isBlank()) {
            System.out.println("[AppConfig] 환경변수에서 API 키 로드");
            return key;
        }

        // 2순위: 시스템 프로퍼티
        key = System.getProperty("GEMINI_API_KEY");
        if (key != null && !key.isBlank()) {
            System.out.println("[AppConfig] 시스템 프로퍼티에서 API 키 로드");
            return key;
        }

        // 3순위: config.properties 파일
        key = properties.getProperty("gemini.api.key");
        if (key != null && !key.isBlank() && !"YOUR_API_KEY_HERE".equals(key)) {
            System.out.println("[AppConfig] config.properties 파일에서 API 키 로드");
            return key;
        }

        throw new IllegalStateException(
                "GEMINI_API_KEY가 설정되지 않았습니다!\n" +
                        "다음 중 하나의 방법으로 API 키를 설정하세요:\n" +
                        "1. 환경변수: GEMINI_API_KEY\n" +
                        "2. 시스템 프로퍼티: -DGEMINI_API_KEY=...\n" +
                        "3. config.properties 파일 (src/main/resources/config.properties)");
    }

    private AppConfig() {
    } // 유틸 클래스이므로 인스턴스화 방지
}
