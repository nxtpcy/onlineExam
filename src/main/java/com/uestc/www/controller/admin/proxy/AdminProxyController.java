package com.uestc.www.controller.admin.proxy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
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
import com.uestc.www.pojo.admin.Admin;
import com.uestc.www.service.admin.AdminService;
import com.uestc.www.service.base.ShareService;

@Controller
@RequestMapping("/admin/adminproxy")
public class AdminProxyController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ShareService shareService;

	/** 查询管理员自身信息(单条查询) **/
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1" }, logical = Logical.OR)
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			Admin admin = adminService.query(userId);
			if (admin == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			return admin;
		} catch (Exception e) {
			this.logger.error("调用AdminProxyController.query出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 有选择性获取管理员信息 **/
	@RequestMapping(value = "querySelective", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object querySelective(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			QueryBase queryBase = adminService.queryBySelective(paramMap);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryBase.getResults());
			result.put("total", queryBase.getTotalRow());
			return result;
		} catch (Exception e) {
			this.logger.error("调用AdminProxyController.querySelective出错,paramMap={},error={}",
					new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 添加普通管理员 **/
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			if (userId == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户名不能为空");
			}
			Admin admin = adminService.query(userId);
			if (admin != null) {
				return new Response(StatusType.EXISTS.getValue(), "用户已存在");
			}
			if (paramMap.get("password") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("adminNum") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("adminName") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("adminDeptId") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("userRole") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			int status = adminService.add(paramMap);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用AdminProxyController.add出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 更新普通管理員信息(超级管理员更新普通管理员信息) **/
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			Admin admin = adminService.query(userId);
			if (admin == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			int status = adminService.update(paramMap);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用AdminProxyController.update出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 更新普通管理員信息(普通管理员更新自身信息) **/
	@RequestMapping(value = "updateself", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1" }, logical = Logical.OR)
	public Object updateSelf(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			Admin admin = adminService.query(userId);
			if (admin == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			if (paramMap.get("telephone") != null) {
				admin.setTelephone((String) paramMap.get("telephone"));
			}
			admin.setModifyTime(new Date());
			int status = adminService.updateSelf(admin);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用AdminProxyController.updateSelf出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 超级管理员删除管理员信息 **/
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object delete(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			int status = adminService.delete(userId);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用AdminProxyController.delete出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	// 用户更新密码
	@RequestMapping(value = { "/updatePassword" }, method = RequestMethod.POST)
	@ResponseBody
	public Object updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId,
			@RequestParam String newPwd, @RequestParam String oldPwd) {
		StatusType status = StatusType.ERROR;
		try {
			int rows = shareService.updatePassword(userId, newPwd, oldPwd);
			if (rows == StatusType.SUCCESS.getValue())
				status = StatusType.SUCCESS;
		} catch (Exception e) {
			this.logger.error("AdminProxyController.updatePassword对用户<{}>更改密码错误, e={}", userId, e);
		}
		return new Response(status.getValue(), status.getMessage());
	}
}
