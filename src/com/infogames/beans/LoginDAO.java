package com.infogames.beans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.infogames.jsp.LoginDBQuery;

public class LoginDAO {
	Connection conn;
	PreparedStatement pstmt;
	Statement stmt;
	ResultSet rs;

	public LoginDAO() {
		try {
			Class.forName(LoginDBQuery.DRIVER);
			conn = DriverManager.getConnection(LoginDBQuery.URL, LoginDBQuery.USERID, LoginDBQuery.USERPW);
			System.out.println("LoginDAO생성, 데이터베이스 연결!!");
		} catch (Exception e) {
			e.printStackTrace();
		} // end try
	}// 생성자

	public void close() throws SQLException {
		if (rs != null)
			rs.close();
		if (pstmt != null)
			pstmt.close();
		if (stmt != null)
			stmt.close();
		if (conn != null)
			conn.close();
	} // end close();

	public int signUpAcc(String id, String pw, String name, String email, Date birth, String gender, String nickName) throws SQLException {
		int cnt = 0;
		try {
			// 트랜잭션 실행
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_INSERT);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.setDate(5, birth);
			pstmt.setString(6, gender);
			pstmt.setString(7, nickName);

			cnt = pstmt.executeUpdate();
		} finally {
			close();
		}

		return cnt;
	}

	// ResultSet --> DTO 배열로 리턴
	public LoginDTO[] createArray(ResultSet rs) throws SQLException {
		ArrayList<LoginDTO> list = new ArrayList<LoginDTO>();

		while (rs.next()) {
			int accNum = rs.getInt("ACCNUM");
			String id = rs.getString("ID");
			String pw = rs.getString("PW");
			String name = rs.getString("NAME");
			String email = rs.getString("EMAIL");
			String gender = rs.getString("GENDER");
			String nickName = rs.getString("NICKNAME");
			Date birth = rs.getDate("BIRTH");
			String birthDate = "";
			if (birth != null) {
				birthDate = new SimpleDateFormat("yyyy-MM-dd").format(birth);
			}
			Date date = rs.getDate("REGDATE");
			String regdate = "";
			if (date != null) {
				regdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			}

			LoginDTO dto = new LoginDTO(accNum, id, pw, name, email, gender, nickName);
			dto.setBirth(birthDate);
			dto.setRegdate(regdate);
			list.add(dto);
		}

		int size = list.size();

		LoginDTO[] arr = new LoginDTO[size];
		list.toArray(arr); // 리스트에 저장된 데이터를 배열객체에 복사

		return arr;
	}

	public LoginDTO[] loginAcc(String id, String pw) throws SQLException {
		LoginDTO[] arr = null;

		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_SELECT);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			arr = createArray(rs);
		} finally {
			close();
		}

		return arr;
	}
	
	public LoginDTO[] findIdAcc(String name, String email) throws SQLException {
		LoginDTO[] arr = null;

		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_ID_SELECT);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			arr = createArray(rs);
		} finally {
			close();
		}

		return arr;
	}
	
	public LoginDTO[] findPwAcc(String id, String email) throws SQLException {
		LoginDTO[] arr = null;

		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_PW_SELECT);
			pstmt.setString(1, id);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			arr = createArray(rs);
		} finally {
			close();
		}

		return arr;
	}
	
	public int updateAcc(String id, String pw, String email, String nickName, int accNum) throws SQLException{
		int cnt = 0;
		
		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_UPDATE);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, email);
			pstmt.setString(4, nickName);
			pstmt.setInt(5, accNum);
			cnt = pstmt.executeUpdate();
		} finally {
			close();
		}
		
		return cnt;
	}
	
	public int deleteAcc(String id, String pw) throws SQLException{
		int cnt = 0;
		
		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_DELETE);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			cnt = pstmt.executeUpdate();
		} finally {
			close();
		}
		
		return cnt;
	}
	
	public LoginDTO[] checkId(String id) throws SQLException{
		LoginDTO[] arr = null;

		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_ID_CHECK);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			arr = createArray(rs);
		} finally {
			close();
		}

		return arr;
	}
	
	public LoginDTO[] checkNickName(String nickName) throws SQLException{
		LoginDTO[] arr = null;

		try {
			pstmt = conn.prepareStatement(LoginDBQuery.SQL_ACCOUNT_NICKNAME_CHECK);
			pstmt.setString(1, nickName);
			rs = pstmt.executeQuery();
			arr = createArray(rs);
		} finally {
			close();
		}

		return arr;
	}
}
