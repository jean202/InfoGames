package com.infogames.beans;

public class LoginDTO {

	private int accNum;
	private String id;
	private String pw;
	private String name;
	private String email;
	private String birth;
	private String gender;
	private String regdate;
	private String nickName;

	public LoginDTO() {
		super();
	}

	public LoginDTO(int accNum, String id, String pw, String name, String email, String gender,
			String nickName) {
		super();
		this.accNum = accNum;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.nickName = nickName;
	}

	public int getAccNum() {
		return accNum;
	}

	public void setAccNum(int accNum) {
		this.accNum = accNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return "WriteDTO] " + accNum + ":" + id + ":" + pw + ":" + name + ":" + email + ":" + birth + ":" + gender + ":"
				+ regdate + ":" + nickName;
	} // end toString()
} // end DTO class
