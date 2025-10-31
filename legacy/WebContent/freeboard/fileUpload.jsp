<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- cos 라이브러리 --%>
<%@ page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page import="com.oreilly.servlet.multipart.FileRenamePolicy"%>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%-- parameter 값들, file 값들 추출 --%>
<%@ page import="java.util.Enumeration"%>
<%-- File 객체 다루기 --%>
<%@ page import="java.io.File"%>

<%
	System.out.println("-- fileUpload.jsp --");
	final String SAVE_URL = "freeboard/ckupload";

	// 실제 저장되는 물리적인 경로 확인
	ServletContext context = this.getServletContext();
	String saveDirectory = context.getRealPath(SAVE_URL);
	System.out.println("업로드 경로: " + saveDirectory);

	int maxPostSize = 5 * 1024 * 1024; // POST 최대 5MB
	String encoding = "utf-8"; // response 인코딩
	FileRenamePolicy policy = new DefaultFileRenamePolicy();

	MultipartRequest multi = null;
	multi = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);

	// 2. File part들 추출
	String fileUrl = null;
	Enumeration names = multi.getFileNames(); // type="file"요소의 name들을 추출
	String fileSystemName = null;
	while (names.hasMoreElements()) {
		String name = (String) names.nextElement();
		System.out.println("input name: " + name + "<br>");
		// 위 name에는 form요소의 name이 담겨 있다. 그 name을 가지고
		// 원래 파일(업로드 할 파일)정보를 가져온다.
		String originalFileName = multi.getOriginalFileName(name);
		System.out.println("원본 파일 이름 : " + originalFileName + "<br>");
		
		// 만약 업로드할 폴더에 동일 이름의 파일이 있으면 현재 올리는 파일 이름이 바뀐다.
		// FileRenamePolicy 중복정책
		// 실제로 서버에 저장된 파일 이름을 가져와 보자
		fileSystemName =multi.getFilesystemName(name);
		System.out.println("파일 시스템 이름: " + fileSystemName + "<br>");		

		// 업로딩된 파일의 타입을 알 수 있다 : MIME 타입 (ex: image/png)
		String fileType = multi.getContentType(name);
		System.out.println("파일 타입: " + fileType + "<br>");
		
		// 파일 url을 조립
		fileUrl = request.getContextPath() + "/" + SAVE_URL + "/" + fileSystemName;
		System.out.println("fileUrl:" + fileUrl); // 경로를 저장(서버의 룰에 의해서)
	} // end while
	
	// 에디터에게 경로를 알려주기(json으로)
	// json response해주기 - json.org라이브러리 사용 or 무식하게 문자열로 때려넣기
	String result = "{\"filename\" : \"" + fileSystemName + "\", \"uploaded\" : 1, \"url\" : \"" + fileUrl + "\"}";
	// 19번 프로젝트에서 참조
	out.clear();
	out.println(result);
	out.flush();
	
		
%>