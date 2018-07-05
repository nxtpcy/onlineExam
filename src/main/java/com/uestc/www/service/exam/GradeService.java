package com.uestc.www.service.exam;

import com.uestc.www.common.QueryBase;

public interface GradeService {

	void selectGradeByPageAndSelections(QueryBase queryBase);

	void selectGradeAndTestpaperInfoByPageSelective(QueryBase queryBase);

	void selectGradeAndUserInfoByPageSelective(QueryBase queryBase);

}