package com.infogames.login.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.LoginDAO;
import com.infogames.beans.LoginDTO;

public class checkNickNameCommand implements LoginCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		LoginDAO dao = new LoginDAO();
		LoginDTO[] arr = null;
		
		String nickName = request.getParameter("nickName");

		try {
			arr = dao.checkNickName(nickName);

			request.setAttribute("chkNickName", arr);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
