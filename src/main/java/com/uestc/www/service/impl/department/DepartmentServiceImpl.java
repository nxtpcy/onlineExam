package com.uestc.www.service.impl.department;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.QueryBase;
import com.uestc.www.dao.department.DepartmentMapper;
import com.uestc.www.pojo.department.Department;
import com.uestc.www.service.department.DepartmentService;

@Service("DepartmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentMapper deptMapper;
	
	@Override
	public int update(Department dept) {
		// TODO Auto-generated method stub
		dept.setModifyTime(new Date());
		int rows = deptMapper.updateByDeptIdSelective(dept);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int add(Department dept) {
		// TODO Auto-generated method stub
		dept.setCreateTime(new Date());
		dept.setModifyTime(new Date());
		int rows = deptMapper.insertSelective(dept);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int delete(String deptId) {
		// TODO Auto-generated method stub
		int rows = deptMapper.deleteByDeptId(deptId);
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
		Long rows = deptMapper.queryRows(queryBase);
		List<Department> results = deptMapper.selectBySelective(queryBase);
		queryBase.setResults(results);
		queryBase.setTotalRow(rows);
		return queryBase;
	}

}
