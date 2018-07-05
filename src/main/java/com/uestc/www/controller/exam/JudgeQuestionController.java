package com.uestc.www.controller.exam;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.uestc.www.pojo.exam.ExamJudgeQuestion;
import com.uestc.www.service.exam.JudgeQuestionService;


@Controller
@RequestMapping("/admin/judgeQuestion/")
public class JudgeQuestionController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	//注入judgeQuestionService
	@Autowired
	private JudgeQuestionService judgeQuestionService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody //方法返回值的Java对象可通过HttpMessageConverter转换为HttpOutputMessage，进而转为一个response返回给客户端 （body里是json对象的形式 ）
	public Object add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody  ExamJudgeQuestion examJudgeQuestion) {
		try {
			int status = judgeQuestionService.add(examJudgeQuestion);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(
					"调用JudgeQuestionController.add出错,examJudgeQuestion={},error={}",
					new Object[] { examJudgeQuestion, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response, @RequestBody ExamJudgeQuestion examJudgeQuestion) {
		try {
			int status = judgeQuestionService.update(examJudgeQuestion);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(
					"调用JudgeQuestionController.update出错,examJudgeQuestion={},error={}",
					new Object[] { examJudgeQuestion, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}

	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response, @RequestBody ExamJudgeQuestion examJudgeQuestion) {
		try {
			int status = judgeQuestionService.delete(examJudgeQuestion);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("JudgeQuestionController.delete出错,examJudgeQuestion={},error={}",
					new Object[] { examJudgeQuestion, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}
	
	//按科目ID分页查询选择题题库中的题目 
	@RequestMapping(value = "selectJudgeByPageAndSubjectId", method = RequestMethod.POST)
	@ResponseBody
	public Object selectJudgeByPageAndSubjectId(HttpServletRequest request,
			HttpServletResponse response, @RequestBody Map<String, Object> map) {
		try {
			QueryBase querybase = new QueryBase();
			querybase.setPageSize(Long.parseLong(map.get("rows").toString()));
			querybase.setCurrentPage(Long.parseLong(map.get("page").toString()));	
			judgeQuestionService.selectJudgeQuestionByPageAndSubjectId(querybase);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", querybase.getTotalRow());
			result.put("rows", querybase.getResults());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用JudgeQuestionController.selectJudgeByPageAndSubjectId出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}
	
}