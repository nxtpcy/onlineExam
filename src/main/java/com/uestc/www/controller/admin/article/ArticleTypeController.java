package com.uestc.www.controller.admin.article;

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
import com.uestc.www.pojo.article.ArticleType;
import com.uestc.www.service.article.ArticleTypeService;

@Controller
@RequestMapping("/admin/article/")
public class ArticleTypeController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ArticleTypeService articleTypeService;

	/** 遍历文章类型 or 有选择遍历文章类型 **/
	@RequestMapping(value = "queryType", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object query(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			QueryBase queryBase = articleTypeService.queryBySelective(paramMap);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("rows", queryBase.getResults());
			result.put("total", queryBase.getTotalRow());
			return result;
		} catch (Exception e) {
			this.logger.error("调用ArticleTypeController.query出错,paramMap={},error={}", new Object[] { paramMap, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 更新角色 **/
	@RequestMapping(value = "updateType", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArticleType articleType) {
		try {
			int status = articleTypeService.update(articleType);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用ArticleTypeController.update出错,articleType={},error={}",
					new Object[] { articleType, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}

	/** 创建新角色 **/
	@RequestMapping(value = "addType", method = RequestMethod.POST)
	@ResponseBody
	@RequiresRoles(value = "0")
	public Object add(HttpServletRequest request, HttpServletResponse response, @RequestBody ArticleType articleType) {
		try {
			int status = articleTypeService.add(articleType);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			this.logger.error("调用ArticleTypeController.add出错,articleType={},error={}", new Object[] { articleType, e });
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
}
