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
@RequestMapping(value = "/admin/grade")
public class AdminGradeController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GradeService gradeService;
	
	/* 
	 * 管理员查看学生考试成绩的接口
	 * 可选条件查询：查询条件可选学号或试卷号
	 * 可带有分页 ，除了返回学号试卷号和学生成绩以外，
	 * 还会将成绩所对应的考试基本信息及考生个人信息一起查出并返回     
	 */
	@RequestMapping(value = "/selectGradeAndUserInfoByPageSelective", method = RequestMethod.POST)
	@ResponseBody
	public Object selectGradeAndUserInfoByPageSelective(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody Map<String, Object> map) {
		try {
			QueryBase queryBase = new QueryBase();
			queryBase.addParameter("stuId", map.get("stuId"));
			queryBase.addParameter("testpaperId", map.get("testpaperId"));
			queryBase.setPageSize(Long.parseLong(map.get("rows").toString()));
			queryBase
					.setCurrentPage(Long.parseLong(map.get("page").toString()));
			gradeService.selectGradeAndUserInfoByPageSelective(queryBase);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("total", queryBase.getTotalRow());
			result.put("rows", queryBase.getResults());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("调用AdminGradeController.selectGradeAndUserInfoByPageSelective出错,map={},error={}",
					new Object[] { map, e });
			return new Response(StatusType.EXCEPTION.getValue(),
					StatusType.EXCEPTION.getMessage());
		}
		
	}
	
}