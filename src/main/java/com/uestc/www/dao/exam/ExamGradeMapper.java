package com.uestc.www.dao.exam;

import java.util.List;
import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.dto.exam.AdminGradeDTO;
import com.uestc.www.pojo.dto.exam.GradeDTO;
import com.uestc.www.pojo.exam.ExamGrade;

public interface ExamGradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamGrade record);

    int insertSelective(ExamGrade record);

    ExamGrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamGrade record);

    int updateByPrimaryKey(ExamGrade record);
    
    // 分页按条件查询获取成绩信息
    List<ExamGrade> selectByPageAndSelections(QueryBase queryBase);
    
    // 根据查询条件获取查询获得的数据量
    long size(Map<String, Object> map);
        
    //根据学号和试卷号更新某学生某份试卷的成绩
    int updateByStuIdAndTestpaperIdSelective(ExamGrade record);
    
    List<GradeDTO> selectGradeAndTestpaperInfoByPageSelective(QueryBase queryBase);
    
    List<AdminGradeDTO> selectGradeAndUserInfoByPageSelective(QueryBase queryBase);
    
}