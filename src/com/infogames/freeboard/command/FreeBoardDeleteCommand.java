package com.infogames.freeboard.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.FreeBoardDAO;

public class FreeBoardDeleteCommand implements FreeBoardCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FreeBoardDAO dao = new FreeBoardDAO();
		int cnt = 0;

		int writeId = Integer.parseInt(request.getParameter("writeId"));

		try {
			cnt = dao.deleteById(writeId);
			request.setAttribute("result", cnt);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
