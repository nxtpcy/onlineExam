package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;

public class ChoiceQuestionDTO implements Serializable{
	
	private static final long serialVersionUID = -6918239517716479402L;
	private Integer id;
	private String testpaperId; // 试卷号
	private String questionId; 
	private String content;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private byte questionType;
	private String answer;
	private Integer score;
	
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getTestpaperId() {
		return testpaperId;
	}


	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}


	public String getQuestionId() {
		return questionId;
	}


	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getOptionA() {
		return optionA;
	}


	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}


	public String getOptionB() {
		return optionB;
	}


	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}


	public String getOptionC() {
		return optionC;
	}


	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}


	public String getOptionD() {
		return optionD;
	}


	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}


	public byte getQuestionType() {
		return questionType;
	}


	public void setQuestionType(byte questionType) {
		this.questionType = questionType;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


}
