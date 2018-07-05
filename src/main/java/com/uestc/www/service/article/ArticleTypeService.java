package com.uestc.www.service.article;

import java.util.Map;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.article.ArticleType;

public interface ArticleTypeService {
	
	int update(ArticleType articleType);

	int add(ArticleType articleType);

	int delete(String articleTypeId);

	abstract QueryBase queryBySelective(Map<String, Object> paramMap);
}
