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
import com.dongyang.TPOWW.member.UserPrefDAO;
import com.dongyang.TPOWW.member.UserPrefDTO;

@WebServlet("/adminEdit.do")
public class AdminEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 수정 화면 띄우기
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserDTO admin = (session != null) ? (UserDTO) session.getAttribute("udto") : null;

        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        int un = Integer.parseInt(request.getParameter("un"));

        UserDAO udao = new UserDAO();
        UserDTO udto = udao.getUserByUn(un);

        UserPrefDAO pdao = new UserPrefDAO();
        UserPrefDTO pdto = pdao.getUserPref(un);

        request.setAttribute("udto", udto);
        request.setAttribute("pdto", pdto);

        request.getRequestDispatcher("/admin/adminEdit.jsp").forward(request, response);
    }

    // 수정 내용 저장
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        UserDTO admin = (session != null) ? (UserDTO) session.getAttribute("udto") : null;

        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }

        int un = Integer.parseInt(request.getParameter("un"));

        String uname = request.getParameter("uname");
        String ageStr = request.getParameter("age");
        String gender = request.getParameter("gender");
        String region = request.getParameter("region");
        String role = request.getParameter("role"); // USER / ADMIN

        String brand = request.getParameter("brand");
        String color = request.getParameter("color");
        String pcolor = request.getParameter("pcolor");

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        // user 업데이트
        UserDTO udto = new UserDTO();
        udto.setUn(un);
        udto.setUname(uname);
        udto.setAge(age);
        udto.setGender(gender);
        udto.setRegion(region);
        udto.setRole(role);

        UserDAO udao = new UserDAO();
        udao.updateUser(udto);   // updateUser에 role도 같이 업데이트하도록 수정하면 더 좋음

        // user_pref 업데이트
        UserPrefDTO pdto = new UserPrefDTO();
        pdto.setUn(un);
        pdto.setBrand(brand);
        pdto.setColor(color);
        pdto.setPcolor(pcolor);

        UserPrefDAO pdao = new UserPrefDAO();
        pdao.updateUserPref(pdto);

        // 수정 후 관리자 목록으로 복귀
        response.sendRedirect("admin.do");
    }
}
