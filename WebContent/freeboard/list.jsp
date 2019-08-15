<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*" %>
<%
	FreeBoardDTO[] arr = (FreeBoardDTO[])request.getAttribute("list");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>자유게시판</title>
<style>
table, th, td {
	border : 1px solid black;
	border-collapse : collapse;
}
</style>
</head>
<body>
<% if(session.getAttribute("id") == null){ %>
	<a href="/InfoGames/login/login.do">로그인</a>
<%} else{%>
	<%=session.getAttribute("id")%>(<%=session.getAttribute("nickName")%>)님 
	<a href="/InfoGames/login/logout.do">로그아웃</a>
	<a href="/InfoGames/login/welcome.do">계정정보</a>
<%} %>
<hr>
<h2>리스트</h2>
<%
	if(arr == null || arr.length == 0){
		out.println("데이터가 없습니다<br>");	
	} else{
%>
<table style="width:100%">
<tr>
<th>번호</th>
<th>제목</th>
<th>작성자</th>
<th>조회수</th>
<th>등록일</th>
</tr>
<%
	for(int i = 0; i < arr.length; i++){
	out.println("<tr>");
	out.println("<td>" + (i  + 1) + "</td>");
	out.println("<td><a href='view.do?writeId=" + arr[i].getWriteId() + "'>" + arr[i].getSubject() + "</a></td>");
	out.println("<td>" + arr[i].getId() + "(" + arr[i].getNickName() + ")</td>");
	out.println("<td>" + arr[i].getViewCnt() + "</td>");
	out.println("<td>" + arr[i].getRegDate() + "</td>");
	out.println("</tr>");
	} 
%>

</table>

<%
	}
%>

<br>
<%if(session.getAttribute("accNum") != null){ %>
<button onClick="location.href='write.do'">새글등록</button>
<% }%>
</body>
</html>