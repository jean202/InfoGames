package com.infogames.login.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.LoginDAO;
import com.infogames.beans.LoginDTO;

public class loginCommand implements LoginCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		LoginDAO dao = new LoginDAO();
		LoginDTO[] dto = null;
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");

		try {
			dto = dao.loginAcc(id, pw);

			request.setAttribute("acc", dto);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
