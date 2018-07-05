package com.uestc.www.dao.access;

import com.uestc.www.pojo.access.Access;

public interface AccessMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Access record);

    int insertSelective(Access record);

    Access selectByPrimaryKey(Integer id);
    
    Access selectByUserId(String userId);

    int updateByPrimaryKeySelective(Access record);

    int updateByPrimaryKey(Access record);
    
    int updateByUserIdSelective(Access record);
}