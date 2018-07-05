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
import com.uestc.www.service.exam.GradeService;

@Controller
@RequestMapping(value = "/user/grade")
public class UserGradeController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GradeService gradeService;
	
	
	/* 
	 * 学生查询成绩接口
	 * 可选条件查询：查询条件可选学号或试卷号
	 * 可带有分页 ，除了返回学号试卷号和学生成绩以外，
	 * 还会将成绩所对应的考试基本信息一起查出并返回     
	 */
	@RequestMapping(value = "/selectGradeAndTestpaperInfoByPageSelective", method = RequestMethod.POST)
	@ResponseBody
	public Object selectGradeAndTestpaperInfoByPageSelective(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("stuId", map.get("stuId"));
			queryBase.addParameter("testpaperId", map.get("testpaperId"));
			queryBase.setPageSize(Long.parseLong(map.get("rows").toString()));
			queryBase
					.setCurrentPage(Long.parseLong(map.get("page").toString()));
			gradeService.selectGradeAndTestpaperInfoByPageSelective(queryBase);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResults());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用UserGradeController.selectGradeAndTestpaperInfoByPageSelective出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}
	
	

	/**
	 * 只查出学号试卷号和学生成绩，不带有对应的考试基本信息
	 * 按条件查询成绩列表(带分页)
	 * 查询条件可选，如可以只传学号（查询该学生的所有成绩），也可学号和试卷号都传（查询该学生的某套试卷的成绩）等等,
	 * 查询条件什么都不传则会查出所有成绩 
	 */
	@RequestMapping(value = "/selectGradeByPageAndSelective", method = RequestMethod.POST)
	@ResponseBody
	public Object selectGradeByPageAndSelective(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("stuId", map.get("stuId"));
			queryBase.addParameter("testpaperId", map.get("testpaperId"));
			queryBase.setPageSize(Long.parseLong(map.get("rows").toString()));
			queryBase
					.setCurrentPage(Long.parseLong(map.get("page").toString()));
			gradeService.selectGradeByPageAndSelections(queryBase);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResults());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用UserGradeController.selectGradeByPageAndSelective出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}	
}