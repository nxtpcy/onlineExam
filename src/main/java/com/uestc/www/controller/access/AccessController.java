package com.uestc.www.controller.access;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.www.common.Constants;
import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;
import com.uestc.www.pojo.access.Access;
import com.uestc.www.service.access.AccessService;


@Controller
@RequestMapping("/access/")
public class AccessController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccessService service;

	@RequestMapping(value = { "/login" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Object login(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId,
			@RequestParam String password, @RequestParam(required = false) String imageValue) {
		// 检查图片验证码
		/**
		 * String kaptchaExpected = (String)
		 * request.getSession().getAttribute("KAPTCHA_SESSION_KEY"); if
		 * ((imageValue == null) || (!imageValue.equals(kaptchaExpected))) {
		 * return new Response(StatusType.KAPTCHA_ERROR.getValue(),
		 * StatusType.KAPTCHA_ERROR.getMessage()); }
		 **/

		StatusType status = StatusType.ERROR;
		// 登录验证
		UsernamePasswordToken token = new UsernamePasswordToken(userId, password);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(token);

			if (currentUser.isAuthenticated()) {
				this.logger.info(userId + " login success");
				status = StatusType.SUCCESS;
				return new Response(status.getValue(), status.getMessage());
			}
		} catch (UnknownAccountException e) {
			this.logger.error("对用户<{}.{}>进行登录验证..验证未通过,未知账户", new Object[] { userId, password }, e);
			token.clear();
			status = StatusType.USER_IS_NULL;
		} catch (IncorrectCredentialsException e) {
			this.logger.error("对用户<{}.{}>进行登录验证..验证未通过,错误的凭证", new Object[] { userId, password }, e);
			token.clear();
			status = StatusType.PASSWD_NOT_MATCH;
		} catch (AuthenticationException e) {
			this.logger.error("对用户<{}.{}>验证错误", new Object[] { userId, password }, e);
			status = StatusType.UNAUTHORIZED;
			token.clear();
		}
		return new Response(status.getValue(), status.getMessage());
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object update(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> paramMap) {
		try {
			String userId = (String) paramMap.get("userId");
			Access access = service.query(userId);
			if (access == null) {
				return new Response(StatusType.USER_IS_NULL.getValue(), StatusType.USER_IS_NULL.getMessage());
			}
			if (paramMap.get("userRole") != null) {
				access.setUserRole((String) paramMap.get("userRole"));
			}
			if (paramMap.get("description") != null) {
				access.setDescription((String) paramMap.get("description"));
			}
			if (paramMap.get("status") != null) {
				access.setStatus((int) paramMap.get("status"));
			}
			access.setModifyTime(new Date());
			int status = this.service.update(access);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用AccessController.update出错,error={}", e);
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = { "0", "1", "2"}, logical = Logical.OR)
	public Object logout(HttpServletRequest request) {
		int status = StatusType.SUCCESS.getValue();
		Object currentUser = request.getSession().getAttribute(Constants.CURRENT_USER);
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.logout();
			if (currentUser != null) {
				logger.info(currentUser + " logout success");
			}
			return new Response(status, StatusType.value(status).getMessage());
		} catch (Exception e) {
			logger.error("{} logout failed", currentUser, e);
			status = StatusType.ERROR.getValue();
			return new Response(status, StatusType.value(status).getMessage());
		}
	}
}
