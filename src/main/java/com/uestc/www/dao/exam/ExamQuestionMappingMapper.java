package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.pojo.dto.exam.ChoiceQuestionDTO;
import com.uestc.www.pojo.dto.exam.JudgeQuestionDTO;
import com.uestc.www.pojo.exam.ExamQuestionMapping;

public interface ExamQuestionMappingMapper {

    int insert(ExamQuestionMapping record);

    int insertSelective(ExamQuestionMapping record);

    ExamQuestionMapping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamQuestionMapping record);

    int updateByPrimaryKey(ExamQuestionMapping record); 
    
    int deleteByTestpaperId(String testpaperId);
    
    List<ExamQuestionMapping> selectByTestpaperId(String testpaperId);
    
    long size(Map<String, Object> map);
    
    List<ChoiceQuestionDTO> selectChoiceQuestionByMapping(String testpaperId);
    
    List<JudgeQuestionDTO> selectJudgeQuestionByMapping(String testpaperId);
    
    List<String> selectTestpaperIdByQuestionId(String questionId);
}