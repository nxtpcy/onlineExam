package com.uestc.www.service.exam;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.exam.ExamSubject;

public interface SubjectService {

	int add(ExamSubject examSubject);

	int update(ExamSubject examSubject);

	void selectSubjectByPageAndSelections(QueryBase queryBase);

}