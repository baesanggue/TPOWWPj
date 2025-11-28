package com.dongyang.TPOWW.member;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/mypage.do")
public class MyPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 마이페이지 조회 (input에 값 채워서 보여주기)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDTO udto = (UserDTO) session.getAttribute("udto");

        // 로그인 안 되어 있으면 index로 보내기
        if (udto == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int un = udto.getUn();

        // user_pref 가져오기
        UserPrefDAO pdao = new UserPrefDAO();
        UserPrefDTO pdto = pdao.getUserPref(un);

        // request에 담아서 JSP로 보내기
        request.setAttribute("udto", udto);
        request.setAttribute("pdto", pdto);

        request.getRequestDispatcher("/member/mypage.jsp").forward(request, response);
    }

    // 마이페이지에서 수정한 내용 저장
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        UserDTO sessionUser = (UserDTO) session.getAttribute("udto");

        if (sessionUser == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int un = sessionUser.getUn();

        // 폼에서 수정된 값 받기
        String uname = request.getParameter("uname");
        String ageStr = request.getParameter("age");
        String gender = request.getParameter("gender");
        String region = request.getParameter("region");

        String brand = request.getParameter("brand");
        String color = request.getParameter("color");
        String pcolor = request.getParameter("pcolor");

        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        // User 수정 DTO
        UserDTO udto = new UserDTO();
        udto.setUn(un);
        udto.setUname(uname);
        udto.setAge(age);
        udto.setGender(gender);
        udto.setRegion(region);

        UserDAO udao = new UserDAO();
        int r = udao.updateUser(udto);
        System.out.println("수정 결과 = "+ r);

        // UserPref 수정 DTO
        UserPrefDTO pdto = new UserPrefDTO();
        pdto.setUn(un);
        pdto.setBrand(brand);
        pdto.setColor(color);
        pdto.setPcolor(pcolor);

        UserPrefDAO pdao = new UserPrefDAO();
        int pr = pdao.updateUserPref(pdto);
        System.out.println("pref 수정 결과 = " + pr);

        // 세션에 있는 유저 정보도 최신값으로 반영
        sessionUser.setUname(uname);
        sessionUser.setAge(age);
        sessionUser.setGender(gender);
        sessionUser.setRegion(region);
        session.setAttribute("udto", sessionUser);

        // 다시 마이페이지로 이동 (PRG 패턴)
        response.sendRedirect("mypage.do");
    }
}
