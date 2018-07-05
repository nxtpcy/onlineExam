package com.uestc.www.service.exam;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamChioceQuestion;

public interface ChoiceQuestionService {

	// 增加题目
	int add(ExamChioceQuestion choiceQuestion);

	// 更新题目信息
	int update(ExamChioceQuestion choiceQuestion);

	// 删除题目
	int delete(ExamChioceQuestion choiceQuestion);

	
	//按科目ID分页查询选择题 
	void selectChoiceQuestionByPageAndSubjectId(QueryBase queryBase);

}