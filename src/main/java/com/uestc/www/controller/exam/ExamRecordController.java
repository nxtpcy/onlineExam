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
import com.uestc.www.service.exam.ExamRecordService;

@Controller
@RequestMapping(value = "/user/record")
public class ExamRecordController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExamRecordService examRecordService;

	/**
	 * 学生或管理员从成绩列表中点击某一套已考过的试卷，
	 * 可依次查看每道题目学生的作答以及题目正确答案及题目解析，
	 * 类似牛客网的流程 
	 * 
	 */
	@RequestMapping(value = "/selectRecordByPageAndStuIdAndTestpaperId", method = RequestMethod.POST)
	@ResponseBody
	public Object selectRecordByPageAndStuIdAndTestpaperId(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("stuId", map.get("stuId"));
			queryBase.addParameter("testpaperId", map.get("testpaperId"));
			queryBase.setPageSize(Long.parseLong(map.get("rows").toString()));
			queryBase
					.setCurrentPage(Long.parseLong(map.get("page").toString()));
			examRecordService.selectRecordByStuIdAndTestpaperIdByPage(queryBase);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResultMap());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用ExamRecordController.selectRecordByPageAndStuIdAndTestpaperId出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}	
	
}