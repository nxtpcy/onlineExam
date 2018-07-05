package com.uestc.www.service.impl.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.www.common.QueryBase;
import com.uestc.www.dao.exam.ExamGradeMapper;
import com.uestc.www.pojo.dto.exam.AdminGradeDTO;
import com.uestc.www.pojo.dto.exam.GradeDTO;
import com.uestc.www.pojo.exam.ExamGrade;
import com.uestc.www.service.exam.GradeService;

@Service("gradeService")
public class GradeServiceImpl implements GradeService {
	
	@Autowired
	private ExamGradeMapper examGradeMapper; 
	
	
	/* 可选字段查询，只传学号查出一个学生的所有成绩列表
	 同时传学号和试卷号查某学生某一次考试的成绩，都带有分页 ，但是只返回学号试卷号和学生成绩，
	 不带成绩所对应的考试基本信息    */
	@Override
	public void selectGradeByPageAndSelections(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stuId", queryBase.getParameter("stuId"));
		map.put("testpaperId", queryBase.getParameter("testpaperId"));
		queryBase.setTotalRow(examGradeMapper.size(map));// 设置查询到的总数
		
		List<ExamGrade> gradeList = examGradeMapper.selectByPageAndSelections(queryBase);
		queryBase.setResults(gradeList);// 设置需要返回的数据集
	}
	
	
	/* 
	 * 学生查询成绩接口
	 * 可选条件查询：查询条件可选学号或试卷号
	 * 可带有分页 ，除了返回学号试卷号和学生成绩以外，
	 * 还会将成绩所对应的考试基本信息一起查出并返回     
	 */
	@Override
	public void selectGradeAndTestpaperInfoByPageSelective(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stuId", queryBase.getParameter("stuId"));
		map.put("testpaperId", queryBase.getParameter("testpaperId"));
		queryBase.setTotalRow(examGradeMapper.size(map));// 设置查询到的总数
		
		List<GradeDTO> gradeInfoList = examGradeMapper
				.selectGradeAndTestpaperInfoByPageSelective(queryBase);
		queryBase.setResults(gradeInfoList);// 设置需要返回的数据集
	}	
	
	/* 
	 * 管理员查看学生考试成绩的接口
	 * 可选条件查询：查询条件可选学号或试卷号
	 * 可带有分页 ，除了返回学号试卷号和学生成绩以外，
	 * 还会将成绩所对应的考试基本信息及考生个人信息一起查出并返回     
	 */
	@Override
	public void selectGradeAndUserInfoByPageSelective(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stuId", queryBase.getParameter("stuId"));
		map.put("testpaperId", queryBase.getParameter("testpaperId"));
		queryBase.setTotalRow(examGradeMapper.size(map));// 设置查询到的总数
		
		List<AdminGradeDTO> gradeInfoList = examGradeMapper
				.selectGradeAndUserInfoByPageSelective(queryBase);
		queryBase.setResults(gradeInfoList);// 设置需要返回的数据集
	}
}