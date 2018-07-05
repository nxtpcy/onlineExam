package com.uestc.www.controller.admin.department;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.www.common.QueryBase;
import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;
import com.uestc.www.pojo.department.Department;
import com.uestc.www.service.department.DepartmentService;

@Controller
@RequestMapping("/admin/department/")
public class DepartmentController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DepartmentService deptService;

	/** 遍历部门 or 有选择遍历部门 **/
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			QueryBase queryBase = deptService.queryBySelective(paramMap);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryBase.getResults());
			result.put("total", queryBase.getTotalRow());
			return result;
		} catch (Exception e) {
			this.logger.error("调用DepartmentController.query出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 更新部门 **/
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object update(HttpServletRequest request, HttpServletResponse response, @RequestBody Department dept) {
		try {
			int status = deptService.update(dept);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用DepartmentController.update出错,department={},error={}", new Object[] { dept, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 创建新部门 **/
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Department dept) {
		try {
			int status = deptService.add(dept);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用DepartmentController.add出错,department={},error={}", new Object[] { dept, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
	
	/** 删除部门 **/
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object delete(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String deptId) {
		try {
			int status = deptService.delete(deptId);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用DepartmentController.delete出错,deptId={},error={}", new Object[] { deptId, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

}
