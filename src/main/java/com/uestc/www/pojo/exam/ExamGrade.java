package com.uestc.www.pojo.exam;

import java.util.Date;

public class ExamGrade {
    private Integer id;

    private String stuId;

    private String testpaperId;

    private Integer grade;

    private Date createTime;

    private Date updateTime;
    
    public ExamGrade() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public ExamGrade(String stuId, String testpaperId, Integer grade, Date createTime, Date updateTime) {
		super();
		this.stuId = stuId;
		this.testpaperId = testpaperId;
		this.grade = grade;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}



	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId == null ? null : stuId.trim();
    }

    public String getTestpaperId() {
        return testpaperId;
    }

    public void setTestpaperId(String testpaperId) {
        this.testpaperId = testpaperId == null ? null : testpaperId.trim();
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}