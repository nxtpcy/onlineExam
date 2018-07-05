package com.uestc.www.dao.user;

import java.util.List;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.user.User;

public interface UserMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    
    User selectByUserId(String userId);
    
    List<User> selectBySelective(QueryBase paramQueryBase);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int updateByUserIdSelective(User record);
    
    long queryRows(QueryBase paramQueryBase);
}