package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.dto.exam.ChoiceQuestionRecordDTO;
import com.uestc.www.pojo.dto.exam.JudgeQuestionRecordDTO;
import com.uestc.www.pojo.exam.ExamRecord;

public interface ExamRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamRecord record);

    int insertSelective(ExamRecord record);

    ExamRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamRecord record);

    int updateByPrimaryKey(ExamRecord record);         
    
    List<ExamRecord> selectByPageAndSelections(QueryBase queryBase);
       
    int deleteByStuIdAndTestpaperId(Map<String, String> map);
        
    long size(Map<String, Object> map);
    
    int updateByStuIdAndTestpaperIdAndQuestionIdSelective(ExamRecord record);
    
    List<ChoiceQuestionRecordDTO> selectChoiceAndRecordByPageAndStuIdAndTestpaperId(QueryBase queryBase);
    
    List<JudgeQuestionRecordDTO> selectJudgeAndRecordByPageAndStuIdAndTestpaperId(QueryBase queryBase);
    
}