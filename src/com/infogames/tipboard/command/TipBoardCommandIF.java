package com.infogames.tipboard.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TipBoardCommandIF {
	void execute(HttpServletRequest request,HttpServletResponse response);
}
