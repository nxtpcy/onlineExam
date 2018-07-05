package com.uestc.www.service.impl.resource;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.www.common.QueryBase;
import com.uestc.www.dao.resource.ResourceMapper;
import com.uestc.www.pojo.resource.Resource;
import com.uestc.www.service.resource.ResourceService;

@Service("ResourceService")
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceMapper resoureceMapper;
	
	@Override
	public int update(Resource resource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(Resource resource) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String resourceId) {
		// TODO Auto-generated method stub
		return 0;
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
		Long rows = resoureceMapper.queryRows(queryBase);
		List<Resource> results = resoureceMapper.selectBySelective(queryBase);
		queryBase.setResults(results);
		queryBase.setTotalRow(rows);
		return queryBase;
	}

}
