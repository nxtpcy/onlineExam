package com.uestc.www.controller.admin.role;

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
import com.uestc.www.pojo.role.Role;
import com.uestc.www.service.role.RoleService;

@Controller
@RequestMapping("/admin/role/")
public class RoleController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleService roleService;

	/** 遍历角色 or 有选择遍历角色 **/
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			QueryBase queryBase = roleService.queryBySelective(paramMap);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryBase.getResults());
			result.put("total", queryBase.getTotalRow());
			return result;
		} catch (Exception e) {
			this.logger.error("调用RoleController.query出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 更新角色 **/
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object update(HttpServletRequest request, HttpServletResponse response, @RequestBody Role role) {
		try {
			int status = roleService.update(role);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用RoleController.update出错,role={},error={}", new Object[] { role, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 创建新角色 **/
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object add(HttpServletRequest request, HttpServletResponse response, @RequestBody Role role) {
		try {
			int status = roleService.add(role);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用RoleController.add出错,role={},error={}", new Object[] { role, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
}
