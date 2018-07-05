package com.uestc.www.common;

public enum IdType {

	Role(1, "R"), Resource(2, "RE"), ArticleType(3, "AT"), Article(4, "A"),
	Testpaper(5, "TP"), ChoiceQuestion(6, "CQ"), JudgeQuestion(7, "JQ"),
	Subject(8, "SU");

	private long value;
	private String sign;

	private IdType(long value, String sign) {
		// TODO Auto-generated constructor stub
		this.value = value;
		this.sign = sign;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String toString() {
		return super.toString() + "(" + this.value + ", " + this.sign + ")";
	}
}
