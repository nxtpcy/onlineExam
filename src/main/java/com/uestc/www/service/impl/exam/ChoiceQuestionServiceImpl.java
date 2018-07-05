package com.uestc.www.service.impl.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.uestc.www.common.*;
import com.uestc.www.dao.exam.ExamChioceQuestionMapper;
import com.uestc.www.dao.exam.ExamQuestionMappingMapper;
import com.uestc.www.pojo.exam.ExamChioceQuestion;
import com.uestc.www.service.exam.ChoiceQuestionService;
import com.uestc.www.common.DAOResultUtil;

@Service("choiceQuestionService")
public class ChoiceQuestionServiceImpl implements ChoiceQuestionService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	//注入ID生成器
	@Autowired
	private IDGenerator iDGenerator;
	
	//注入选择题题库mapper 
	@Autowired 
	private ExamChioceQuestionMapper examChioceQuestionMapper;
	
	@Autowired 
	private ExamQuestionMappingMapper examQuestionMappingMapper;
	
	// 增加题目
	@Override
	public int add(ExamChioceQuestion choiceQuestion) {
		if (choiceQuestion == null || choiceQuestion.getContent() == null || choiceQuestion.getContent().equals("")) {
			return StatusType.OBJECT_NULL.getValue();
		}
		//生成唯一id
		String questionId = IdType.ChoiceQuestion.getSign() + iDGenerator.nextId();
		choiceQuestion.setQuestionId(questionId);
		Date now = new Date();
		choiceQuestion.setCreateTime(now);
		choiceQuestion.setUpdateTime(now);
		int rows = examChioceQuestionMapper.insertSelective(choiceQuestion);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	// 更新题目信息
	@Override
	public int update(ExamChioceQuestion choiceQuestion) {
		if (choiceQuestion == null || choiceQuestion.getQuestionId() == null
				|| examChioceQuestionMapper.selectByQuestionId(choiceQuestion.getQuestionId()) == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		String questionId = choiceQuestion.getQuestionId();
		choiceQuestion.setUpdateTime(new Date());
		int rows = examChioceQuestionMapper.updateByPrimaryKeySelective(choiceQuestion);
		//同时让redis中该题目所相关的试卷缓存失效 
		deleteRedisCache(questionId);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	// 删除题目
	@Override
	public int delete(ExamChioceQuestion choiceQuestion) {
		if (choiceQuestion == null || choiceQuestion.getQuestionId() == null
				|| examChioceQuestionMapper.selectByQuestionId(choiceQuestion.getQuestionId()) == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		int rows = examChioceQuestionMapper.deleteByPrimaryKey(choiceQuestion.getId());
		String questionId = choiceQuestion.getQuestionId();
		deleteRedisCache(questionId);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	//按科目ID分页查询该科目下的选择题
	@Override
	public void selectChoiceQuestionByPageAndSubjectId(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", queryBase.getParameter("subjectId"));
		queryBase.setTotalRow(examChioceQuestionMapper.size(map));// 设置查询到的题目数
		
		List<ExamChioceQuestion> choiceQuestionList = examChioceQuestionMapper.selectByPageAndSelections(queryBase);
		queryBase.setResults(choiceQuestionList);// 获取需要返回的数据集
	}
	
	private void deleteRedisCache(String questionId) {
		List<String> paperIdList = examQuestionMappingMapper.selectTestpaperIdByQuestionId(questionId);
		if (paperIdList!=null && paperIdList.size()>0) {
			// 遍历，查看redis中有无试卷上该题目的缓存
			for (String testpaperId : paperIdList) {
				if (stringRedisTemplate.hasKey(testpaperId)) {
					stringRedisTemplate.delete(testpaperId);
				}
				if (stringRedisTemplate.hasKey(testpaperId+"Questions")) {
					stringRedisTemplate.delete(testpaperId+"Questions");
				}
			}
		}
	}
	
}