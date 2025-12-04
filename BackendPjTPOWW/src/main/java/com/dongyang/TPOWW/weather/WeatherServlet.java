package com.dongyang.TPOWW.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.dongyang.TPOWW.member.UserDTO;
import com.dongyang.TPOWW.weather.LocationCoord.Point;

@WebServlet("/weather.do")
public class WeatherServlet extends HttpServlet {

    private static final String SERVICE_KEY = "3659dfdc17c8d704ea3b676ae54690c6df40cfa660853eb227dbe7bd57d50747";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // ===============================
        // 0. 기본값 설정
        // ===============================
        String nx = "60";  // 서울
        String ny = "127";
        String regionMidCode = "11B10101";
        String displayRegion = "서울특별시";

        // ===============================
        // 1. 로그인 체크 및 좌표 변경
        // ===============================
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserDTO udto = (UserDTO) session.getAttribute("udto");
            if (udto != null) {
                String region = udto.getRegion();   // 예: "Seoul"
                String sigungu = udto.getSigungu(); // 예: "강남구"

                // 좌표 변경
                Point pt = LocationCoord.getCoordinate(region, sigungu);
                if (pt != null) {
                    nx = pt.x;
                    ny = pt.y;
                }
                
               
                // 중기예보 지역 코드 변경
                String midCode = LocationCoord.getMidCode(region, sigungu);
                if (midCode != null) regionMidCode = midCode;

                displayRegion = getKoreanRegionName(region) + " " + sigungu;
            }
        }

        request.setAttribute("currentRegion", displayRegion);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String baseDate = now.format(dateFmt);

        // 0~1시는 이전날 23시 기준
        if (now.getHour() < 2) {
            baseDate = now.minusDays(1).format(dateFmt);
        }

        // ===============================
        // ① 현재날씨 (XML)
        // ===============================
        try {
            String url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?"
                    + "serviceKey=" + SERVICE_KEY
                    + "&dataType=XML"
                    + "&numOfRows=100"
                    + "&pageNo=1"
                    + "&base_date=" + baseDate
                    + "&base_time=0200"
                    + "&nx=" + nx + "&ny=" + ny;

            Document doc = loadXML(url);
            NodeList items = doc.getElementsByTagName("item");

            String T1H = "-", REH = "-", RN1 = "-", WSD = "-";

            for (int i = 0; i < items.getLength(); i++) {
                Element e = (Element) items.item(i);

                String category = getTag(e, "category");
                String val = getTag(e, "obsrValue");

                switch (category) {
                    case "T1H": T1H = val; break;
                    case "REH": REH = val; break;
                    case "RN1": RN1 = val; break;
                    case "WSD": WSD = val; break;
                }
            }

            request.setAttribute("t1h", T1H);
            request.setAttribute("reh", REH);
            request.setAttribute("rn1", RN1);
            request.setAttribute("wsd", WSD);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("t1h", "-");
            request.setAttribute("reh", "-");
            request.setAttribute("rn1", "-");
            request.setAttribute("wsd", "-");
        }

        // ===============================
        // ② 단기예보 (base_time=0500 기준)
        // ===============================
        Map<String, Map<String, Object>> dailyList = new LinkedHashMap<>();

        try {
            String shortUrl =
                "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"
                + "serviceKey=" + SERVICE_KEY
                + "&dataType=XML"
                + "&numOfRows=2000"
                + "&pageNo=1"
                + "&base_date=" + baseDate
                + "&base_time=0500"
                + "&nx=" + nx + "&ny=" + ny;

            Document doc = loadXML(shortUrl);
            NodeList items = doc.getElementsByTagName("item");

            Map<String, Map<String, String>> tempMap = new HashMap<>();

            for (int i = 0; i < items.getLength(); i++) {
                Element e = (Element) items.item(i);

                String fcstDate = getTag(e, "fcstDate");
                String fcstTime = getTag(e, "fcstTime");
                String category = getTag(e, "category");
                String fcstValue = getTag(e, "fcstValue");

                if (!category.equals("TMP")) continue;

                tempMap
                    .computeIfAbsent(fcstDate, k -> new HashMap<>())
                    .put(fcstTime, fcstValue);
            }

            for (int d = 0; d <= 3; d++) {
                String keyDate = LocalDate.now()
                        .plusDays(d)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                Map<String, Object> day = new HashMap<>();
                Map<String, String> tmap = tempMap.get(keyDate);

                if (tmap != null) {
                    String morning = tmap.getOrDefault("0600", "-");
                    String afternoon = tmap.getOrDefault("1500", "-");

                    day.put("morningTemp", morning);
                    day.put("afternoonTemp", afternoon);
                } else {
                    day.put("morningTemp", "-");
                    day.put("afternoonTemp", "-");
                }

                dailyList.put(keyDate, day);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===============================
        // ③ 중기예보
        // ===============================
        try {
            LocalDateTime mid = LocalDateTime.now().withMinute(0).withSecond(0);
            if (now.getHour() < 18) mid = mid.withHour(6);
            else mid = mid.withHour(18);

            String tmFc = mid.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

            String midUrl =
                "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?"
                + "serviceKey=" + SERVICE_KEY
                + "&dataType=XML"
                + "&regId=" + regionMidCode
                + "&tmFc=" + tmFc;

            Document doc = loadXML(midUrl);
            NodeList items = doc.getElementsByTagName("item");

            if (items.getLength() > 0) {
                Element e = (Element) items.item(0);

                for (int i = 4; i <= 10; i++) {
                    String key = LocalDate.now().plusDays(i)
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                    String min = getTag(e, "taMin" + i, "-");
                    String max = getTag(e, "taMax" + i, "-");

                    Map<String, Object> day = new HashMap<>();
                    day.put("morningTemp", min);
                    day.put("afternoonTemp", max);

                    dailyList.put(key, day);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("dailyList", dailyList);

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    // ===============================
    // XML 로드 함수
    // ===============================
    private Document loadXML(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(conn.getInputStream());
    }

    // 태그값 가져오기
    private String getTag(Element e, String tag) {
        try {
            return e.getElementsByTagName(tag).item(0).getTextContent();
        } catch (Exception ex) {
            return "-";
        }
    }

    private String getTag(Element e, String tag, String def) {
        try {
            return e.getElementsByTagName(tag).item(0).getTextContent();
        } catch (Exception ex) {
            return def;
        }
    }

    private String getKoreanRegionName(String regionCode) {
        if (regionCode == null) return "";
        switch (regionCode) {
            case "Seoul": return "서울특별시";
            case "Busan": return "부산광역시";
            case "Daegu": return "대구광역시";
            case "Incheon": return "인천광역시";
            case "Gwangju": return "광주광역시";
            case "Daejeon": return "대전광역시";
            case "Ulsan": return "울산광역시";
            case "Sejong": return "세종특별자치시";
            case "Gyeonggi": return "경기도";
            case "Gangwon": return "강원특별자치도";
            case "Chungbuk": return "충청북도";
            case "Chungnam": return "충청남도";
            case "Jeonbuk": return "전북특별자치도";
            case "Jeonnam": return "전라남도";
            case "Gyeongbuk": return "경상북도";
            case "Gyeongnam": return "경상남도";
            case "Jeju": return "제주특별자치도";
            default: return regionCode;
        }
    }
}
