package com.uestc.www.common;

public enum StatusType {
	
    SUCCESS(1, "成功"), 
	STUDENTSUCCESS(2, "学生登录成功"),
	ADMINSUCCESS(3, "管理员登录成功"),
	ERROR(-1, "失败"), 
	EXCEPTION(-2, "系统操作异常"), 
	USER_IS_NULL(-3, "用户为空"), 
	OBJECT_NULL(-4, "对象为空"), 
	EXISTS(-5, "重复记录"), 
	NOT_EXISTS(-6, "记录不存在"), 
	PASSWD_NOT_MATCH(-7, "用户或密码错误"), 
	DATE_ERROR(-8,"日期不符合规范"), 
	DATA_PARSE_ERROR(-9, "数据解析错误"), 
	DATE_INVALID(-10,"不符合有效日期"),
	MONEY_INVALID(-11, "不符合有效的金额"), 
	PARAMETER_IS_NULL(-12,"参数为空"), 
	ROLE_INVALID(-13, "用户角色错误"), 
	UNAUTHORIZED(-14, "未登录"), 
	PERMISSION_DENIED(-15, "权限不足"), 
	OPERATION_FORBID(-16, "不允许的操作"), 
    KICKOUT(-17,"其他设备登录，你被强制退出"),
    DEADLINE_SUBMIT(-18, "当前提交时间已过期"),
    DATA_INVALID(-19, "输入数据不合法"), 
	JOB_NUM_INVALID(-20, "岗位数量设置无效"), 
	INFO_INCOMPLETE(-21, "银行名称、卡号、联系方式、电子邮件、学院等信息部分缺少，请完善"), 
	KAPTCHA_ERROR(-22, "验证码错误"),
	USER_INCOMPLETE_ON(-23, "用户信息不完善"), 
	EMAIL_INCOMPLETE(-24, "请先完善电子邮箱，保存信息失败"), 
	TELEPHONE_INCOMPLETE(-25, "请先完善联系方式，保存信息失败"), 
	DEPT_INCOMPLETE(-26, "请先完善所属学院信息，保存信息失败"),
	BANK_NAME_INCOMPLETE(-27, "请先完善银行名称信息，保存信息失败"), 
	BANK_ID_INCOMPLETE(-28,"请先完善银行卡号信息，保存信息失败"), 
	USER_INCOMPLETE_CLOSE(-29,"用户信息不完善，但强制完善选项关闭"),
	SURVEY_INCOMPLETE(-30, "问卷调查尚未完成"),
	FILE_UPLOAD_FAIL(-31, "文件上传失败"), 
	IMAGE_FILE_TYPE_UNMATCH(-32,"文件类型不匹配,只能上传.jpge|.jpg图片文件"), 
	FILE_MISSING(-33, "文件无法找到"), 
	FILE_TOO_MAX(-34, "文件超过指定大小1024K"),
	BEYOND_MAX_NUM(-35, "申请数量已达上限"),
	REPEAT_APPLICATION(-36, "已经是助管，不可再申请岗位"), 
	SURVEY_ANSWER_ERROR(-37,"无法提交问卷答案，请确认是否有权限或已做问卷"), 
	EXAM_TIMEOUT(-38,"答题时间过长，已超时"),
	THIRD_REVIEW(-39, "无法进行审核,请确认前两次审核是否已通过");

	private int value;    // 值
	private String message;   // 值对应的描述

	private StatusType(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setDesc(String message) {
		this.message = message;
	}
	
	public String toString() {
		return super.toString() + "(" + this.value + ", " + this.message + ")";
	}
	
	/**
	 * 根据值返回StatusType对象
	 * @param value
	 * @return
	 */
	public static StatusType value(int value) {
		StatusType[] statusTypes = values();
		StatusType dsType = null;
		for (int i = 0; i < statusTypes.length; i++) {
			 dsType = statusTypes[i];
			 if (value == dsType.getValue()) {
				 return dsType;
				 }
			 }
		return null;
	}
	
}
