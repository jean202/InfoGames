package com.infogames.reviewboard.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ReviewBoardCommandIF {
	void execute(HttpServletRequest request,HttpServletResponse response);
}
