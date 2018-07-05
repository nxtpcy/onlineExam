package com.uestc.www.service.impl.article;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.IDGenerator;
import com.uestc.www.common.IdType;
import com.uestc.www.common.QueryBase;
import com.uestc.www.dao.article.ArticleTypeMapper;
import com.uestc.www.pojo.article.ArticleType;
import com.uestc.www.service.article.ArticleTypeService;

@Service("ArticleTypeService")
public class ArticleTypeServiceImpl implements ArticleTypeService {

	@Autowired
	private ArticleTypeMapper articleTypeMapper;
	
	@Autowired
	private IDGenerator iDGenerator;
	
	@Override
	public int update(ArticleType articleType) {
		// TODO Auto-generated method stub
		articleType.setModifyTime(new Date());
		int rows = articleTypeMapper.updateByArticleTypeIdSelective(articleType);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int add(ArticleType articleType) {
		// TODO Auto-generated method stub
		String articleTypeId = IdType.ArticleType.getSign() + iDGenerator.nextId();
		articleType.setArticleTypeId(articleTypeId);
		articleType.setCreateTime(new Date());
		articleType.setModifyTime(new Date());
		int rows = articleTypeMapper.insertSelective(articleType);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public int delete(String articleTypeId) {
		// TODO Auto-generated method stub
		int rows = articleTypeMapper.deleteByArticleTypeId(articleTypeId);
		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public QueryBase queryBySelective(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		QueryBase queryBase = new QueryBase();
		if ((paramMap.get("page") != null) && (paramMap.get("rows") != null)) {
			long page = Long.parseLong(paramMap.get("page").toString());
			long rows = Long.parseLong(paramMap.get("rows").toString());
			queryBase.setPageSize(Long.valueOf(rows));
			queryBase.setCurrentPage(Long.valueOf(page));
		}
		queryBase.setParameters(paramMap);
		Long rows = articleTypeMapper.queryRows(queryBase);
		List<ArticleType> results = articleTypeMapper.selectBySelective(queryBase);
		queryBase.setResults(results);
		queryBase.setTotalRow(rows);
		return queryBase;
	}

}
