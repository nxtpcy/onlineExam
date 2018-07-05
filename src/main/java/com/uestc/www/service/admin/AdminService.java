package com.uestc.www.service.admin;

import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.admin.Admin;

public interface AdminService {

	abstract Admin query(String userId); //得到Admin对象
	
	int update(Map<String, Object> paramMap);             //更新管理员资料(超级管理员更新普通管理员资料)
	
	int updateSelf(Admin admin);
	
	int resetPassword(String userId, String newPwd); //重置密码

	int add(Map<String, Object> paramMap);      //新增普通管理员
	
	int delete(String userId);            //删除普通管理员

	abstract QueryBase queryBySelective(Map<String, Object> paramMap);   //有选择获取普通管理员
	
}
