package com.uestc.www.service.impl.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uestc.www.common.*;
import com.uestc.www.dao.exam.ExamSubjectMapper;
import com.uestc.www.pojo.exam.ExamSubject;

import com.uestc.www.service.exam.SubjectService;
import com.uestc.www.common.DAOResultUtil;

@Service("subjectService")
public class SubjectServiceImpl implements SubjectService {

	
	//注入ID生成器
	@Autowired
	private IDGenerator iDGenerator;
	
	//注入mapper 
	@Autowired 
	private ExamSubjectMapper examSubjectMapper;
	
	
	// 增加科目
	@Override
	public int add(ExamSubject examSubject) {
		if (examSubject == null || examSubject.getSubjectName() == null || examSubject.getSubjectName().trim().equals("")) {
			return StatusType.OBJECT_NULL.getValue();
		}
		//判断是否已有同名的科目存在
		if(examSubjectMapper.selectBySubjectNameAccurate(examSubject.getSubjectName()) != null){
			//已存在同名科目，添加失败
			return StatusType.EXISTS.getValue();
		}
		
		//生成subjectId，并设置新增加的科目的状态为未发布（0）
		String subjectId = IdType.Subject.getSign() + iDGenerator.nextId();
		examSubject.setSubjectId(subjectId);
		examSubject.setState((byte) 0);
		Date now = new Date();
		examSubject.setCreateTime(now);
		examSubject.setUpdateTime(now);
		int rows = examSubjectMapper.insertSelective(examSubject);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	// 更新科目的状态state
	@Override
	public int update(ExamSubject examSubject) {
		if (examSubject == null || examSubject.getId() == null || examSubject.getSubjectId() == null
				|| examSubject.getSubjectId().trim().equals("") || examSubjectMapper.selectBySubjectId(examSubject.getSubjectId()) == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		
		examSubject.setUpdateTime(new Date());
		int rows = examSubjectMapper.updateBySubjectIdSelective(examSubject);

		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	
	/**
	 * 按科目id，科目名，状态查询科目（带分页，各查询字段均可选 ）
	 * 
	 */
	@Override
	public void selectSubjectByPageAndSelections(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", queryBase.getParameter("subjectId"));
		map.put("subjectName", queryBase.getParameter("subjectName"));
		map.put("state", queryBase.getParameter("state"));
		queryBase.setTotalRow(examSubjectMapper.size(map));// 设置查询到的总数
		
		List<ExamSubject> subjectList = examSubjectMapper.selectByPageAndSelections(queryBase);
		queryBase.setResults(subjectList);// 设置需要返回的数据集
	}
	
}