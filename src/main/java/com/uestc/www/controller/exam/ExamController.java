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
import com.uestc.www.pojo.dto.exam.StuAnswerDTO;
import com.uestc.www.service.exam.ExamRecordService;
import com.uestc.www.service.exam.MappingService;

@Controller
@RequestMapping(value = "/user/exam")
public class ExamController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExamRecordService examRecordService;

	@Autowired
	private MappingService mappingService;
	
	/**
	 * 点击了试卷列表中的某一套试卷准备开始考试时   
	 */
	@RequestMapping(value = "/startExam", method = RequestMethod.POST)
	@ResponseBody
	public Object startExam(HttpServletRequest request,
			HttpServletResponse response, @RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("testpaperId", map.get("testpaperId"));
			
			mappingService.selectQuestionByTestpaperId(queryBase);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResultMap());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用ExamController.startExam出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}
	
	//提交试卷并更新分数，插入考试记录 
	@RequestMapping(value = "/submitPaper", method = RequestMethod.POST)
	@ResponseBody
	public Object submitPaper(HttpServletRequest request,
			HttpServletResponse response, @RequestBody StuAnswerDTO stuAnswerDTO) {
		try {
			int status = examRecordService.submitPaperAndUpdateGrade(stuAnswerDTO);
			String message = StatusType.value(status).getMessage();
			return new Response(status, message);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用ExamController.submitPaper出错,stuAnswerDTO={},error={}",
					new Object[] { stuAnswerDTO, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
	}
	
}