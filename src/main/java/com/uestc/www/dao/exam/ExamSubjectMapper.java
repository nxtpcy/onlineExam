package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamSubject;

public interface ExamSubjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamSubject record);

    int insertSelective(ExamSubject record);

    ExamSubject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamSubject record);

    int updateByPrimaryKey(ExamSubject record);
    
    ExamSubject selectBySubjectId(String subjectId);
    
    List<ExamSubject> selectBySubjectName(String subjectName); // 按科目名模糊查询
    
    ExamSubject selectBySubjectNameAccurate(String subjectName); // 按科目名精确查询
    
    int updateBySubjectIdSelective(ExamSubject record);
    
    int deleteBySubjectId(String subjectId);
    
    long size(Map<String, Object> map);
    
    List<ExamSubject> selectByPageAndSelections(QueryBase queryBase);
}