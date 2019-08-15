<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	if(session.getAttribute("id") == null){
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
<title><%=session.getAttribute("nickName") %>님 환영합니다.</title>
</head>
<body>
<h1><%=session.getAttribute("id")%>(<%=session.getAttribute("nickName")%>)님 </h1>
<button type="button" onclick="location.href='logout.do'">로그아웃</button>
<button type="button" onclick="location.href='updateAccChk.do'">계정정보 수정하기</button>
<button type="button" onclick="location.href='delete.do?id=<%=session.getAttribute("id")%>'">계정삭제하기</button>
<button type="button" onclick="location.href='/InfoGames/freeboard/list.do'">자유게시판</button>
</body>
</html>