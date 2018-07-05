package com.uestc.www.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;
import com.uestc.www.service.admin.AdminService;

@Controller
@RequestMapping("/admin/")
public class AdminController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AdminService adminService;
	
	/** 重置密码 **/
	@RequestMapping(value = { "/resetPwd" }, method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object resetPwd(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId,
			@RequestParam String newPwd) {
		try {
			int status = adminService.resetPassword(userId, newPwd);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用AdminController.resetPwd出错,error={}", e);
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

}
