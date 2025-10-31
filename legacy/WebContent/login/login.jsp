<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>

<%
if(session.getAttribute("id") != null){
%>
<script>
location.href="welcome.do";
</script>
<%
	return;
}
%>

<script>

function chkSubmit(){
	var frm = document.forms['frm'];
	var id = frm['id'].value.trim();
	var pw = frm['pw'].value.trim();
	
	
	if(id == ""){
		alert("ID는 반드시 입력해야 합니다!");
		frm['id'].focus();
		return false;
	}
	
	if(pw == ""){
		alert("비밀번호는 반드시 입력해야 합니다!");
		frm['pw'].focus();
		return false;
	}
	
	return true;
}


</script>

<body>

<form name="frm" action="loginOk.do" method="post" onsubmit="return chkSubmit()">
ID:
<input type="text" name="id"/><br>
PW:
<input type="password" name="pw"/>
<input type="submit" value="로그인"/><br><br>
</form>

<button type="button" onclick="location.href='signUp.do'">회원가입</button>
<button type="button" onclick="location.href='forgotId.do'">ID찾기</button>
<button type="button" onclick="location.href='forgotPw.do'">비밀번호찾기</button>
<button type="button" onclick="location.href='/InfoGames/freeboard/list.do'">자유게시판</button>
</body>

</html>