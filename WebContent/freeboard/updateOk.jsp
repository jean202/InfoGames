<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	int cnt = (Integer)request.getAttribute("result");
	int writeId = Integer.parseInt(request.getParameter("writeId"));
%>
<% if(cnt == 0){ %>
	<script>
		alert("수정 실패!");
		history.back();    // 브라우저가 직전에 기억하는 페이지로
	</script>
<% } else { %>
	<script>
		alert("수정 성공!");
		location.href = "view.do?writeId=<%=writeId%>";
	</script>
<% } %>
