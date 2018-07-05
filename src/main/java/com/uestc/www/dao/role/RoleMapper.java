package com.uestc.www.dao.role;

import java.util.List;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.role.Role;


public interface RoleMapper {
	
    int deleteByPrimaryKey(Integer id);
    
    int deleteByRoleId(String roleId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);
    
    Role selectByRoleId(String roleId);
    
    List<Role> selectBySelective(QueryBase paramQueryBase);

    int updateByPrimaryKeySelective(Role record);
    
    int updateByRoleIdSelective(Role record);

    int updateByPrimaryKey(Role record);
    
    long queryRows(QueryBase paramQueryBase);
}