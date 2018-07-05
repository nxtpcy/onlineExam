package com.uestc.www.service.exam;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamJudgeQuestion;

public interface JudgeQuestionService {

	// 增加题目
	int add(ExamJudgeQuestion judgeQuestion);

	// 更新题目信息
	int update(ExamJudgeQuestion judgeQuestion);

	// 删除题目
	int delete(ExamJudgeQuestion judgeQuestion);

	
	//按科目ID分页查询判断题 
	void selectJudgeQuestionByPageAndSubjectId(QueryBase queryBase);

}