package com.dongyang.TPOWW.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.dongyang.TPOWW.member.UserDTO;
import com.dongyang.TPOWW.tpo.TpoHistoryDAO;
import com.dongyang.TPOWW.tpo.TpoHistoryDTO;

@WebServlet("/history.do")
public class TpoHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDTO udto = (UserDTO) session.getAttribute("udto");

        if (udto == null) {
            request.setAttribute("errorMsg", "로그인이 필요한 서비스입니다.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        TpoHistoryDAO dao = new TpoHistoryDAO();
        List<TpoHistoryDTO> historyList = dao.getHistoryByUn(udto.getUn());

        request.setAttribute("historyList", historyList);
        request.getRequestDispatcher("/member/history.jsp").forward(request, response);
    }
}
