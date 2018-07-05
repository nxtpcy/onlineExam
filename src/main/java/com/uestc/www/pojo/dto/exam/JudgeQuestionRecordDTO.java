package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import com.alibaba.fastjson.JSON;

public class JudgeQuestionRecordDTO implements Serializable{
	
	private static final long serialVersionUID = -3330538760378369951L;

	private Integer id;
	private String questionId; 
	private String content;
	private String trueOption;
	private String falseOption;
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
