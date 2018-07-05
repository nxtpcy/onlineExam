package com.uestc.www.controller.admin.proxy;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.www.common.QueryBase;
import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;
import com.uestc.www.pojo.user.User;
import com.uestc.www.service.user.UserService;

@Controller
@RequestMapping("/admin/userproxy")
public class UserProxyController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	/** 更改用户信息 **/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1" }, logical = Logical.OR)
	public Object updateUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			User user = userService.query(userId);
			if (user == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			int status = userService.update(paramMap);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用UserProxyController.update出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 获取用户信息 or 有条件获取用户信息 **/
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1" }, logical = Logical.OR)
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			QueryBase queryBase = userService.queryBySelective(paramMap);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryBase.getResults());
			result.put("total", queryBase.getTotalRow());
			return result;
		} catch (Exception e) {
			this.logger.error("调用UserProxyController.querySelective出错,paramMap={},error={}",
					new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 删除用户 **/
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1" }, logical = Logical.OR)
	public Object deleteUser(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			int status = userService.delete(userId);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用UserProxyController.delete出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
}
