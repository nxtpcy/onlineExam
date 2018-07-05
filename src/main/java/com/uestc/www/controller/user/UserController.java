package com.uestc.www.controller.user;

import java.util.Date;
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

import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;
import com.uestc.www.pojo.user.User;
import com.uestc.www.service.base.ShareService;
import com.uestc.www.service.user.UserService;

@Controller
@RequestMapping("/user/")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Autowired
	private ShareService shareService;

	
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1", "2" }, logical = Logical.OR)
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			User user = userService.query(userId);
			if (user == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			return user;
		} catch (Exception e) {
			this.logger.error("调用UserController.query出错,error={}", e);
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	// 用户更新自身信息
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1", "2" }, logical = Logical.OR)
	public Object update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			User user = userService.query(userId);
			if (user == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			if (paramMap.get("telephone") != null) {
				user.setTelephone((String) paramMap.get("telephone"));
			}
			if (paramMap.get("email") != null) {
				user.setEmail((String) paramMap.get("email"));
			}
			if (paramMap.get("userAge") != null) {
				user.setUserAge(Integer.valueOf(String.valueOf(paramMap.get("userAge"))));
			}
			user.setModifyTime(new Date());
			int status = this.userService.updateSelf(user);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用UserController.update出错,error={}", e);
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	// 用户更新密码
	@RequestMapping(value = { "/updatePassword" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Object updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId,
			@RequestParam String newPwd, @RequestParam String oldPwd) {
		StatusType status = StatusType.ERROR;
		try {
			int rows = shareService.updatePassword(userId, newPwd, oldPwd);
			if (rows == StatusType.SUCCESS.getValue())
				status = StatusType.SUCCESS;
		} catch (Exception e) {
			this.logger.error("对用户<{}>更改密码错误, e={}", userId, e);
		}
		return new Response(status.getValue(), status.getMessage());
	}

	// 用户注册
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public Object register(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			if (userId == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户名不能为空");
			}
			User user = userService.query(userId);
			if (user != null) {
				return new Response(StatusType.EXISTS.getValue(), "用户已存在");
			}
			if (paramMap.get("password") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("userNum") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("userName") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			if (paramMap.get("userDeptId") == null) {
				return new Response(StatusType.PARAMETER_IS_NULL.getValue(), "用户所需信息不完整");
			}
			int status = this.userService.add(paramMap);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用UserController.register出错,error={}", e);
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

}
