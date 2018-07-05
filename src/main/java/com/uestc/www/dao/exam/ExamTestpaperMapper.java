package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamTestpaper;

public interface ExamTestpaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamTestpaper record);

    int insertSelective(ExamTestpaper record);

    ExamTestpaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamTestpaper record);

    int updateByPrimaryKey(ExamTestpaper record);
      
    ExamTestpaper selectByTestpaperId(String testpaperId);
    
    //按试卷名精确查询 
    List<ExamTestpaper> selectByTestpaperNameAccurate(String testpaperName);
    
    int updateByTestpaperIdSelective(ExamTestpaper record);
    
    int deleteByTestpaperId(String testpaperId);
    
    long size(Map<String, Object> map);
    
    List<ExamTestpaper> selectByPageAndSelections(QueryBase queryBase);
}