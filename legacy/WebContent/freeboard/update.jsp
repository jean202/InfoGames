<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infogames.beans.*"%>

<%
	FreeBoardDTO[] arr = (FreeBoardDTO[]) request.getAttribute("list");
%>

<%
	if (arr == null || arr.length == 0) {
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
<title>수정<%=subject%></title>
</head>

<script src="ckeditor/ckeditor.js" />

<script>

function chkSubmit(){
	var frm = document.forms['frm'];
	var subject = frm['subject'].value.trim();
	var content = frm['content'].value.trim();
	
	if(subject == ""){
		alert("제목은 반드시 작성해야 합니다");
		frm["subject"].focus();
		return false;
	}
	
	if(content == ""){
		alert("내용은 반드시 작성해야 합니다");
		frm["content"].focus();
		return false;
	}
	
	return true;
}

</script>

<body>
	<h2>
		수정<%=subject%></h2>
	<%-- 글 내용이 많을수 있기 때문에 POST 방식 사용 --%>
	<form name="frm" action="updateOk.do" method="post"
		onsubmit="return chkSubmit()">
		<input type="hidden" name="writeId" value="<%=writeId%>" /> 작성자:<%=id%>(<%=nickName%>)<br>
		제목: <input type="text" name="subject" value="<%=subject%>" /><br>
		내용:<br>
		<textarea name="content" id="editor1"><%=content%></textarea>
		<script>
		CKEDITOR.replace("editor1", {
				allowedContent: true,	// HTML태그 자동삭제 방지 설정
				filebrowserUploadUrl : '<%=request.getContextPath()%>/freeboard/fileUpload.do'
							// 서버 쪽에서 받아서 처리할 url을 명시해줌 // 웹 어플리케이션 (서버의) 경로
							// 파일 업로딩이 발생하면 이 url로 보내라
			});
		</script>
		<br>
		<br> <input type="submit" value="수정" />
	</form>
	<br>
	<button onclick="history.back()">이전으로</button>
	<button type="button" onclick="location.href='list.do'">목록으로</button>
</body>

</html>