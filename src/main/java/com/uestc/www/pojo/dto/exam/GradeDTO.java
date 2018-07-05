package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import java.util.Date;
import com.alibaba.fastjson.JSON;

public class GradeDTO implements Serializable{

	private static final long serialVersionUID = 3337788666550775048L;
	
	private Integer id;
	private String stuId;
	private String testpaperId;  //  试卷号
	private String testpaperName; // 试卷名称
	private Integer totalScore;  // 试卷总分 
	private String subjectId; // 科目ID
	private String subjectName; // 科目名称
	private Date startTime; // 考试开始时间
	private Date endTime; // 考试结束时间
	private Integer stuGrade; // 学生所得成绩
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getTestpaperId() {
		return testpaperId;
	}

	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}

	public String getTestpaperName() {
		return testpaperName;
	}

	public void setTestpaperName(String testpaperName) {
		this.testpaperName = testpaperName;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
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

	public Integer getStuGrade() {
		return stuGrade;
	}

	public void setStuGrade(Integer stuGrade) {
		this.stuGrade = stuGrade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
