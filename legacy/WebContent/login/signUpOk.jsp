<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int cnt = (Integer)request.getAttribute("result");
%>
<% if(cnt == 0){ %>
	<script>
		alert("회원가입 실패");
		history.back();
	</script>
<% } else { %>
	<script>
		alert("회원가입 완료");
		location.href = "login.do";
	</script>
<% } %>