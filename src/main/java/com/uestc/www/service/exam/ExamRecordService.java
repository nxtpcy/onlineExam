package com.uestc.www.service.exam;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.dto.exam.StuAnswerDTO;

public interface ExamRecordService {

	int submitPaperAndUpdateGrade(StuAnswerDTO stuAnswerDTO);

	void selectRecordByStuIdAndTestpaperIdByPage(QueryBase queryBase);
}