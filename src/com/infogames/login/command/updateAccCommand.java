package com.infogames.login.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.LoginDAO;

public class updateAccCommand implements LoginCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		LoginDAO dao = new LoginDAO();
		int cnt = 0;

		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String email = request.getParameter("email");
		String nickName = request.getParameter("nickName");
		int accNum = Integer.parseInt(request.getParameter("accNum"));

		try {
			cnt = dao.updateAcc(id, pw, email, nickName, accNum);
			request.setAttribute("result", cnt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
