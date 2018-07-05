package com.uestc.www.pojo.dto.admin;

import java.util.Date;

public class AdminDTO {

	private Integer id;

    private String userId;

    private String adminName;

    private String adminNum;

    private String adminDeptId;

    private Integer status;

    private String telephone;
    
    private String userRole;
    
    private String description;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public String getAdminNum() {
        return adminNum;
    }

    public void setAdminNum(String adminNum) {
        this.adminNum = adminNum == null ? null : adminNum.trim();
    }

    public String getAdminDeptId() {
        return adminDeptId;
    }

    public void setAdminDeptId(String adminDeptId) {
        this.adminDeptId = adminDeptId == null ? null : adminDeptId.trim();
    }

    public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
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
