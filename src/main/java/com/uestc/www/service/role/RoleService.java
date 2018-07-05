package com.uestc.www.service.role;

import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.role.Role;

public interface RoleService {

	int update(Role role);

	int add(Role role);

	int delete(String roleId);

	abstract QueryBase queryBySelective(Map<String, Object> paramMap);
}
