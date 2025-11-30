package com.dongyang.TPOWW.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.dongyang.TPOWW.util.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GeminiClient implements LlmClient {

    private static final String MODEL = "gemini-2.5-flash";
    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL + ":generateContent";

    private final String apiKey;
    private final Gson gson = new Gson();

    public GeminiClient() {
        this.apiKey = AppConfig.getGeminiApikey();
    }

    @Override
    public String generate(String prompt) throws IOException {

        // 1. 요청 JSON 만들기
        JsonObject root = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject partText = new JsonObject();

        partText.addProperty("text", prompt);
        parts.add(partText);
        content.add("parts", parts);
        contents.add(content);
        root.add("contents", contents);

        String jsonRequest = gson.toJson(root);

        // 2. HTTP 연결
        URL url = new URL(ENDPOINT + "?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        status >= 200 && status < 300
                                ? conn.getInputStream()
                                : conn.getErrorStream(),
                        StandardCharsets.UTF_8));

        StringBuilder resp = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            resp.append(line);
        }
        br.close();
        conn.disconnect();

        if (status < 200 || status >= 300) {
            // 여기서는 너무 자세한 내용은 던지지 않고, 위에서 로그로만 찍는 게 좋음
            throw new IOException("Gemini API 오류 상태코드: " + status);
        }

        // 3. 응답 파싱 (candidates[0].content.parts[0].text)
        JsonObject json = gson.fromJson(resp.toString(), JsonObject.class);
        JsonArray candidates = json.getAsJsonArray("candidates");
        if (candidates == null || candidates.size() == 0) {
            throw new IOException("Gemini 응답에 candidates가 없습니다.");
        }

        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
        JsonObject contentObj = firstCandidate.getAsJsonObject("content");
        JsonArray respParts = contentObj.getAsJsonArray("parts");
        if (respParts == null || respParts.size() == 0) {
            throw new IOException("Gemini 응답에 parts가 없습니다.");
        }

        JsonObject firstPart = respParts.get(0).getAsJsonObject();
        if (!firstPart.has("text")) {
            throw new IOException("Gemini 응답에 text 필드가 없습니다.");
        }

        return firstPart.get("text").getAsString().trim();
    }
}
