<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*"%>

<%
	LoginDTO[] arr = (LoginDTO[]) request.getAttribute("chkNickName");
%>

<%if (arr.length == 0) {%>
		<script>
		alert("사용 가능한 닉네임입니다.");
		opener.document.forms['frm'].nickNameDuplication.value = "nickNameCheck";
		window.close();
		</script>
<%} else {%>
		<script>
		alert("닉네임이 이미 존재합니다.");
		opener.document.forms['frm'].nickNameDuplication.value = "nickNameUnCheck";
		window.close();
		</script>
<%}%>


