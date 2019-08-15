<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*"%>

<%
	LoginDTO[] arr = (LoginDTO[]) request.getAttribute("chkId");
%>

<%if (arr.length == 0) {%>
		<script>
		alert("사용 가능한 아이디입니다.");
		opener.document.forms['frm'].idDuplication.value = "idCheck";
		window.close();
		</script>
<%} else {%>
		<script>
		alert("아이디가 이미 존재합니다.");
		opener.document.forms['frm'].idDuplication.value = "idUnCheck";
		window.close();
		</script>
<%}%>


