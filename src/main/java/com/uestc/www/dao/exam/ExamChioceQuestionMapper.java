package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamChioceQuestion;

public interface ExamChioceQuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamChioceQuestion record);

    int insertSelective(ExamChioceQuestion record);

    ExamChioceQuestion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamChioceQuestion record);

    int updateByPrimaryKey(ExamChioceQuestion record);
    
    ExamChioceQuestion selectByQuestionId(String questionId);
    
    List<ExamChioceQuestion> selectBySubjectId(String subjectId);
    
    long size(Map<String, Object> map);
    
    List<ExamChioceQuestion> selectByPageAndSelections(QueryBase queryBase);
    
    
}