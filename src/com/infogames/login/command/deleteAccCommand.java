package com.infogames.login.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.LoginDAO;

public class deleteAccCommand implements LoginCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		LoginDAO dao = new LoginDAO();
		int cnt = 0;
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");

		try {
			cnt = dao.deleteAcc(id, pw);
			request.setAttribute("result", cnt);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
