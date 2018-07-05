package com.uestc.www.dao.admin;

import java.util.List;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.admin.Admin;
import com.uestc.www.pojo.dto.admin.AdminDTO;

public interface AdminMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);
    
    long queryRows(QueryBase paramQueryBase);
    
    Admin selectByUserId(String userId);

    Admin selectByPrimaryKey(Integer id);
    
    List<AdminDTO> selectBySelective(QueryBase paramQueryBase);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
    
    int updateByUserIdSelective(Admin record);
}