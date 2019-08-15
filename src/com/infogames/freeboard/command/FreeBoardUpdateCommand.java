package com.infogames.freeboard.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.FreeBoardDAO;

public class FreeBoardUpdateCommand implements FreeBoardCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FreeBoardDAO dao = new FreeBoardDAO();
		int cnt = 0;

		
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		int writeId = Integer.parseInt(request.getParameter("writeId"));

		try {
			cnt = dao.update(subject, content, writeId);
			request.setAttribute("result", cnt);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
