package com.dongyang.TPOWW.member;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/withdraw.do")
public class WithdrawServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public WithdrawServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        UserDTO udto = (UserDTO) session.getAttribute("udto");
        if (udto == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int un = udto.getUn();

        UserDAO udao = new UserDAO();
        int result = udao.deleteUser(un);

        // 세션 날리기
        session.invalidate();

        // 탈퇴 성공/실패는 옵션. 우선 메인으로
        response.sendRedirect("index.jsp");
    }
}
