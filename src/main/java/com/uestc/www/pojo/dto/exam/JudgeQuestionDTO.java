package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;

public class JudgeQuestionDTO implements Serializable{
	
	
	private static final long serialVersionUID = -3468165744238852775L;
	
	private Integer id;
	private String testpaperId; // 试卷号
	private String questionId; 
	private String content;
	private String trueOption;
	private String falseOption;
	private byte questionType;
	private String answer;
	private Integer score;
	
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
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


	public String getTrueOption() {
		return trueOption;
	}


	public void setTrueOption(String trueOption) {
		this.trueOption = trueOption;
	}


	public String getFalseOption() {
		return falseOption;
	}


	public void setFalseOption(String falseOption) {
		this.falseOption = falseOption;
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


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	


}
