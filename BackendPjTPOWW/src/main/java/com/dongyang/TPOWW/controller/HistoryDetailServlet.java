package com.dongyang.TPOWW.controller;

import java.io.IOException;

import com.dongyang.TPOWW.member.UserDTO;
import com.dongyang.TPOWW.tpo.TpoHistoryDAO;
import com.dongyang.TPOWW.tpo.TpoHistoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/historyDetail.do")
public class HistoryDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("udto");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 히스토리 ID 받기
        String hIdStr = request.getParameter("hId");
        if (hIdStr == null || hIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/history.do");
            return;
        }

        try {
            int hId = Integer.parseInt(hIdStr);
            TpoHistoryDAO dao = new TpoHistoryDAO();
            TpoHistoryDTO history = dao.getHistoryById(hId);

            // 본인의 히스토리인지 확인
            if (history == null || history.getUn() != user.getUn()) {
                response.sendRedirect(request.getContextPath() + "/history.do");
                return;
            }

            request.setAttribute("history", history);
            request.getRequestDispatcher("/member/historyDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/history.do");
        }
    }
}
