package com.uestc.www.pojo.exam;

import java.util.Date;

public class ExamQuestionMapping {
    private Integer id;

    private String questionId;

    private String testpaperId;

    private Date createTime;

    private Date updateTime;
    

    public ExamQuestionMapping(String questionId, String testpaperId, Date createTime, Date updateTime) {
		super();
		this.questionId = questionId;
		this.testpaperId = testpaperId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public ExamQuestionMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId == null ? null : questionId.trim();
    }

    public String getTestpaperId() {
        return testpaperId;
    }

    public void setTestpaperId(String testpaperId) {
        this.testpaperId = testpaperId == null ? null : testpaperId.trim();
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