package com.uestc.www.service.access;

import com.uestc.www.pojo.access.Access;

public interface AccessService {

	Access query(String userId);  // 根据用户登录名得到权限对象
	
	int update(Access access);
	
	int add(Access access);
	
	int delete(String userId);
	
}
