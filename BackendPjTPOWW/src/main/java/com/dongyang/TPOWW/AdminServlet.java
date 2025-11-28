package com.dongyang.TPOWW;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin.do")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserDTO udto = (session != null) ? (UserDTO) session.getAttribute("udto") : null;

        if (udto == null || !"ADMIN".equals(udto.getRole())) {
            // 관리자 아니면 접근 불가
            response.sendRedirect("index.jsp");
            return;
        }

        UserDAO udao = new UserDAO();
        List<UserWithPrefDTO> list = udao.findAllusersWithPref();

        request.setAttribute("userList", list);

        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
}
