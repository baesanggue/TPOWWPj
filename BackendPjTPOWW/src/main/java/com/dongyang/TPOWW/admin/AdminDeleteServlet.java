package com.dongyang.TPOWW.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dongyang.TPOWW.member.UserDAO;
import com.dongyang.TPOWW.member.UserDTO;

@WebServlet("/adminDelete.do")
public class AdminDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserDTO admin = (session != null) ? (UserDTO) session.getAttribute("udto") : null;

        // 로그인 확인 + 관리자 확인
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        int targetUn = Integer.parseInt(request.getParameter("un"));

        UserDAO udao = new UserDAO();
        UserDTO targetUser = udao.getUserByUn(targetUn);

        // 자기 자신 또는 다른 관리자 삭제 금지
        if (targetUser == null || targetUser.getUn() == admin.getUn() || "ADMIN".equals(targetUser.getRole())) {
            // 삭제 금지 메시지 추가 가능
            response.sendRedirect("admin.do");
            return;
        }

        // 삭제 진행
        udao.deleteUser(targetUn);

        // 삭제 후 관리자 페이지로 이동
        response.sendRedirect("admin.do");
    }
}

