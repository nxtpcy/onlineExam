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
import com.uestc.www.pojo.exam.ExamSubject;
import com.uestc.www.service.exam.SubjectService;

@Controller
@RequestMapping(value = "/admin/subject")
public class SubjectController {  //科目可用时状态state为1,0表示不可用
	
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SubjectService subjectService;
	

	/**
	 * 按科目id，科目名，状态查询科目（带分页，各查询字段均可选 ）
	 * 
	 */
	@RequestMapping(value = "/selectSubjectByPageAndSelections", method = RequestMethod.POST)
	@ResponseBody
	public Object selectSubjectByPageAndSelections(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("subjectId", map.get("subjectId"));
			queryBase.addParameter("subjectName", map.get("subjectName"));
			queryBase.addParameter("state", map.get("state"));
			queryBase.setPageSize(Long.parseLong(map.get("rows").toString()));
			queryBase
					.setCurrentPage(Long.parseLong(map.get("page").toString()));
			subjectService.selectSubjectByPageAndSelections(queryBase);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResults());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用SubjectController.selectSubjectByPageAndSelections出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}
	
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody //方法返回值的Java对象可通过HttpMessageConverter转换为HttpOutputMessage，进而转为一个response返回给客户端 （body里是json对象的形式 ）
	public Object add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody  ExamSubject examSubject) {
		try {
			
			int status = subjectService.add(examSubject);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(
					"调用SubjectController.add出错,examSubject={},error={}",
					new Object[] { examSubject, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response, @RequestBody ExamSubject examSubject) {
		try {
			
			int status = subjectService.update(examSubject);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(
					"调用SubjectController.update出错,examSubject={},error={}",
					new Object[] { examSubject, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}
	
	
}