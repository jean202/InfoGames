<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>아이디 찾기</title>
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
	var name = frm['name'].value.trim();
	var email = frm['email'].value.trim();
	
	if(name == ""){
		alert("이름을 입력하세요.");
		frm['name'].focus();
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
<h1>아이디 찾기</h1>
<form name="frm" action="forgotIdOk.do" method="post" onsubmit="return chkSubmit()">
이름:
<input type="text" name="name" maxlength="15"><br><br>
Email:
<input type="text" name="email" maxlength="20"><br><br>
<input type="submit" value="ID찾기"><br><br>
</form>
<button type="button" onclick="location.href='login.do'">로그인 화면으로 이동</button>
</body>
</html>