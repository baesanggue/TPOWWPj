package com.dongyang.TPOWW.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dongyang.TPOWW.member.UserDTO;
import com.dongyang.TPOWW.member.UserPrefDAO;
import com.dongyang.TPOWW.member.UserPrefDTO;


@WebServlet("/tpo.do")
public class TpoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public TpoServlet() {
        super();

    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO udto = (UserDTO) session.getAttribute("udto");
		
		if (udto == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		request.setAttribute("udto", udto);
		
		UserPrefDAO pdao = new UserPrefDAO();
		UserPrefDTO pdto = pdao.getUserPref(udto.getUn());
		
		request.setAttribute("udto", udto);
		request.setAttribute("pdto", pdto);
		
		request.getRequestDispatcher("/tpo/tpo.jsp").forward(request, response);
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
