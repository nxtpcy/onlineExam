package com.uestc.www.pojo.exam;

import java.util.Date;

public class ExamRecord {
    private Integer id;

    private String stuId;

    private String questionId;

    private String testpaperId;

    private String stuAnswer;

    private Integer stuScore;

    private Date createTime;

    private Date updateTime;
    

    public ExamRecord() {}

	public ExamRecord(String stuId, String questionId, String testpaperId, String stuAnswer, Integer stuScore,
			Date createTime, Date updateTime) {
		super();
		this.stuId = stuId;
		this.questionId = questionId;
		this.testpaperId = testpaperId;
		this.stuAnswer = stuAnswer;
		this.stuScore = stuScore;
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

    public String getStuAnswer() {
        return stuAnswer;
    }

    public void setStuAnswer(String stuAnswer) {
        this.stuAnswer = stuAnswer == null ? null : stuAnswer.trim();
    }

    public Integer getStuScore() {
        return stuScore;
    }

    public void setStuScore(Integer stuScore) {
        this.stuScore = stuScore;
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