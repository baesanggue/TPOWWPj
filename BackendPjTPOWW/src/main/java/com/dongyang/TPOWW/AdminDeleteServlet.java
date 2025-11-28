package com.dongyang.TPOWW;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/adminDelete.do")
public class AdminDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserDTO admin = (session != null) ? (UserDTO) session.getAttribute("udto") : null;

        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        String unStr = request.getParameter("un");
        int un = Integer.parseInt(unStr);

        UserDAO udao = new UserDAO();
        udao.deleteUser(un);

        // 삭제 후 다시 관리자 페이지로
        response.sendRedirect("admin.do");
    }
}
