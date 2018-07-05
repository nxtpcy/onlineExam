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
import com.uestc.www.pojo.dto.exam.TestpaperDTO;
import com.uestc.www.pojo.exam.ExamTestpaper;
import com.uestc.www.service.exam.TestpaperService;

@Controller
@RequestMapping(value = "/admin/testpaper")
public class TestpaperController {  //试卷状态state为0表示未发布,  1表示已发布，2表示废卷 
	
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TestpaperService testpaperService;
	

	/**
	 * 按试卷id，试卷名，科目id，状态查询试卷（带分页，各查询字段均可选  ）
	 * 
	 */
	@RequestMapping(value = "/selectTestpaperByPageAndSelections", method = RequestMethod.POST)
	@ResponseBody
	public Object selectTestpaperByPageAndSelections(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("subjectId", map.get("subjectId"));
			queryBase.addParameter("testpaperName", map.get("testpaperName"));
			queryBase.addParameter("testpaperId", map.get("testpaperId"));
			queryBase.addParameter("state", map.get("state"));
			queryBase.setPageSize(Long.parseLong(map.get("rows").toString()));
			queryBase
					.setCurrentPage(Long.parseLong(map.get("page").toString()));
			testpaperService.selectTestpaperByPageAndSelections(queryBase);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResults());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用TestpaperController.selectTestpaperByPageAndSelections出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}
	
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody //方法返回值的Java对象可通过HttpMessageConverter转换为HttpOutputMessage，进而转为一个response返回给客户端 （body里是json对象的形式 ）
	public Object add(HttpServletRequest request, HttpServletResponse response,
			@RequestBody  TestpaperDTO testpaperDTO) {
		try {
			
			int status = testpaperService.add(testpaperDTO);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(
					"调用TestpaperController.add出错,testpaperDTO={},error={}",
					new Object[] { testpaperDTO, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}
	
	// 更新试卷名称、状态、开始考试时间、结束考试时间等字段
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response, @RequestBody ExamTestpaper examTestpaper) {
		try {
			
			int status = testpaperService.update(examTestpaper);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(
					"调用TestpaperController.update出错,examTestpaper={},error={}",
					new Object[] { examTestpaper, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}
	
	//  只能对还未发布的试卷进行题目组成关系的变动，如向该试卷再添加题目或删除题目，注意同时要更新试卷总分  
	// 是一次性传所有新的题号，还是分成向试卷添加题目和删除题目两个操作来完成？
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}