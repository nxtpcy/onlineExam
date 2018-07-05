package com.uestc.www.service.exam;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.dto.exam.TestpaperDTO;
import com.uestc.www.pojo.exam.ExamTestpaper;

public interface TestpaperService {

	int add(TestpaperDTO testpaperDTO);

	int update(ExamTestpaper testpaper);

	void selectTestpaperByPageAndSelections(QueryBase queryBase);

	
}