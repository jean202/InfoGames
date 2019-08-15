package com.infogames.login.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.LoginDAO;
import com.infogames.beans.LoginDTO;

public class checkIdCommand implements LoginCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		LoginDAO dao = new LoginDAO();
		LoginDTO[] arr = null;
		
		String id = request.getParameter("id");

		try {
			arr = dao.checkId(id);

			request.setAttribute("chkId", arr);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
