package com.uestc.www.service.impl.role;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.IDGenerator;
import com.uestc.www.common.IdType;
import com.uestc.www.common.QueryBase;
import com.uestc.www.dao.role.RoleMapper;
import com.uestc.www.pojo.role.Role;
import com.uestc.www.service.role.RoleService;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private IDGenerator iDGenerator;
	
	@Override
	public int update(Role role) {
		// TODO Auto-generated method stub
		role.setModifyTime(new Date());
		int rows = roleMapper.updateByRoleIdSelective(role);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int add(Role role) {
		// TODO Auto-generated method stub
		long roleCount = iDGenerator.nextId();
		String roleId = IdType.Role.getSign() + roleCount;
		role.setRoleId(roleId);
		role.setCreateTime(new Date());
		role.setModifyTime(new Date());
		int rows = roleMapper.insertSelective(role);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int delete(String roleId) {
		// TODO Auto-generated method stub
		int rows = roleMapper.deleteByRoleId(roleId);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
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
		Long rows = roleMapper.queryRows(queryBase);
		List<Role> results = roleMapper.selectBySelective(queryBase);
		queryBase.setResults(results);
		queryBase.setTotalRow(rows);
		return queryBase;
	}

}
