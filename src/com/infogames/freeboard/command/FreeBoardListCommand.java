package com.infogames.freeboard.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.FreeBoardDAO;
import com.infogames.beans.FreeBoardDTO;

public class FreeBoardListCommand implements FreeBoardCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FreeBoardDAO dao = new FreeBoardDAO();
		FreeBoardDTO[] arr = null;

		try {
			arr = dao.select();
			request.setAttribute("list", arr);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
