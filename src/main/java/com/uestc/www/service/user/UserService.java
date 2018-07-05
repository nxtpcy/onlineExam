package com.uestc.www.service.user;

import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.user.User;

public interface UserService {
	
	abstract User query(String userId);  // 根据用户登录名得到权限对象
	
	int update(Map<String, Object> paramMap);
	
	int updateSelf(User user);
			
	int add(Map<String, Object> paramMap);
	
	int delete(String userId);
	
	abstract QueryBase queryBySelective(Map<String, Object> paramMap);
}
