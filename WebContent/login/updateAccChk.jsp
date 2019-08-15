<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	if (session.getAttribute("id") == null) {
%>
<script>
	alert("다시 로그인 해주세요.");
	location.href = "login.do";
</script>
<%
return;
	} 
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title><%=session.getAttribute("id")%>(<%=session.getAttribute("nickName")%>) 계정 정보 수정하기</title>
</head>

<script>
	function chkSubmit() {
		var frm = document.forms['frm'];
		var pw = frm['pw'].value.trim();
		var chkPw = frm['chkPw'].value.trim();

		if (pw == "") {
			alert("비밀번호를 입력하세요.");
			frm['pw'].focus();
			return false;
		}
		if (chkPw == "") {
			alert("비밀번호 확인을 입력하세요.");
			frm['chkPw'].focus();
			return false;
		}
		if (pw != chkPw) {
			alert("비밀번호가 일치하지 않습니다.");
			frm['chkPw'].focus();
			return false;
		}
		if (pw != "<%=session.getAttribute("pw")%>") {
			alert("현재 계정의 비밀번호와 일치하지 않습니다.");
			frm['pw'].focus();
			return false;
		}
		if (pw == chkPw && pw == "<%=session.getAttribute("pw")%>") {
			return true;
		}
		return false;
	}
</script>

<body>
	<h3><%=session.getAttribute("id")%>(<%=session.getAttribute("nickName")%>) 계정 정보 수정하기</h3>
	<br>
	<form name="frm" action="updateAcc.do" method="post" onsubmit="return chkSubmit()">
		비밀번호:
		 <input type="password" name="pw" maxlength="25" /><br><br>
		 비밀번호확인:
		 <input type="password" name="chkPw" maxlength="25" />
		 <input type="submit" value="수정하기"><br><br>
	</form>
<button onclick="location.href='welcome.do'">회원창으로 이동</button>
</body>
</html>