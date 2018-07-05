package com.uestc.www.service.impl.access;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.StatusType;
import com.uestc.www.dao.access.AccessMapper;
import com.uestc.www.pojo.access.Access;
import com.uestc.www.service.access.AccessService;

@Service("AccessService")
public class AccessServiceImpl implements AccessService {

	@Autowired
	private AccessMapper mapper;
	
	@Override
	public Access query(String userId) {
		// TODO Auto-generated method stub
		Access access = mapper.selectByUserId(userId);
		return access;
	}

	@Override
	public int update(Access access) {
		// TODO Auto-generated method stub
		if (access == null || access.getUserId() == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		int rows = mapper.updateByUserIdSelective(access);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int add(Access access) {
		// TODO Auto-generated method stub
		int rows = mapper.insertSelective(access);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int delete(String userId) {
		// TODO Auto-generated method stub
		Access access = mapper.selectByUserId(userId);
		if(access != null) {
			access.setStatus(1);
			access.setModifyTime(new Date());
			int rows = mapper.updateByUserIdSelective(access);
			return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
		} 
		return StatusType.OBJECT_NULL.getValue();
	}
	
}
