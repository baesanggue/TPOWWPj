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
		UserDTO user = new UserDTO();
		user.setId(request.getParameter("id"));
		user.setPw(request.getParameter("pw"));

		UserDAO udao = new UserDAO();
		UserDTO udto = udao.userLogin(user);

		if (udto != null) {
			HttpSession session = request.getSession();
			session.setAttribute("udto", udto);
			System.out.println("로그인 성공 ");
			System.out.println(udto.getUn());

			response.sendRedirect("index.jsp");
		} else {
			request.setAttribute("errorMsg", "아이디 또는 비밀 번호가 올바르지 않습니다. ");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}

}
