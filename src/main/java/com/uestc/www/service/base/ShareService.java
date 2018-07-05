package com.uestc.www.service.base;

import com.uestc.www.pojo.access.Access;

public interface ShareService {

	int updatePassword(String userId, String newPwd, String oldPwd);
	
	int addAccess(Access access);
	
	int deleteAccess(String userId);
	
	int updateAccess(Access access);
}
