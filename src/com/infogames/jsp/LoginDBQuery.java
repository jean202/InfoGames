package com.infogames.jsp;

public class LoginDBQuery {
	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	public static final String USERID = "scott5";
	public static final String USERPW = "tiger5";

	public static final String SQL_ACCOUNT_INSERT = 
			"INSERT INTO ACCOUNT"
			+ " (ACCNUM, ID, PW, NAME, EMAIL, BIRTH, GENDER, NICKNAME) "
			+ "VALUES"
			+ "(ACCOUNT_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String SQL_ACCOUNT_SELECT =
			"SELECT * FROM ACCOUNT WHERE ID = ? AND PW = ?";
	
	public static final String SQL_ACCOUNT_ID_SELECT = 
			"SELECT * FROM ACCOUNT WHERE NAME = ? AND EMAIL = ?";
	
	public static final String SQL_ACCOUNT_PW_SELECT = 
			"SELECT * FROM ACCOUNT WHERE ID = ? AND EMAIL = ?";
	
	public static final String SQL_ACCOUNT_DELETE = 
			"DELETE FROM ACCOUNT WHERE ID = ? AND PW = ?";

	public static final String SQL_ACCOUNT_UPDATE =
			"UPDATE ACCOUNT SET ID = ?, PW = ?, EMAIL = ?, NICKNAME = ? WHERE ACCNUM = ?";
	
	public static final String SQL_ACCOUNT_ID_CHECK =
			"SELECT * FROM ACCOUNT WHERE ID = ?";
	
	public static final String SQL_ACCOUNT_NICKNAME_CHECK =
			"SELECT * FROM ACCOUNT WHERE NICKNAME = ?";
}


