package com.uestc.www.controller.admin.resource;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.www.common.QueryBase;
import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;
import com.uestc.www.service.resource.ResourceService;

@Controller
@RequestMapping("/admin/resource/")
public class ResourceController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResourceService resourceService;

	/** 遍历资源 **/
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			QueryBase queryBase = resourceService.queryBySelective(paramMap);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryBase.getResults());
			result.put("total", queryBase.getTotalRow());
			return result;
		} catch (Exception e) {
			this.logger.error("调用ResourceController.query出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

}
