package com.uestc.www.service.resource;

import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.resource.Resource;

public interface ResourceService {
	
	int update(Resource resource);

	int add(Resource resource);

	int delete(String resourceId);

	abstract QueryBase queryBySelective(Map<String, Object> paramMap);
}
