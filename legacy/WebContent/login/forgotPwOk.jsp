<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*"%>

<%
	LoginDTO[] arr = (LoginDTO[]) request.getAttribute("acc");
%>

<%if (arr.length == 0) {%>
		<script>
		alert("없는 계정이거나 정보를 잘 못 입력했습니다.");
		history.back();
		</script>
<%} else {%>
	ID: <%= arr[0].getId() %><br>
	비밀번호: <%= arr[0].getPw() %><br>
	<button type="button" onclick="location.href='login.do'">확인</button>
<%}%>