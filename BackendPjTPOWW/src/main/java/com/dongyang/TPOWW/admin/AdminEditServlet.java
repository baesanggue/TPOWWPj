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
        UserDAO udao = new UserDAO();
        UserDTO udto = udao.getUserByUn(un); // 기존 정보 가져오기

        String uname = request.getParameter("uname");
        String ageStr = request.getParameter("age");
        String gender = request.getParameter("gender");
        String region = request.getParameter("region");
        String sigungu = request.getParameter("sigungu");
        String newRole = request.getParameter("role"); // POST로 받은 role

        String brand = request.getParameter("brand");
        String color = request.getParameter("color");
        String pcolor = request.getParameter("pcolor");

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        // 서버단 안전 체크: 본인 계정 or 다른 관리자이면 role 변경 불가
        if (admin.getUn() == udto.getUn() || "ADMIN".equals(udto.getRole())) {
            newRole = udto.getRole(); // 기존 role 유지
        }

        // User 업데이트
        udto.setUname(uname);
        udto.setAge(age);
        udto.setGender(gender);
        udto.setRegion(region);
        udto.setSigungu(sigungu);
        udto.setRole(newRole);
        udao.updateUser(udto);

        // UserPref 업데이트
        UserPrefDTO pdto = new UserPrefDTO();
        pdto.setUn(un);
        pdto.setBrand(brand);
        pdto.setColor(color);
        pdto.setPcolor(pcolor);
        UserPrefDAO pdao = new UserPrefDAO();
        pdao.updateUserPref(pdto);

        response.sendRedirect("admin.do");
    }
}
