package com.uestc.www.service.impl.exam;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.IDGenerator;
import com.uestc.www.common.IdType;
import com.uestc.www.common.QueryBase;
import com.uestc.www.common.StatusType;
import com.uestc.www.dao.exam.ExamChioceQuestionMapper;
import com.uestc.www.dao.exam.ExamJudgeQuestionMapper;
import com.uestc.www.dao.exam.ExamQuestionMappingMapper;
import com.uestc.www.dao.exam.ExamSubjectMapper;
import com.uestc.www.dao.exam.ExamTestpaperMapper;
import com.uestc.www.pojo.dto.exam.TestpaperDTO;
import com.uestc.www.pojo.exam.ExamChioceQuestion;
import com.uestc.www.pojo.exam.ExamJudgeQuestion;
import com.uestc.www.pojo.exam.ExamQuestionMapping;
import com.uestc.www.pojo.exam.ExamTestpaper;
import com.uestc.www.service.exam.TestpaperService;



@Service("testpaperService")
public class TestpaperServiceImpl implements TestpaperService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TransactionTemplate txTemplate;
	
	@Autowired
	private IDGenerator iDGenerator;
	
	@Autowired
	private ExamQuestionMappingMapper mappingMapper;

	@Autowired
	private ExamTestpaperMapper testpaperMapper;
	
	@Autowired
	private ExamSubjectMapper subjectMapper;
	
	@Autowired
	private ExamChioceQuestionMapper choiceQuestionMapper;
	
	@Autowired
	private ExamJudgeQuestionMapper judgeQuestionMapper;
	
	


	//新添加一套试卷，同时向试卷表和试卷题目映射表中插入数据，放在一个事务中进行，失败就全部回滚 
	@Override
	public int add(TestpaperDTO testpaperDTO) {
		if (testpaperDTO == null || testpaperDTO.getTestpaperName() == null 
				|| testpaperDTO.getTestpaperName().trim().equals("") 
				|| testpaperDTO.getSubjectId() == null || subjectMapper.selectBySubjectId(testpaperDTO.getSubjectId()) == null 
				|| testpaperDTO.getStartTime() == null || testpaperDTO.getEndTime() == null 
				|| testpaperDTO.getQuestionIdList() == null || testpaperDTO.getQuestionIdList().size() < 10) {
			return StatusType.PARAMETER_IS_NULL.getValue();
		}
		
		int rows = 0;
		int correctRows = testpaperDTO.getQuestionIdList().size();
		
		try {
			rows = txTemplate.execute(new TransactionCallback<Integer>() {
				public Integer doInTransaction(TransactionStatus status) {
					int totalRows = 0;
					
					String testpaperId = IdType.Testpaper.getSign() + iDGenerator.nextId();
					Date now = new Date();
					ExamTestpaper examTestpaper = new ExamTestpaper();
					examTestpaper.setTestpaperId(testpaperId);
					examTestpaper.setTestpaperName(testpaperDTO.getTestpaperName());
					examTestpaper.setSubjectId(testpaperDTO.getSubjectId());
					examTestpaper.setSubjectName(testpaperDTO.getSubjectName());
					examTestpaper.setStartTime(testpaperDTO.getStartTime());
					examTestpaper.setEndTime(testpaperDTO.getEndTime());
					examTestpaper.setCreateTime(now);
					examTestpaper.setUpdateTime(now);
					examTestpaper.setState((byte) 0); // 新添加的试卷状态应该为未发布 0
					
					List<String> questionIdList = testpaperDTO.getQuestionIdList();
					int totalScore = 0;
					
					for (String questionId : questionIdList) {
						if (questionId ==null || questionId.trim().equals("")) {
							return StatusType.PARAMETER_IS_NULL.getValue();
						}else if(IdType.ChoiceQuestion.getSign().equals(questionId.substring(0, 2))) {
							//选择题
							ExamChioceQuestion choiceQuestion = choiceQuestionMapper.selectByQuestionId(questionId);
							if (choiceQuestion == null) {
								//该题号对应的选择题不存在,回滚，操作失败返回
								status.setRollbackOnly();
								logger.error("该题号对应的选择题不存在,questionId={}", questionId);
								return StatusType.DATA_INVALID.getValue();
							}else {
								totalScore += choiceQuestion.getScore();
								ExamQuestionMapping mapping = new ExamQuestionMapping();
								mapping.setQuestionId(questionId);
								mapping.setCreateTime(now);
								mapping.setUpdateTime(now);
								mapping.setTestpaperId(testpaperId);
								totalRows += mappingMapper.insertSelective(mapping);
							}
							
						}else if (IdType.JudgeQuestion.getSign().equals(questionId.substring(0, 2))) {
							//判断题
							ExamJudgeQuestion judgeQuestion = judgeQuestionMapper.selectByQuestionId(questionId);
							if (judgeQuestion == null) {
								//该题号对应的判断题不存在,回滚，操作失败返回
								status.setRollbackOnly();
								logger.error("该题号对应的判断题不存在,questionId={}", questionId);
								return StatusType.DATA_INVALID.getValue();
							}else {
								totalScore += judgeQuestion.getScore();
								ExamQuestionMapping mapping = new ExamQuestionMapping();
								mapping.setQuestionId(questionId);
								mapping.setCreateTime(now);
								mapping.setUpdateTime(now);
								mapping.setTestpaperId(testpaperId);
								totalRows += mappingMapper.insertSelective(mapping);
							}
						}else {
							//不是判断题也不是选择题，题目ID错误
							status.setRollbackOnly();
							logger.error("前缀不是JQ或CQ，即不是判断题也不是选择题，题目ID错误,questionId={}", questionId);
							return StatusType.DATA_INVALID.getValue();
						}
					}
					//设置计算出的试卷总分 
					examTestpaper.setTotalScore(totalScore);
					//插入试卷表中 
					testpaperMapper.insertSelective(examTestpaper);
					return totalRows;
				}
			});
		} catch (Exception e) {
			logger.error(
					"调用TestpaperServiceImpl.add出错,rows={},testpaperDTO={}",
					new Object[] { rows, testpaperDTO}, e);
		}
		return DAOResultUtil.getBatchResult(rows, correctRows).getValue();
	}

	// 更新试卷名称、状态、开始考试时间、结束考试时间等字段
	@Override
	public int update(ExamTestpaper testpaper) {
		if (testpaper == null || testpaper.getId() == null
				|| testpaper.getTestpaperId() == null || testpaperMapper.selectByTestpaperId(testpaper.getTestpaperId()) == null) {
			return StatusType.NOT_EXISTS.getValue();
		}
		testpaper.setUpdateTime(new Date());
		int rows = testpaperMapper.updateByTestpaperIdSelective(testpaper);

		return DAOResultUtil.getAddUpDateRemoveResult(rows, 0).getValue();
	}

	@Override
	public void selectTestpaperByPageAndSelections(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("subjectId", queryBase.getParameter("subjectId"));
		map.put("testpaperName", queryBase.getParameter("testpaperName"));
		map.put("testpaperId", queryBase.getParameter("testpaperId"));
		map.put("state", queryBase.getParameter("state"));
		queryBase.setTotalRow(testpaperMapper.size(map));// 设置查询到的总数
		
		List<ExamTestpaper> testpaperList = testpaperMapper.selectByPageAndSelections(queryBase);
		queryBase.setResults(testpaperList);// 设置需要返回的数据集
	}
	
}