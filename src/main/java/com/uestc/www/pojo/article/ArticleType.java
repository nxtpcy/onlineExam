package com.uestc.www.pojo.article;

import java.util.Date;

public class ArticleType {
    private Integer id;

    private String articleTypeId;

    private String articleTypeName;

    private String articleTypeDescribe;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticleTypeId() {
        return articleTypeId;
    }

    public void setArticleTypeId(String articleTypeId) {
        this.articleTypeId = articleTypeId == null ? null : articleTypeId.trim();
    }

    public String getArticleTypeName() {
        return articleTypeName;
    }

    public void setArticleTypeName(String articleTypeName) {
        this.articleTypeName = articleTypeName == null ? null : articleTypeName.trim();
    }

    public String getArticleTypeDescribe() {
        return articleTypeDescribe;
    }

    public void setArticleTypeDescribe(String articleTypeDescribe) {
        this.articleTypeDescribe = articleTypeDescribe == null ? null : articleTypeDescribe.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}