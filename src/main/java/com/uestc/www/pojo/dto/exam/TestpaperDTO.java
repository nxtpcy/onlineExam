package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.uestc.www.pojo.exam.ExamTestpaper;

public class TestpaperDTO extends ExamTestpaper implements Serializable{

	
	private static final long serialVersionUID = 134748518759299323L;

	private String testpaperName;
	private String subjectId; // 科目ID
	private String subjectName; // 科目名称
	private Date startTime; // 考试开始时间
	private Date endTime; // 考试结束时间
	private List<String> questionIdList; //该份试卷含有的题目所对应的题目ID集合 
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getTestpaperName() {
		return testpaperName;
	}

	public void setTestpaperName(String testpaperName) {
		this.testpaperName = testpaperName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<String> getQuestionIdList() {
		return questionIdList;
	}

	public void setQuestionIdList(List<String> questionIdList) {
		this.questionIdList = questionIdList;
	}

	

}
