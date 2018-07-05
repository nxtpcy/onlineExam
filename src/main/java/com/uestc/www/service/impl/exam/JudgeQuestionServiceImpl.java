package com.uestc.www.service.impl.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.uestc.www.common.*;
import com.uestc.www.dao.exam.ExamJudgeQuestionMapper;
import com.uestc.www.dao.exam.ExamQuestionMappingMapper;
import com.uestc.www.pojo.exam.ExamJudgeQuestion;
import com.uestc.www.service.exam.JudgeQuestionService;
import com.uestc.www.common.DAOResultUtil;

@Service("judgeQuestionService")
public class JudgeQuestionServiceImpl implements JudgeQuestionService {

	
	//注入ID生成器
	@Autowired
	private IDGenerator iDGenerator;
	
	//注入选择题题库mapper 
	@Autowired 
	private ExamJudgeQuestionMapper examJudgeQuestionMapper;
	
	@Autowired
	private ExamQuestionMappingMapper examQuestionMappingMapper;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	// 增加题目
	@Override
	public int add(ExamJudgeQuestion judgeQuestion) {
		if (judgeQuestion == null || judgeQuestion.getContent() == null || judgeQuestion.getContent().equals("")) {
			return StatusType.OBJECT_NULL.getValue();
		}
		//生成唯一id
		String questionId = IdType.JudgeQuestion.getSign() + iDGenerator.nextId();
		judgeQuestion.setQuestionId(questionId);
		int rows = examJudgeQuestionMapper.insertSelective(judgeQuestion);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	// 更新题目信息
	@Override
	public int update(ExamJudgeQuestion judgeQuestion) {
		if (judgeQuestion == null || judgeQuestion.getQuestionId() == null
				|| examJudgeQuestionMapper.selectByQuestionId(judgeQuestion.getQuestionId()) == null) {
			return StatusType.NOT_EXISTS.getValue();
		}

		int rows = examJudgeQuestionMapper.updateByPrimaryKeySelective(judgeQuestion);
		String questionId = judgeQuestion.getQuestionId();
		//同时让redis中该题目所相关的试卷缓存失效 
		deleteRedisCache(questionId);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	// 删除题目
	@Override
	public int delete(ExamJudgeQuestion judgeQuestion) {
		if (judgeQuestion == null || judgeQuestion.getQuestionId() == null
				|| examJudgeQuestionMapper.selectByQuestionId(judgeQuestion.getQuestionId()) == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		int rows = examJudgeQuestionMapper.deleteByPrimaryKey(judgeQuestion.getId());
		String questionId = judgeQuestion.getQuestionId();
		deleteRedisCache(questionId);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	//按科目ID分页查询该科目下的选择题
	@Override
	public void selectJudgeQuestionByPageAndSubjectId(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", queryBase.getParameter("subjectId"));
		queryBase.setTotalRow(examJudgeQuestionMapper.size(map));// 设置查询到的题目数
		
		List<ExamJudgeQuestion> judgeQuestionList = examJudgeQuestionMapper.selectByPageAndSelections(queryBase);
		queryBase.setResults(judgeQuestionList);// 获取需要返回的数据集
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