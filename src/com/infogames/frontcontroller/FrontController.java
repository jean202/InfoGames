package com.infogames.frontcontroller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.freeboard.command.FreeBoardCommandIF;
import com.infogames.freeboard.command.FreeBoardDeleteCommand;
import com.infogames.freeboard.command.FreeBoardListCommand;
import com.infogames.freeboard.command.FreeBoardSelectCommand;
import com.infogames.freeboard.command.FreeBoardUpdateCommand;
import com.infogames.freeboard.command.FreeBoardUploadCommand;
import com.infogames.freeboard.command.FreeBoardViewCommand;
import com.infogames.freeboard.command.FreeBoardWriteCommand;
import com.infogames.login.command.LoginCommandIF;
import com.infogames.login.command.checkIdCommand;
import com.infogames.login.command.checkNickNameCommand;
import com.infogames.login.command.deleteAccCommand;
import com.infogames.login.command.findIdCommand;
import com.infogames.login.command.findPwCommand;
import com.infogames.login.command.loginCommand;
import com.infogames.login.command.signUpCommand;
import com.infogames.login.command.updateAccCommand;
import com.infogames.reviewboard.command.ReviewBoardCommandIF;
import com.infogames.tipboard.command.TipBoardCommandIF;

@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FrontController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		actionDo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		actionDo(request, response);
	}

	protected void actionDo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("actionDo() 호출");
		
		request.setCharacterEncoding("UTF-8");
		
		//컨트롤러는 아래 두 가지 정보를 갖고 움직여야 한다.
		String viewPage = null; // 어떠한 페이지에 보여줄지 (View)
		LoginCommandIF loginCommand = null; // 어떠한 로직을 수행할지
		FreeBoardCommandIF freeBoardCommand = null; // 어떠한 로직을 수행할지
		TipBoardCommandIF tipBoardCommand = null; // 어떠한 로직을 수행할지
		ReviewBoardCommandIF reviewBoardCommand = null; // 어떠한 로직을 수행할지
		
		//URL로부터 URI, ContextPath, Command 분리
		String uri = request.getRequestURI();
		String conPath = request.getContextPath();
		String com = uri.substring(conPath.length()); // URL에서 커맨드 분리
		
		//테스트 출력
		System.out.println("uri: " + uri);
		System.out.println("conPath: " + conPath);
		System.out.println("com: " + com);
		
		// 컨트롤러는 커맨드에 따라, 로직을 수행하고
		// 결과를 내보낼 View를 결정한다.
		switch(com) {
		case "/login/login.do":
			viewPage = "/login/login.jsp";
			break;
		case "/login/loginOk.do":
			loginCommand = new loginCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/loginOk.jsp";
			break;
		case "/login/welcome.do":
			viewPage = "/login/welcome.jsp";
			break;
		case "/login/logout.do":
			viewPage = "/login/logout.jsp";
			break;
		case "/login/signUp.do":
			viewPage = "/login/signUp.jsp";
			break;
		case "/login/signUpOk.do":
			loginCommand = new signUpCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/signUpOk.jsp";
			break;
		case "/login/forgotId.do":
			viewPage = "/login/forgotId.jsp";
			break;
		case "/login/forgotIdOk.do":
			loginCommand = new findIdCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/forgotIdOk.jsp";
			break;
		case "/login/forgotPw.do":
			viewPage = "/login/forgotPw.jsp";
			break;
		case "/login/forgotPwOk.do":
			loginCommand = new findPwCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/forgotPwOk.jsp";
			break;
		case "/login/delete.do":
			viewPage = "/login/delete.jsp";
			break;
		case "/login/deleteOk.do":
			loginCommand = new deleteAccCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/deleteOk.jsp";
			break;
		case "/login/updateAccChk.do":
			viewPage = "/login/updateAccChk.jsp";
			break;
		case "/login/updateAcc.do":
			viewPage = "/login/updateAcc.jsp";
			break;
		case "/login/updateAccOk.do":
			loginCommand = new updateAccCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/updateAccOk.jsp";
			break;
		case "/login/idCheckOk.do":
			loginCommand = new checkIdCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/idCheckOk.jsp";
			break;
		case "/login/nickNameCheckOk.do":
			loginCommand = new checkNickNameCommand();
			loginCommand.execute(request, response);
			viewPage = "/login/nickNameCheckOk.jsp";
			break;
		case "/freeboard/list.do":
			freeBoardCommand = new FreeBoardListCommand();
			freeBoardCommand.execute(request, response);
			viewPage = "/freeboard/list.jsp";
			break;
		case "/freeboard/write.do":
			viewPage = "/freeboard/write.jsp";
			break;
		case "/freeboard/writeOk.do":
			freeBoardCommand = new FreeBoardWriteCommand();
			freeBoardCommand.execute(request, response);
			viewPage = "/freeboard/writeOk.jsp";
			break;
		case "/freeboard/view.do":
			freeBoardCommand = new FreeBoardViewCommand();
			freeBoardCommand.execute(request, response);
			viewPage = "/freeboard/view.jsp";
			break;
		case "/freeboard/update.do":
			freeBoardCommand = new FreeBoardSelectCommand();
			freeBoardCommand.execute(request, response);
			viewPage = "/freeboard/update.jsp";
			break;
		case "/freeboard/updateOk.do":
			freeBoardCommand = new FreeBoardUpdateCommand();
			freeBoardCommand.execute(request, response);
			viewPage = "/freeboard/updateOk.jsp";
			break;
		case "/freeboard/deleteOk.do":
			freeBoardCommand = new FreeBoardDeleteCommand();
			freeBoardCommand.execute(request, response);
			viewPage = "/freeboard/deleteOk.jsp";
			break;
		case "/freeboard/fileUpload.do":
			freeBoardCommand = new FreeBoardUploadCommand();
			freeBoardCommand.execute(request, response);
			// viewPage는 직접 보여줄 페이지를 담아서 디스패쳐에 올려주는 녀석이다.
			viewPage = null;
			break;
				
		}// end switch
		
		//response를 위해서 
		// 위에서 결정된 viewPage 에 forward 해줌
		if(viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			// 디스패쳐 : 리퀘스트 할 때 다음에 브라우저에 보여줄 페이지를 포워딩 해 준다
			// 여기서는 실제로 경로를 서버에 올려서 브라우저에 뿌려주는 역할을 하기 위해 사용했다
			dispatcher.forward(request, response);
		}
	}
}
