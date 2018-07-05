package com.uestc.www.service.impl.user;

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
import com.uestc.www.dao.user.UserMapper;
import com.uestc.www.pojo.access.Access;
import com.uestc.www.pojo.user.User;
import com.uestc.www.service.base.ShareService;
import com.uestc.www.service.user.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private AccessMapper accessMapper;

	@Autowired
	private ShareService shareService;

	@Autowired
	TransactionTemplate transactionTemplate;

	@Override
	public User query(String userId) {
		// TODO Auto-generated method stub
		User user = this.userMapper.selectByUserId(userId);
		return user;
	}

	@Override
	public int update(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Integer result = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				String userId = (String) paramMap.get("userId");
				if (userId == null)
					return StatusType.OBJECT_NULL.getValue();
				User user = userMapper.selectByUserId(userId);
				if (paramMap.get("userNum") != null)
					user.setUserNum((String) paramMap.get("userNum"));
				if (paramMap.get("userName") != null)
					user.setUserName((String) paramMap.get("userName"));
				if (paramMap.get("userDeptId") != null)
					user.setUserDeptId((String) paramMap.get("userDeptId"));
				if (paramMap.get("status") != null)
					user.setStatus(Integer.valueOf(paramMap.get("status").toString()));
				if (paramMap.get("userAge") != null)
					user.setUserAge((int) paramMap.get("userAge"));
				if (paramMap.get("telephone") != null)
					user.setTelephone((String) paramMap.get("telephone"));
				if (paramMap.get("email") != null)
					user.setEmail((String) paramMap.get("email"));
				user.setModifyTime(new Date());

				Access access = accessMapper.selectByUserId(userId);
				if (paramMap.get("status") != null)
					access.setStatus(Integer.valueOf(paramMap.get("status").toString()));
				access.setModifyTime(new Date());
				int result = StatusType.ERROR.getValue();
				try {
					result = userMapper.updateByUserIdSelective(user);
					result = shareService.updateAccess(access);
				} catch (Exception e) {
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
	public int updateSelf(User user) {
		// TODO Auto-generated method stub
		if (user == null || user.getUserId() == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		int rows = this.userMapper.updateByUserIdSelective(user);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int add(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		Integer result = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				String userId = (String) paramMap.get("userId");
				if (userId == null)
					return StatusType.OBJECT_NULL.getValue();
				User user = new User();
				user.setUserId(userId);
				user.setUserNum((String) paramMap.get("userNum"));
				user.setUserName((String) paramMap.get("userName"));
				user.setUserAge((int) paramMap.get("userAge"));
				user.setUserDeptId((String) paramMap.get("userDeptId"));
				user.setTelephone((String) paramMap.get("telephone"));
				user.setStatus(0);
				user.setEmail((String) paramMap.get("email"));
				user.setCreateTime(new Date());
				user.setModifyTime(new Date());

				Access access = new Access();
				access.setUserId(userId);
				access.setPassword((String) paramMap.get("password"));
				access.setStatus(0);
				access.setUserRole("2");
				access.setDescription("学生用户");
				access.setCreateTime(new Date());
				access.setModifyTime(new Date());
				int result = StatusType.ERROR.getValue();
				try {
					result = userMapper.insertSelective(user);
					result = shareService.addAccess(access);
				} catch (Exception e) {
					logger.error("UserService add方法出错，e={}", e);
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
		User user = this.userMapper.selectByUserId(userId);
		if (user == null)
			return StatusType.OBJECT_NULL.getValue();
		Integer result = transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				user.setStatus(1);
				user.setModifyTime(new Date());
				int result = StatusType.ERROR.getValue();
				try {
					result = userMapper.updateByUserIdSelective(user);
					result = shareService.deleteAccess(userId);
				} catch (Exception e) {
					logger.error("UserService delete方法出错，e={}", e);
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
		Long rows = this.userMapper.queryRows(queryBase);
		List<User> results = this.userMapper.selectBySelective(queryBase);
		queryBase.setResults(results);
		queryBase.setTotalRow(rows);
		return queryBase;
	}

}
