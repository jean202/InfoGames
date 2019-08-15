package com.infogames.freeboard.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FreeBoardCommandIF {
	void execute(HttpServletRequest request,HttpServletResponse response);
}
