package com.infogames.jsp;

public class FreeBoardDBQuery {
	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	public static final String USERID = "scott5";
	public static final String USERPW = "tiger5";

	public static final String SQL_FREEBOARD_INSERT = 
			"INSERT INTO WRITEID "
			+ "(WRITE_ID, SUBJECT, CONTENT) "
			+ "VALUES "
			+ "(WRITE_ID_SEQ.nextval, ?, ?)";
	
	public static final String SQL_FREEBOARD_ID_INSERT = 
			"INSERT INTO FREEBOARD VALUES "
			+ "(?, 1, WRITE_ID_SEQ.CURRVAL)";
	
	public static final String SQL_FREEBOARD_SELECT = 
			"SELECT A.ID ID, A.NICKNAME NICKNAME, W.SUBJECT, W.CONTENT, W.REGDATE, W.VIEWCNT, W.WRITE_ID, F.ACCNUM, F.BOARD_ID " + 
			"FROM WRITEID W " + 
			"INNER JOIN FREEBOARD F " + 
			"ON W.WRITE_ID = F.WRITE_ID " + 
			"INNER JOIN ACCOUNT A " + 
			"ON A.ACCNUM = F.ACCNUM";
	
	public static final String SQL_FREEBOARD_SELECT_BY_ID = 
			"SELECT A.ID, A.NICKNAME, W.SUBJECT, W.CONTENT, W.REGDATE, W.VIEWCNT, W.WRITE_ID, F.ACCNUM, F.BOARD_ID "
			+ "FROM WRITEID W, FREEBOARD F, ACCOUNT A "
			+ "WHERE W.WRITE_ID = F.WRITE_ID AND A.ACCNUM = F.ACCNUM AND W.WRITE_ID = ?";
	
	public static final String SQL_FREEBOARD_INC_VIEWCNT = 
			"UPDATE WRITEID SET VIEWCNT = VIEWCNT + 1 WHERE WRITE_ID = ?";
	
	public static final String SQL_FREEBOARD_UPDATE =
			"UPDATE WRITEID SET SUBJECT = ?, CONTENT = ? WHERE WRITE_ID = ?";
	
	public static final String SQL_FREEBOARD_DELETE_BY_ID = 
			"DELETE FROM WRITEID WHERE WRITE_ID = ?";
	
	public static final String SQL_FREEBOARDID_DELETE_BY_ID = 
			"DELETE FROM FREEBOARD WHERE WRITE_ID = ?";

}
