<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int cnt = (Integer)request.getAttribute("result");
%>
<% if(cnt == 0){ %>
	<script>
		alert("계정삭제 실패");
		location.href = "login.do";
	</script>
<% } else { 
session.invalidate();
%>
	<script>
		alert("계정이 삭제 되었습니다.");
		location.href = "login.do";
	</script>
<% } %>