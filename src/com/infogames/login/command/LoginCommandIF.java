package com.infogames.login.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginCommandIF {
	void execute(HttpServletRequest request,HttpServletResponse response);
}
