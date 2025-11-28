package com.dongyang.TPOWW;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/regist.do")
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public RegistServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		UserDTO udto = new UserDTO();
		udto.setId(request.getParameter("id"));
		udto.setPw(request.getParameter("pw"));
		udto.setUname(request.getParameter("uname"));
		udto.setAge(Integer.parseInt(request.getParameter("age")));
		udto.setGender(request.getParameter("gender"));
		udto.setRegion(request.getParameter("region"));
		
		
		UserDAO udao = new UserDAO();
		int un = udao.registUser(udto);
		
		if (un == 0) {
			request.setAttribute("errorMsg", "회원 정보 저장 실패");
			request.getRequestDispatcher("regist.jsp").forward(request, response);
			return;
		}
		
		UserPrefDTO pdto = new UserPrefDTO();
		pdto.setUn(un);
		pdto.setBrand(request.getParameter("brand"));
		pdto.setColor(request.getParameter("color"));
		pdto.setPcolor(request.getParameter("pcolor"));
		
		UserPrefDAO pdao = new UserPrefDAO();
		int prefok = pdao.registUserPref(pdto);
		
		
		response.sendRedirect("index.jsp");
	}

}
