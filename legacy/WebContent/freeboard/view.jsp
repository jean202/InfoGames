<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*" %>

<%
	FreeBoardDTO[] arr = (FreeBoardDTO[])request.getAttribute("list");
%>

<%
	if(arr == null || arr.length == 0){
%>
<script>
	alert("해당 글은 삭제되거나 없습니다");
	history.back();
</script>	
<%
	return;
	}
%>

<%
	String id = arr[0].getId();
	String nickName = arr[0].getNickName();
	String subject = arr[0].getSubject();
	String content = arr[0].getContent();
	String regdate = arr[0].getRegDate();
	int viewcnt = arr[0].getViewCnt();
	int writeId = arr[0].getWriteId();
%>


<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>읽기 <%= subject %></title>
</head>

<script>
function chkDelete(writeId){
var r = confirm("삭제하시겠습니까?");

if(r){
	location.href = "deleteOk.do?writeId=" + writeId;	
}

}
</script>

<body>
<h2>읽기 <%= subject %></h2>
<br>
작성자 : <%=id%>(<%= nickName %>)<br>
제목 : <%= subject %><br>
등록일 : <%= regdate %><br>
조회수 : <%= viewcnt %><br>
내용 :
<br><hr>
<div>
<%= content %>
</div>
<hr><br>
<%if(session.getAttribute("accNum") != null){ %>
<%if((Integer)session.getAttribute("accNum") == arr[0].getAccNum()){ %>
<button onclick="location.href='update.do?writeId=<%= writeId %>'">수정하기</button>
<button onclick="chkDelete(<%= writeId %>)">삭제하기</button>
<% } }%>
<button onclick="location.href='list.do'">목록보기</button>
</body>
</html>