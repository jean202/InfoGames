<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
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
	var pw = frm['pw'].value.trim();
	var chkPw = frm['chkPw'].value.trim();
	var name = frm['name'].value.trim();
	var email = frm['email'].value.trim();
	var birth = frm['birth'].value.trim();
	var nickName = frm['nickName'].value.trim();
	var idDuplication = frm['idDuplication'].value.trim();
	var nickNameDuplication = frm['nickNameDuplication'].value.trim();
	
	var idReg = /^[A-za-z0-9]/g;
	var emailReg = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

	if(id == ""){
		alert("아이디를 입력하세요.");
		frm['id'].focus();
		return false;
	}
	if(idReg.test(id)==false){
		alert("아이디를 제대로 입력하세요.");
		frm['id'].focus();
		return false;
	}
	if(pw == ""){
		alert("비밀번호를 입력하세요.");
		frm['pw'].focus();
		return false;
	}
	if(chkPw == ""){
		alert("비밀번호 확인을 입력하세요.");
		frm['chkPw'].focus();
		return false;
	}
	if(pw != chkPw){
		alert("비밀번호가 일치하지 않습니다.");
		frm['chkPw'].focus();
		return false;
	}
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
	if (emailReg.test(email)==false) {
		   alert("이메일을 제대로 입력하세요.");
		   frm['email'].focus();
		   return false;
		}
	if(birth == ""){
		alert("생년월일을 입력하세요.");
		frm['birth'].focus();
		return false;
	}
	if(nickName == ""){
		alert("닉네임을 입력하세요.");
		frm['nickName'].focus();
		return false;
	}
	if(idDuplication != "idCheck"){
		alert("아이디 중복체크를 하세요.")
		return false;
	}
	if(nickNameDuplication != "nickNameCheck"){
		alert("닉네임 중복체크를 하세요.")
		return false;
	}
	
	return true;
}

function openChkId(){
	var frm = document.forms['frm'];
	var id = frm['id'].value.trim();
	var idReg = /^[A-za-z0-9]/g;
	
	if(id == ""){
		alert("아이디를 입력하세요.");
		return;
	}
	
	if(idReg.test(id)==false){
		alert("아이디를 제대로 입력하세요.");
		frm['id'].focus();
		return;
	}
	
	
	window.open("idCheckOk.do?id=" + frm['id'].value.trim(), "chkId", "width=500, height=300, resizable=no, scrollbars=no");
}

function inputIdChk(){
	document.forms['frm'].idDuplication.value="idUncheck";
}

function openChkNickName(){

	if(document.forms['frm']['nickName'].value.trim() == ""){
		alert("닉네임을 입력하세요.");
		return;
	}
	window.open("nickNameCheckOk.do?nickName=" + frm['nickName'].value.trim(), "chkNickName", "width=500, height=300, resizable=no, scrollbars=no");
}

function inputNickNameChk(){
	document.forms['frm'].nickNameDuplication.value="nickNameUncheck";
}

</script>

<body>
<h1>회원가입</h1>

<form name="frm" action="signUpOk.do" method="post" onsubmit="return chkSubmit()">

ID(최대 15자):
<input type="text" name="id" maxlength="15" onkeydown="inputIdChk()" placeholder="영문, 숫자만 가능"/>
<input type="button" onclick="openChkId()" value="중복확인"><br><br>
<input type="hidden" name="idDuplication" value="idUncheck">
비밀번호:
<input type="password" name="pw" maxlength="25"/><br><br>
비밀번호확인:
<input type="password" name="chkPw" maxlength="25"/><br><br>
이름:
<input type="text" name="name" maxlength="20"/><br><br>
Email:
<input type="text" name="email" maxlength="40" placeholder="ex)asd123@naver.com"/><br><br>
생년월일(YYMMDD):
<input type="date" name="birth"><br>
성별:
<input type="radio" name="gender" value="남자" checked/>남자
<input type="radio" name="gender" value="여자"/>여자<br><br>
닉네임(최대 10자):
<input type="text" name="nickName" maxlength="10" onkeydown="inputNickNameChk()"/>
<input type="button" onclick="openChkNickName()" value="중복확인"><br><br>
<input type="hidden" name="nickNameDuplication" value="nickNameUncheck">
<input type="submit" value="가입하기"/><br><br>

</form>

<button type="button" onclick="location.href='login.do'">로그인 화면으로 이동</button>
</body>
</html>