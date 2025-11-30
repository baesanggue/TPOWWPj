package com.dongyang.TPOWW.controller;

import java.io.IOException;

import com.dongyang.TPOWW.ai.GeminiClient;
import com.dongyang.TPOWW.ai.LlmClient;
import com.dongyang.TPOWW.tpo.TpoRequest;
import com.dongyang.TPOWW.tpo.TpoResult;
import com.dongyang.TPOWW.tpo.TpoService;
import com.dongyang.TPOWW.weather.KmaWeatherService;
import com.dongyang.TPOWW.weather.WeatherService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tpoRecommend.do")
public class TpoRecommendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TpoService tpoService;

    @Override
    public void init() throws ServletException {
        WeatherService weatherService = new KmaWeatherService();
        LlmClient llmClient = new GeminiClient();
        this.tpoService = new TpoService(weatherService, llmClient);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        TpoRequest tpoReq = buildTpoRequest(request);

        TpoResult result;
        try {
            result = tpoService.recommend(tpoReq);
        } catch (IOException e) {
            e.printStackTrace(); // 서버 로그
            request.setAttribute("error", "AI 추천 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            request.getRequestDispatcher("/tpo/tpoResult.jsp").forward(request, response);
            return;
        }

        request.setAttribute("tpoResult", result);
        request.setAttribute("tpoRequest", tpoReq);

        request.getRequestDispatcher("/tpo/tpoResult.jsp").forward(request, response);
    }

    private TpoRequest buildTpoRequest(HttpServletRequest request) {
        TpoRequest r = new TpoRequest();

        r.setGender(request.getParameter("gender"));

        String ageStr = request.getParameter("age");
        try {
            r.setAge(Integer.parseInt(ageStr));
        } catch (NumberFormatException e) {
            r.setAge(0);
        }

        r.setRegion(request.getParameter("region"));
        r.setSigungu(request.getParameter("sigungu"));
        r.setIndoor(request.getParameter("indoor") != null);
        r.setOutdoor(request.getParameter("outdoor") != null);
        r.setDate(request.getParameter("whenDate"));
        r.setTime(request.getParameter("whenTime"));
        r.setWhat(request.getParameter("what"));

        r.setBrand(request.getParameter("brand"));
        r.setColor(request.getParameter("color"));
        r.setPcolor(request.getParameter("pcolor"));

        return r;
    }
}
