package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;

public class ChoiceQuestionRecordDTO implements Serializable{
	
	private static final long serialVersionUID = -6921931738776131049L;
	
	private Integer id;
	private String questionId; 
	private String content;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private byte questionType; // 题目类型：单选或判断题 
	private String correctAnswer; // 题目正确答案
	private String stuAnswer; // 学生的答案
	private Integer questionScore; // 题目分值
	private Integer stuScore; // 学生得分
	private String analysis; // 题目的解析
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
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

	

	public byte getQuestionType() {
		return questionType;
	}

	public void setQuestionType(byte questionType) {
		this.questionType = questionType;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getStuAnswer() {
		return stuAnswer;
	}

	public void setStuAnswer(String stuAnswer) {
		this.stuAnswer = stuAnswer;
	}

	public Integer getQuestionScore() {
		return questionScore;
	}

	public void setQuestionScore(Integer questionScore) {
		this.questionScore = questionScore;
	}

	public Integer getStuScore() {
		return stuScore;
	}

	public void setStuScore(Integer stuScore) {
		this.stuScore = stuScore;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}

	
	

}
