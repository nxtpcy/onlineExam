package com.uestc.www.service.exam;

import java.util.Map;

import com.uestc.www.common.QueryBase;

public interface MappingService {

	void selectQuestionByTestpaperId(QueryBase queryBase);

	void selectQuestionByUnpublishedTestpaperId(QueryBase queryBase);

	int batchDelete(Map<String, Object> map);

	int batchAddChoice(Map<String, Object> map);

	int batchAddJudge(Map<String, Object> map);

}