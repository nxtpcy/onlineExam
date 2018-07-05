package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import java.util.Map;
import com.alibaba.fastjson.JSON;

public class StuAnswerDTO implements Serializable{
	
	private static final long serialVersionUID = 2978140317769474100L;
	
	private String testpaperId; // 试卷号
	private String stuId; // 学号
	private Map<String, String> questionStuAnswerMap; //  题目id与学生答案的映射  <questionId, stuAnswer>
	
	
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


	public String getStuId() {
		return stuId;
	}


	public void setStuId(String stuId) {
		this.stuId = stuId;
	}


	public Map<String, String> getQuestionStuAnswerMap() {
		return questionStuAnswerMap;
	}


	public void setQuestionStuAnswerMap(Map<String, String> questionStuAnswerMap) {
		this.questionStuAnswerMap = questionStuAnswerMap;
	}


}
