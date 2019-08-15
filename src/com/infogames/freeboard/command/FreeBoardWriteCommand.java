package com.infogames.freeboard.command;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.FreeBoardDAO;

public class FreeBoardWriteCommand implements FreeBoardCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FreeBoardDAO dao = new FreeBoardDAO();
		int cnt = 0;

		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		int accNum = Integer.parseInt(request.getParameter("accNum"));
		
		try {
			cnt = dao.insert(subject, content, accNum);
			request.setAttribute("result", cnt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
