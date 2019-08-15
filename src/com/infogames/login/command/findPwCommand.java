package com.infogames.login.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.LoginDAO;
import com.infogames.beans.LoginDTO;

public class findPwCommand implements LoginCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		LoginDAO dao = new LoginDAO();
		LoginDTO[] dto = null;
		
		String id = request.getParameter("id");
		String email = request.getParameter("email");
		
		try {
			dto = dao.findPwAcc(id, email);
			request.setAttribute("acc", dto);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
