package com.uestc.www.dao.article;

import java.util.List;

import com.uestc.www.common.QueryBase;
import com.uestc.www.pojo.article.ArticleType;

public interface ArticleTypeMapper {
	
    int deleteByPrimaryKey(Integer id);
    
    int deleteByArticleTypeId(String articleTypeId);

    int insert(ArticleType record);

    int insertSelective(ArticleType record);

    ArticleType selectByPrimaryKey(Integer id);
    
    List<ArticleType> selectBySelective(QueryBase paramQueryBase);

    int updateByPrimaryKeySelective(ArticleType record);

    int updateByPrimaryKey(ArticleType record);
    
    int updateByArticleTypeIdSelective(ArticleType record);
    
    long queryRows(QueryBase paramQueryBase);
}