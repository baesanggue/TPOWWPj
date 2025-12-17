package com.dongyang.TPOWW.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		UserDTO user = new UserDTO();
		user.setId(request.getParameter("id"));
		user.setPw(request.getParameter("pw"));

		UserDAO udao = new UserDAO();
		UserDTO udto = udao.userLogin(user);

		if (udto != null) {
			HttpSession session = request.getSession();
			session.setAttribute("udto", udto);
			System.out.println("로그인 성공 ");
			System.out.println("[LoginServlet] Session ID: " + session.getId());
			System.out.println(udto.getUn());

			// [추가] 로그인 직후 기존(비로그인 상태) 날씨 세션 삭제
			// 그래야 index.jsp가 weather.do를 호출하여 유저 지역 날씨로 갱신함
			session.removeAttribute("t1h");
			session.removeAttribute("reh");
			session.removeAttribute("rn1");
			session.removeAttribute("wsd");
			session.removeAttribute("PTY");
			session.removeAttribute("SKY");
			session.removeAttribute("POP");
			session.removeAttribute("forecastList");
			session.removeAttribute("threeDayForecast");
			session.removeAttribute("midLandJson");
			session.removeAttribute("midTaJson");
			session.removeAttribute("nx");
			session.removeAttribute("ny");
			session.removeAttribute("currentRegion");

			response.sendRedirect("index.jsp");
		} else {
			request.setAttribute("errorMsg", "아이디 또는 비밀 번호가 올바르지 않습니다. ");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}

}
