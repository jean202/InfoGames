<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
</head>

<%
	if(session.getAttribute("id") != null){
%>
	<script>
		alert("로그아웃 후에 이용해주세요.");
		location.href = "welcome.do";
	</script>
<%
return;
	}
%>

<script>

function chkSubmit(){
	var frm = document.forms['frm'];
	var id = frm['id'].value.trim();
	var email = frm['email'].value.trim();
	
	if(id == ""){
		alert("아이디를 입력하세요.");
		frm['id'].focus();
		return false;
	}
	if(email == ""){
		alert("이메일을 입력하세요.");
		frm['email'].focus();
		return false;
	}
	return true;
}

</script>

<body>
<h1>비밀번호 찾기</h1>
<form name="frm" action="forgotPwOk.do" method="post" onsubmit="return chkSubmit()">
ID:
<input type="text" name="id" maxlength="15"><br><br>
Email:
<input type="text" name="email" maxlength="20"><br><br>
<input type="submit" value="비밀번호찾기"><br><br>
</form>
<button type="button" onclick="location.href='login.do'">로그인 화면으로 이동</button>
</body>
</html>