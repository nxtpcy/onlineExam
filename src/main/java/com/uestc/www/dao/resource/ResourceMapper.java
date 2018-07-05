package com.uestc.www.dao.resource;

import java.util.List;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.resource.Resource;

public interface ResourceMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Integer id);
    
    List<Resource> selectBySelective(QueryBase paramQueryBase);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);
    
    long queryRows(QueryBase paramQueryBase);
}