<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int cnt = (Integer)request.getAttribute("result");
%>
<% if(cnt == 0){ %>
	<script>
		alert("계정정보 수정실패");
		history.back();
	</script>
<% } else { %>
	<script>
		alert("계정정보 수정 완료");
		location.href="welcome.do";
	</script>
<%
session.setAttribute("accNum", request.getParameter("accNum"));
session.setAttribute("id", request.getParameter("id"));
session.setAttribute("pw", request.getParameter("pw"));
session.setAttribute("email", request.getParameter("email"));
session.setAttribute("nickName", request.getParameter("nickName"));
}
%>