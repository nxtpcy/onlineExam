package com.uestc.www.common;


public enum AccessType {

	NORMAL(0,"正常登录"),
    FORBID(1,"禁止登录");
   
	private int value;
	private String desc;
	
	private AccessType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getVal() {
		return this.value;
	}

	public String getDesc() {
		return this.desc;
	}

	public static AccessType getAccessTypeByValue(int value) {
		AccessType[] types = AccessType.values();
		for (AccessType type : types) {
			if (type.getVal() == value)
				return type;
		}
		return null;
	}
}
