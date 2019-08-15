<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>글작성</title>
</head>
<script src="ckeditor/ckeditor.js"/>

<script>

function chkSubmit(){
	var frm = document.forms["frm"];
	var subject = frm["subject"].value.trim();
	var content = frm["content"].value.trim();
	
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
} // end chkSubmit()

</script>

<body>
<h2>글작성</h2>

<form name="frm" action="writeOk.do" method="post" onsubmit="return chkSubmit()">
작성자:
<%=session.getAttribute("id") %>(<%=session.getAttribute("nickName") %>)<br>
제목:
<input type="text" name="subject"/><br>
내용:<br>
<textarea name="content" id="editor1"></textarea>
<script>
	CKEDITOR.replace("editor1", {
		allowedContent: true,	// HTML태그 자동삭제 방지 설정
		filebrowserUploadUrl : '<%=request.getContextPath()%>/freeboard/fileUpload.do'
		// 서버 쪽에서 받아서 처리할 url을 명시해줌 // 웹 어플리케이션 (서버의) 경로
		// 파일 업로딩이 발생하면 이 url로 보내라
	});
</script>
<br><br>
<input type="hidden" name="accNum" value="<%=session.getAttribute("accNum") %>">
<input type="submit" value="등록"/>
</form>
<br>
<button type="button" onclick="location.href='list.do'">목록으로</button>
</body>

</html>