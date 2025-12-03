package com.dongyang.TPOWW.weather;

import java.util.HashMap;
import java.util.Map;

public class LocationCoord {

    public static class Point {
        public String x;
        public String y;

        public Point(String x, String y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final Map<String, Point> coordMap = new HashMap<>();

    static {
        // 1. 서울특별시 (Seoul)
        add("Seoul", "종로구", "60", "127");
        add("Seoul", "중구", "60", "127");
        add("Seoul", "용산구", "60", "126");
        add("Seoul", "성동구", "61", "127");
        add("Seoul", "광진구", "62", "126");
        add("Seoul", "동대문구", "61", "127");
        add("Seoul", "중랑구", "62", "128");
        add("Seoul", "성북구", "61", "127");
        add("Seoul", "강북구", "61", "128");
        add("Seoul", "도봉구", "61", "129");
        add("Seoul", "노원구", "61", "129");
        add("Seoul", "은평구", "59", "127");
        add("Seoul", "서대문구", "59", "127");
        add("Seoul", "마포구", "59", "127");
        add("Seoul", "양천구", "58", "126");
        add("Seoul", "강서구", "58", "126");
        add("Seoul", "구로구", "58", "125");
        add("Seoul", "금천구", "59", "124");
        add("Seoul", "영등포구", "58", "126");
        add("Seoul", "동작구", "59", "125");
        add("Seoul", "관악구", "59", "125");
        add("Seoul", "서초구", "61", "125");
        add("Seoul", "강남구", "61", "126");
        add("Seoul", "송파구", "62", "126");
        add("Seoul", "강동구", "62", "126");

        // 2. 부산광역시 (Busan)
        add("Busan", "중구", "97", "74");
        add("Busan", "서구", "97", "74");
        add("Busan", "동구", "98", "75");
        add("Busan", "영도구", "98", "74");
        add("Busan", "부산진구", "97", "75");
        add("Busan", "동래구", "98", "76");
        add("Busan", "남구", "98", "75");
        add("Busan", "북구", "96", "76");
        add("Busan", "해운대구", "99", "75");
        add("Busan", "사하구", "96", "74");
        add("Busan", "금정구", "98", "77");
        add("Busan", "강서구", "96", "76");
        add("Busan", "연제구", "98", "76");
        add("Busan", "수영구", "99", "75");
        add("Busan", "사상구", "96", "75");
        add("Busan", "기장군", "100", "77");

        // 3. 대구광역시 (Daegu)
        add("Daegu", "중구", "89", "90");
        add("Daegu", "동구", "90", "91");
        add("Daegu", "서구", "88", "90");
        add("Daegu", "남구", "89", "90");
        add("Daegu", "북구", "89", "91");
        add("Daegu", "수성구", "89", "90");
        add("Daegu", "달서구", "88", "90");
        add("Daegu", "달성군", "86", "88");
        add("Daegu", "군위군", "88", "99");

        // 4. 인천광역시 (Incheon)
        add("Incheon", "중구", "54", "125");
        add("Incheon", "동구", "54", "125");
        add("Incheon", "미추홀구", "54", "124");
        add("Incheon", "연수구", "55", "123");
        add("Incheon", "남동구", "56", "124");
        add("Incheon", "부평구", "55", "125");
        add("Incheon", "계양구", "56", "126");
        add("Incheon", "서구", "55", "126");
        add("Incheon", "강화군", "51", "130");
        add("Incheon", "옹진군", "54", "124");

        // 5. 광주광역시 (Gwangju)
        add("Gwangju", "동구", "60", "74");
        add("Gwangju", "서구", "59", "74");
        add("Gwangju", "남구", "59", "73");
        add("Gwangju", "북구", "59", "75");
        add("Gwangju", "광산구", "57", "74");

        // 6. 대전광역시 (Daejeon)
        add("Daejeon", "동구", "68", "100");
        add("Daejeon", "중구", "68", "100");
        add("Daejeon", "서구", "67", "100");
        add("Daejeon", "유성구", "67", "101");
        add("Daejeon", "대덕구", "68", "100");

        // 7. 울산광역시 (Ulsan)
        add("Ulsan", "중구", "102", "84");
        add("Ulsan", "남구", "102", "84");
        add("Ulsan", "동구", "104", "83");
        add("Ulsan", "북구", "103", "85");
        add("Ulsan", "울주군", "101", "84");

        // 8. 세종특별자치시 (Sejong)
        add("Sejong", "세종특별자치시", "66", "103");

        // 9. 경기도 (Gyeonggi)
        add("Gyeonggi", "수원시 장안구", "60", "121");
        add("Gyeonggi", "수원시 권선구", "60", "120");
        add("Gyeonggi", "수원시 팔달구", "61", "121");
        add("Gyeonggi", "수원시 영통구", "61", "120");
        add("Gyeonggi", "성남시 수정구", "63", "124");
        add("Gyeonggi", "성남시 중원구", "63", "124");
        add("Gyeonggi", "성남시 분당구", "62", "123");
        add("Gyeonggi", "의정부시", "61", "130");
        add("Gyeonggi", "안양시 만안구", "59", "123");
        add("Gyeonggi", "안양시 동안구", "59", "123");
        add("Gyeonggi", "부천시 원미구", "57", "125");
        add("Gyeonggi", "부천시 소사구", "57", "125");
        add("Gyeonggi", "부천시 오정구", "57", "126");
        add("Gyeonggi", "광명시", "58", "125");
        add("Gyeonggi", "평택시", "62", "114");
        add("Gyeonggi", "동두천시", "61", "134");
        add("Gyeonggi", "안산시 상록구", "58", "121");
        add("Gyeonggi", "안산시 단원구", "57", "121");
        add("Gyeonggi", "고양시 덕양구", "57", "128");
        add("Gyeonggi", "고양시 일산동구", "56", "129");
        add("Gyeonggi", "고양시 일산서구", "56", "129");
        add("Gyeonggi", "과천시", "60", "124");
        add("Gyeonggi", "구리시", "62", "127");
        add("Gyeonggi", "남양주시", "64", "128");
        add("Gyeonggi", "오산시", "62", "118");
        add("Gyeonggi", "시흥시", "57", "123");
        add("Gyeonggi", "군포시", "59", "122");
        add("Gyeonggi", "의왕시", "60", "122");
        add("Gyeonggi", "하남시", "64", "126");
        add("Gyeonggi", "용인시 처인구", "64", "119");
        add("Gyeonggi", "용인시 기흥구", "62", "120");
        add("Gyeonggi", "용인시 수지구", "62", "121");
        add("Gyeonggi", "파주시", "56", "131");
        add("Gyeonggi", "이천시", "68", "121");
        add("Gyeonggi", "안성시", "65", "115");
        add("Gyeonggi", "김포시", "55", "128");
        add("Gyeonggi", "화성시", "57", "119");
        add("Gyeonggi", "광주시", "65", "123");
        add("Gyeonggi", "양주시", "61", "131");
        add("Gyeonggi", "포천시", "64", "134");
        add("Gyeonggi", "여주시", "71", "121");
        add("Gyeonggi", "연천군", "61", "138");
        add("Gyeonggi", "가평군", "69", "133");
        add("Gyeonggi", "양평군", "69", "125");

        // 10. 강원특별자치도 (Gangwon)
        add("Gangwon", "춘천시", "73", "134");
        add("Gangwon", "원주시", "76", "122");
        add("Gangwon", "강릉시", "92", "131");
        add("Gangwon", "동해시", "97", "127");
        add("Gangwon", "태백시", "95", "119");
        add("Gangwon", "속초시", "87", "141");
        add("Gangwon", "삼척시", "98", "125");
        add("Gangwon", "홍천군", "75", "130");
        add("Gangwon", "횡성군", "77", "125");
        add("Gangwon", "영월군", "86", "119");
        add("Gangwon", "평창군", "84", "123");
        add("Gangwon", "정선군", "89", "123");
        add("Gangwon", "철원군", "65", "139");
        add("Gangwon", "화천군", "72", "139");
        add("Gangwon", "양구군", "77", "139");
        add("Gangwon", "인제군", "80", "138");
        add("Gangwon", "고성군", "85", "145");
        add("Gangwon", "양양군", "88", "138");

        // 11. 충청북도 (Chungbuk)
        add("Chungbuk", "청주시 상당구", "69", "106");
        add("Chungbuk", "청주시 서원구", "69", "107");
        add("Chungbuk", "청주시 흥덕구", "67", "106");
        add("Chungbuk", "청주시 청원구", "69", "107");
        add("Chungbuk", "충주시", "76", "114");
        add("Chungbuk", "제천시", "81", "118");
        add("Chungbuk", "보은군", "73", "103");
        add("Chungbuk", "옥천군", "71", "99");
        add("Chungbuk", "영동군", "74", "97");
        add("Chungbuk", "증평군", "71", "110");
        add("Chungbuk", "진천군", "68", "111");
        add("Chungbuk", "괴산군", "74", "111");
        add("Chungbuk", "음성군", "72", "113");
        add("Chungbuk", "단양군", "84", "115");

        // 12. 충청남도 (Chungnam)
        add("Chungnam", "천안시 동남구", "63", "110");
        add("Chungnam", "천안시 서북구", "63", "112");
        add("Chungnam", "공주시", "63", "102");
        add("Chungnam", "보령시", "54", "100");
        add("Chungnam", "아산시", "60", "110");
        add("Chungnam", "서산시", "51", "110");
        add("Chungnam", "논산시", "62", "97");
        add("Chungnam", "계룡시", "65", "99");
        add("Chungnam", "당진시", "54", "112");
        add("Chungnam", "금산군", "69", "95");
        add("Chungnam", "부여군", "59", "99");
        add("Chungnam", "서천군", "55", "94");
        add("Chungnam", "청양군", "57", "103");
        add("Chungnam", "홍성군", "55", "106");
        add("Chungnam", "예산군", "58", "107");
        add("Chungnam", "태안군", "48", "109");

        // 13. 전북특별자치도 (Jeonbuk)
        add("Jeonbuk", "전주시 완산구", "63", "89");
        add("Jeonbuk", "전주시 덕진구", "63", "89");
        add("Jeonbuk", "군산시", "56", "92");
        add("Jeonbuk", "익산시", "60", "91");
        add("Jeonbuk", "정읍시", "58", "83");
        add("Jeonbuk", "남원시", "68", "80");
        add("Jeonbuk", "김제시", "59", "88");
        add("Jeonbuk", "완주군", "63", "89");
        add("Jeonbuk", "진안군", "68", "88");
        add("Jeonbuk", "무주군", "72", "93");
        add("Jeonbuk", "장수군", "70", "85");
        add("Jeonbuk", "임실군", "66", "84");
        add("Jeonbuk", "순창군", "63", "79");
        add("Jeonbuk", "고창군", "56", "80");
        add("Jeonbuk", "부안군", "56", "87");

        // 14. 전라남도 (Jeonnam)
        add("Jeonnam", "목포시", "50", "67");
        add("Jeonnam", "여수시", "73", "66");
        add("Jeonnam", "순천시", "70", "70");
        add("Jeonnam", "나주시", "56", "71");
        add("Jeonnam", "광양시", "73", "70");
        add("Jeonnam", "담양군", "61", "78");
        add("Jeonnam", "곡성군", "66", "77");
        add("Jeonnam", "구례군", "69", "75");
        add("Jeonnam", "고흥군", "66", "62");
        add("Jeonnam", "보성군", "62", "66");
        add("Jeonnam", "화순군", "61", "72");
        add("Jeonnam", "장흥군", "59", "64");
        add("Jeonnam", "강진군", "57", "63");
        add("Jeonnam", "해남군", "54", "61");
        add("Jeonnam", "영암군", "56", "66");
        add("Jeonnam", "무안군", "52", "71");
        add("Jeonnam", "함평군", "52", "72");
        add("Jeonnam", "영광군", "52", "77");
        add("Jeonnam", "장성군", "57", "77");
        add("Jeonnam", "완도군", "57", "56");
        add("Jeonnam", "진도군", "48", "59");
        add("Jeonnam", "신안군", "50", "66");

        // 15. 경상북도 (Gyeongbuk)
        add("Gyeongbuk", "포항시 남구", "102", "94");
        add("Gyeongbuk", "포항시 북구", "102", "95");
        add("Gyeongbuk", "경주시", "100", "91");
        add("Gyeongbuk", "김천시", "80", "96");
        add("Gyeongbuk", "안동시", "91", "106");
        add("Gyeongbuk", "구미시", "84", "96");
        add("Gyeongbuk", "영주시", "89", "111");
        add("Gyeongbuk", "영천시", "95", "93");
        add("Gyeongbuk", "상주시", "81", "102");
        add("Gyeongbuk", "문경시", "81", "106");
        add("Gyeongbuk", "경산시", "91", "90");
        add("Gyeongbuk", "의성군", "90", "101");
        add("Gyeongbuk", "청송군", "96", "103");
        add("Gyeongbuk", "영양군", "97", "108");
        add("Gyeongbuk", "영덕군", "102", "103");
        add("Gyeongbuk", "청도군", "91", "86");
        add("Gyeongbuk", "고령군", "83", "87");
        add("Gyeongbuk", "성주군", "83", "91");
        add("Gyeongbuk", "칠곡군", "85", "93");
        add("Gyeongbuk", "예천군", "86", "107");
        add("Gyeongbuk", "봉화군", "90", "113");
        add("Gyeongbuk", "울진군", "102", "115");
        add("Gyeongbuk", "울릉군", "127", "127");
        add("Gyeongbuk", "독도", "144", "123");

        // 16. 경상남도 (Gyeongnam)
        add("Gyeongnam", "창원시 의창구", "90", "77");
        add("Gyeongnam", "창원시 성산구", "91", "76");
        add("Gyeongnam", "창원시 마산합포구", "89", "76");
        add("Gyeongnam", "창원시 마산회원구", "89", "76");
        add("Gyeongnam", "창원시 진해구", "91", "75");
        add("Gyeongnam", "진주시", "81", "75");
        add("Gyeongnam", "통영시", "87", "68");
        add("Gyeongnam", "사천시", "80", "71");
        add("Gyeongnam", "김해시", "95", "77");
        add("Gyeongnam", "밀양시", "92", "83");
        add("Gyeongnam", "거제시", "90", "69");
        add("Gyeongnam", "양산시", "97", "79");
        add("Gyeongnam", "의령군", "83", "78");
        add("Gyeongnam", "함안군", "86", "77");
        add("Gyeongnam", "창녕군", "87", "83");
        add("Gyeongnam", "고성군", "85", "71");
        add("Gyeongnam", "남해군", "77", "68");
        add("Gyeongnam", "하동군", "74", "73");
        add("Gyeongnam", "산청군", "76", "80");
        add("Gyeongnam", "함양군", "74", "82");
        add("Gyeongnam", "거창군", "77", "86");
        add("Gyeongnam", "합천군", "81", "84");

        // 17. 제주특별자치도 (Jeju)
        add("Jeju", "제주시", "53", "38");
        add("Jeju", "서귀포시", "53", "33");
    }

    private static void add(String region, String sigungu, String nx, String ny) {
        coordMap.put(region + "_" + sigungu, new Point(nx, ny));
    }

    public static Point getCoordinate(String region, String sigungu) {
        return coordMap.getOrDefault(region + "_" + sigungu, new Point("60", "127")); // 기본값: 서울
    }
}