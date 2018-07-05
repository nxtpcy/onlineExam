package com.uestc.www.service.impl.base;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.Status;
import com.uestc.www.common.StatusType;
import com.uestc.www.dao.access.AccessMapper;
import com.uestc.www.pojo.access.Access;
import com.uestc.www.service.base.ShareService;

@Service("ShareService")
public class ShareServiceImpl implements ShareService {

	@Autowired
	private AccessMapper accessMapper;
	
	@Override
	public int updatePassword(String userId, String newPwd, String oldPwd) {
		// TODO Auto-generated method stub
		Access access = this.accessMapper.selectByUserId(userId);
		if(access == null || !oldPwd.equals(access.getPassword())) {
			return Status.ERROR;
		}
		access.setPassword(newPwd);
		return updateAccess(access);
	}

	@Override
	public int addAccess(Access access) {
		// TODO Auto-generated method stub
		int rows = accessMapper.insertSelective(access);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int deleteAccess(String userId) {
		// TODO Auto-generated method stub
		Access access = accessMapper.selectByUserId(userId);
		if(access != null) {
			access.setStatus(1);
			access.setModifyTime(new Date());
			int rows = accessMapper.updateByUserIdSelective(access);
			return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
		} 
		return StatusType.OBJECT_NULL.getValue();
	}

	@Override
	public int updateAccess(Access access) {
		// TODO Auto-generated method stub
		int rows = this.accessMapper.updateByUserIdSelective(access);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}
}
