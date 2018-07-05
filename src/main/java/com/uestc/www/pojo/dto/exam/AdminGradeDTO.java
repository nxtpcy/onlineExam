package com.uestc.www.pojo.dto.exam;

import java.io.Serializable;
import java.util.Date;
import com.alibaba.fastjson.JSON;

// 管理员查看学生成绩时应该一并显示成绩信息及该学生的基本个人信息 
public class AdminGradeDTO implements Serializable{

	private static final long serialVersionUID = -6792420336056705344L;
	
	// 成绩信息
	private Integer id;
	private String testpaperId;  //  试卷号
	private String testpaperName; // 试卷名称
	private Integer totalScore;  // 试卷总分 
	private String subjectId; // 科目ID
	private String subjectName; // 科目名称
	private Date startTime; // 考试开始时间
	private Date endTime; // 考试结束时间
	private Integer stuGrade; // 学生所得成绩
	
	
	//该学生的基本个人信息
	private String userId; //登录账号
	private String stuId; // 学号
    private String userName;  //姓名
    private String userDeptId; //部门（学院）号
    private String userDeptName; // 部门名称
    private Integer userAge; //年龄
    private String telephone;  //电话
    private String email;  //电邮
	
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}	

	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserDeptId() {
		return userDeptId;
	}



	public void setUserDeptId(String userDeptId) {
		this.userDeptId = userDeptId;
	}



	public String getUserDeptName() {
		return userDeptName;
	}



	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}



	public Integer getUserAge() {
		return userAge;
	}



	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}



	public String getTelephone() {
		return telephone;
	}



	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
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
