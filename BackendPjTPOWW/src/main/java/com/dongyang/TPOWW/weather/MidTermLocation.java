package com.dongyang.TPOWW.weather;

import java.util.HashMap;
import java.util.Map;

public class MidTermLocation {

    // 중기육상예보 구역 코드 (강수확률, 날씨)
    private static final Map<String, String> landCodeMap = new HashMap<>();

    // 중기기온예보 구역 코드 (최저/최고 기온)
    private static final Map<String, String> tempCodeMap = new HashMap<>();

    static {
        // 1. 서울/인천/경기도 -> 11B00000
        landCodeMap.put("Seoul", "11B00000");
        landCodeMap.put("Incheon", "11B00000");
        landCodeMap.put("Gyeonggi", "11B00000");

        // 2. 부산/울산/경남 -> 11H20000
        landCodeMap.put("Busan", "11H20000");
        landCodeMap.put("Ulsan", "11H20000");
        landCodeMap.put("Gyeongnam", "11H20000");

        // 3. 대구/경북 -> 11H10000
        landCodeMap.put("Daegu", "11H10000");
        landCodeMap.put("Gyeongbuk", "11H10000");

        // 4. 광주/전남 -> 11F20000
        landCodeMap.put("Gwangju", "11F20000");
        landCodeMap.put("Jeonnam", "11F20000");

        // 5. 전북 -> 11F10000
        landCodeMap.put("Jeonbuk", "11F10000");

        // 6. 대전/세종/충남 -> 11C20000
        landCodeMap.put("Daejeon", "11C20000");
        landCodeMap.put("Sejong", "11C20000");
        landCodeMap.put("Chungnam", "11C20000");

        // 7. 충북 -> 11C10000
        landCodeMap.put("Chungbuk", "11C10000");

        // 8. 강원 -> 11D10000 (영서), 11D20000 (영동) - 편의상 영서로 통일하거나 분기 필요하지만 일단 영서 기준
        landCodeMap.put("Gangwon", "11D10000");

        // 9. 제주 -> 11G00000
        landCodeMap.put("Jeju", "11G00000");

        // --- 기온 코드 (주요 도시 기준 매핑) ---
        tempCodeMap.put("Seoul", "11B10101"); // 서울
        tempCodeMap.put("Incheon", "11B20201"); // 인천
        tempCodeMap.put("Gyeonggi", "11B20601"); // 수원 (경기 대표)

        tempCodeMap.put("Busan", "11H20201"); // 부산
        tempCodeMap.put("Ulsan", "11H20101"); // 울산
        tempCodeMap.put("Gyeongnam", "11H20301"); // 창원 (경남 대표)

        tempCodeMap.put("Daegu", "11H10701"); // 대구
        tempCodeMap.put("Gyeongbuk", "11H10501"); // 안동 (경북 대표)

        tempCodeMap.put("Gwangju", "11F20501"); // 광주
        tempCodeMap.put("Jeonnam", "11F20601"); // 목포 (전남 대표)

        tempCodeMap.put("Jeonbuk", "11F10201"); // 전주

        tempCodeMap.put("Daejeon", "11C20401"); // 대전
        tempCodeMap.put("Sejong", "11C20404"); // 세종
        tempCodeMap.put("Chungnam", "11C20101"); // 천안 (충남 대표)

        tempCodeMap.put("Chungbuk", "11C10301"); // 청주

        tempCodeMap.put("Gangwon", "11D10301"); // 춘천 (강원 영서 대표)

        tempCodeMap.put("Jeju", "11G00201"); // 제주
    }

    public static String getLandCode(String region) {
        return landCodeMap.getOrDefault(region, "11B00000"); // 기본: 서울/경기
    }

    public static String getTempCode(String region) {
        return tempCodeMap.getOrDefault(region, "11B10101"); // 기본: 서울
    }
}
