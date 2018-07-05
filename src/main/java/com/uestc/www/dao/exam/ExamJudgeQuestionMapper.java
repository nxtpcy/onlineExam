package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamJudgeQuestion;

public interface ExamJudgeQuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamJudgeQuestion record);

    int insertSelective(ExamJudgeQuestion record);

    ExamJudgeQuestion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamJudgeQuestion record);

    int updateByPrimaryKey(ExamJudgeQuestion record);  
    
    ExamJudgeQuestion selectByQuestionId(String questionId);
    
    List<ExamJudgeQuestion> selectBySubjectId(String subjectId);
    
    // 获取查询到的数据量 
    long size(Map<String, Object> map);
    
    List<ExamJudgeQuestion> selectByPageAndSelections(QueryBase queryBase);
    
}