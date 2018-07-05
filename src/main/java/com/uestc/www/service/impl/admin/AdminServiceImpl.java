package com.uestc.www.service.impl.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.QueryBase;
import com.uestc.www.common.StatusType;
import com.uestc.www.dao.access.AccessMapper;
import com.uestc.www.dao.admin.AdminMapper;
import com.uestc.www.pojo.access.Access;
import com.uestc.www.pojo.admin.Admin;
import com.uestc.www.pojo.dto.admin.AdminDTO;
import com.uestc.www.service.admin.AdminService;
import com.uestc.www.service.base.ShareService;

@Service("AdminService")
public class AdminServiceImpl implements AdminService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private AccessMapper accessMapper;
	
	@Autowired
	private ShareService shareService;
	
	@Autowired
	TransactionTemplate transactionTemplate;
	
	@Override
	public Admin query(String userId) {
		// TODO Auto-generated method stub
		Admin admin = adminMapper.selectByUserId(userId);
		return admin;
	}


	@Override
	public int update(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Integer result = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				String userId = (String)paramMap.get("userId");
				if(userId == null)
					return StatusType.OBJECT_NULL.getValue();
				Admin admin = adminMapper.selectByUserId(userId);
				if(paramMap.get("adminNum") != null) 
					admin.setAdminNum((String)paramMap.get("adminNum"));
				if(paramMap.get("adminName") != null) 
					admin.setAdminName((String)paramMap.get("adminName"));
				if(paramMap.get("adminDeptId") != null) 
					admin.setAdminDeptId((String)paramMap.get("adminDeptId"));
				if(paramMap.get("status") != null) 
					admin.setStatus(Integer.valueOf(paramMap.get("status").toString()));
				if(paramMap.get("telephone") != null) 
					admin.setTelephone((String)paramMap.get("telephone"));
				admin.setModifyTime(new Date());
				
				Access access = accessMapper.selectByUserId(userId);
				if(paramMap.get("userRole") != null) 
					access.setUserRole((String)paramMap.get("adminName"));
				if(paramMap.get("description") != null) 
					admin.setAdminName((String)paramMap.get("description"));
				if(paramMap.get("status") != null) 
					access.setStatus(Integer.valueOf(paramMap.get("status").toString()));
				access.setModifyTime(new Date());
				
				int result = StatusType.ERROR.getValue();
				try {
					result = adminMapper.updateByUserIdSelective(admin);
					result = shareService.updateAccess(access);
				} catch(Exception e) {
					logger.error("AdminService update方法出错，e={}", e);
					status.setRollbackOnly();  
                    e.printStackTrace();  
				}
				return result;
			}
		});
		return result;
	}

	@Override
	public int resetPassword(String userId, String newPwd) {
		// TODO Auto-generated method stub
		Access access = accessMapper.selectByUserId(userId);
		access.setPassword(newPwd);
		return shareService.updateAccess(access);
	}

	@Override
	public int add(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Integer result = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				String userId = (String)paramMap.get("userId");
				if(userId == null)
					return StatusType.OBJECT_NULL.getValue();
				Admin admin = new Admin();
				admin.setUserId(userId);
				admin.setAdminNum((String)paramMap.get("adminNum"));
				admin.setAdminName((String)paramMap.get("adminName"));
				admin.setAdminDeptId((String)paramMap.get("adminDeptId"));
				if(paramMap.get("telephone") != null) 
					admin.setTelephone((String)paramMap.get("telephone"));
				admin.setStatus(0);
				admin.setCreateTime(new Date());
				admin.setModifyTime(new Date());
				
				Access access = new Access();
				access.setUserId(userId);
				access.setPassword((String)paramMap.get("password"));
				access.setStatus(0);
				access.setUserRole((String)paramMap.get("userRole"));
				if(paramMap.get("description") != null) 
					admin.setAdminName((String)paramMap.get("description"));
				access.setCreateTime(new Date());
				access.setModifyTime(new Date());
				int result = StatusType.ERROR.getValue();
				try {
					result = adminMapper.insertSelective(admin);
					result = shareService.addAccess(access);
				} catch(Exception e) {
					logger.error("AdminService add方法出错，e={}", e);
					status.setRollbackOnly();  
                    e.printStackTrace();  
				}
				return result;
			}
		});
		return result;
	}

	@Override
	public int delete(String userId) {
		// TODO Auto-generated method stub
		Admin admin = this.adminMapper.selectByUserId(userId);
		if(admin == null)
			return StatusType.OBJECT_NULL.getValue();
		Integer result = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				admin.setStatus(1);
				admin.setModifyTime(new Date());
				int result = StatusType.ERROR.getValue();
				try {
					result = adminMapper.updateByUserIdSelective(admin);
					result = shareService.deleteAccess(userId);
				} catch(Exception e) {
					logger.error("AdminService delete方法出错，e={}", e);
					status.setRollbackOnly();  
                    e.printStackTrace();  
				}
				return result;
			}
		});
		return result;
	}

	@Override
	public QueryBase queryBySelective(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		QueryBase queryBase = new QueryBase();
		if ((paramMap.get("page") != null) && (paramMap.get("rows") != null)) {
			long page = Long.parseLong(paramMap.get("page").toString());
			long rows = Long.parseLong(paramMap.get("rows").toString());
			queryBase.setPageSize(Long.valueOf(rows));
			queryBase.setCurrentPage(Long.valueOf(page));
		}
		queryBase.setParameters(paramMap);
		Long rows = this.adminMapper.queryRows(queryBase);
		List<AdminDTO> results = this.adminMapper.selectBySelective(queryBase);
		queryBase.setResults(results);
		queryBase.setTotalRow(rows);
		return queryBase;
	}


	@Override
	public int updateSelf(Admin admin) {
		// TODO Auto-generated method stub
		if (admin == null || admin.getUserId() == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		int rows = adminMapper.updateByUserIdSelective(admin);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

}
