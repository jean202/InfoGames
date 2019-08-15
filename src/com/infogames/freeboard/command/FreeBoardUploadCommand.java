package com.infogames.freeboard.command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infogames.beans.FreeBoardDAO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;

public class FreeBoardUploadCommand implements FreeBoardCommandIF {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FreeBoardDAO dao = new FreeBoardDAO();
		int cnt = 0;
		final String SAVE_URL = "freeboard/ckupload";
		ServletContext context = request.getSession().getServletContext();
		// 실제 저장되는 물리적인 경로 확인
	
		String saveDirectory = context.getRealPath(SAVE_URL);
		System.out.println("업로드 경로: " + saveDirectory);

		int maxPostSize = 5 * 1024 * 1024; // POST 최대 5MB
		String encoding = "utf-8"; // response 인코딩
		FileRenamePolicy policy = new DefaultFileRenamePolicy();

		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

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
		
		String jsonString = "{\"filename\" : \"" + fileSystemName + "\", \"uploaded\" : 1, \"url\" : \"" + fileUrl + "\"}";
		
		try {
			//String jsonString = jsonOutput.toString();	// JSON object를 문자열로 변환
			// 위의 코드는 jdom을 할때만 필요하다.
			response.setContentType("application/json; charset=utf-8");	// MIME 설정
			response.getWriter().write(jsonString);	// response보내기
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}

}
