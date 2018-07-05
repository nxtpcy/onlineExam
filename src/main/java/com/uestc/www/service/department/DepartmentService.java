package com.uestc.www.service.department;

import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.department.Department;

public interface DepartmentService {
	
	int update(Department dept);

	int add(Department dept);

	int delete(String deptId);

	abstract QueryBase queryBySelective(Map<String, Object> paramMap);
}
