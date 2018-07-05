package com.uestc.www.controller.admin.article;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uestc.www.common.Response;
import com.uestc.www.common.StatusType;

public class ArticleController {

	/**获取文章信息 or 有条件获取文章信息**/
	@RequestMapping(value = "queryArticle", method = RequestMethod.POST)
	@ResponseBody
	public Object queryArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			
		} catch (Exception e) {
			
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
	
	/**更新文章信息**/
	@RequestMapping(value = "updateArticle", method = RequestMethod.POST)
	@ResponseBody
	public Object updateArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			
		} catch (Exception e) {
			
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
	
	/**添加文章信息**/
	@RequestMapping(value = "addArticle", method = RequestMethod.POST)
	@ResponseBody
	public Object addArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			
		} catch (Exception e) {
			
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
	
	/**删除文章信息**/
	@RequestMapping(value = "deleteArticle", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> paramMap) {
		try {
			
		} catch (Exception e) {
			
		}
		return new Response(StatusType.EXCEPTION.getValue(), StatusType.EXCEPTION.getMessage());
	}
}
