package com.uestc.www.dao.department;

import java.util.List;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.department.Department;

public interface DepartmentMapper {
	
    int deleteByPrimaryKey(Integer id);

    int deleteByDeptId(String deptId);
    
    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer id);
    
    List<Department> selectBySelective(QueryBase paramQueryBase);

    int updateByPrimaryKeySelective(Department record);
    
    int updateByDeptIdSelective(Department record);

    int updateByPrimaryKey(Department record);
    
    long queryRows(QueryBase paramQueryBase);
}