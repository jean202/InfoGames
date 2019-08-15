<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*"%>

<%
	if(session.getAttribute("id") != null){
%>
	<script>
		alert("이미 로그인 돼 있습니다.");
		location.href = "welcome.do";
	</script>
<%
	return;
	}
%>

<%
	LoginDTO[] arr = (LoginDTO[]) request.getAttribute("acc");
%>

<%
	if (arr == null || arr.length == 0) {
		session.invalidate();
%>
<script>
	alert("아이디 또는 비밀번호를 잘 못 입력하셨습니다.");
	location.href = "login.do";
</script>
<%
	} else {
		session.setAttribute("accNum", arr[0].getAccNum());
		session.setAttribute("id", arr[0].getId());
		session.setAttribute("pw", arr[0].getPw());
		session.setAttribute("email", arr[0].getEmail());
		session.setAttribute("nickName", arr[0].getNickName());
%>
<script>
	location.href = "welcome.do";
</script>
<%
	}
%>

