package com.uestc.www.service.impl.exam;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.uestc.www.common.DAOResultUtil;
import com.uestc.www.common.IdType;
import com.uestc.www.common.QueryBase;
import com.uestc.www.common.StatusType;
import com.uestc.www.dao.exam.ExamChioceQuestionMapper;
import com.uestc.www.dao.exam.ExamJudgeQuestionMapper;
import com.uestc.www.dao.exam.ExamQuestionMappingMapper;
import com.uestc.www.dao.exam.ExamTestpaperMapper;
import com.uestc.www.pojo.dto.exam.ChoiceQuestionDTO;
import com.uestc.www.pojo.dto.exam.JudgeQuestionDTO;
import com.uestc.www.pojo.exam.ExamChioceQuestion;
import com.uestc.www.pojo.exam.ExamJudgeQuestion;
import com.uestc.www.pojo.exam.ExamQuestionMapping;
import com.uestc.www.pojo.exam.ExamTestpaper;
import com.uestc.www.service.exam.MappingService;



@Service("mappingService")
public class MappingServiceImpl implements MappingService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExamQuestionMappingMapper examQuestionMappingMapper;
	
	@Autowired
	private ExamChioceQuestionMapper examChioceQuestionMapper;
	
	@Autowired
	private ExamJudgeQuestionMapper examJudgeQuestionMapper;
	
	@Autowired
	private ExamTestpaperMapper examTestpaperMapper;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private TransactionTemplate txTemplate;
	
	@Autowired
	
	private static final long EXPIRE_TIME = 60;

	// 按试卷号查出这份试卷包含的所有题目 （选择和判断题）
	@Override
	public void selectQuestionByTestpaperId(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		String testpaperId = (String) queryBase.getParameter("testpaperId");
		String questionId = "";
		String answer = "";
		int score = 0;
		List<ChoiceQuestionDTO> choiceQuestionList = new ArrayList<ChoiceQuestionDTO>();
		List<JudgeQuestionDTO> judgeQuestionList = new ArrayList<JudgeQuestionDTO>();
		
		map.put("testpaperId", testpaperId);
		long totalQuestionNum = examQuestionMappingMapper.size(map); // 该套试卷的总题数
		queryBase.setTotalRow(totalQuestionNum);// 设置查询到的总题数
		// 先查redis中有无该套试卷的题目缓存，有则直接从缓存取，否则才查询数据库
		Map<Object, Object> questionMap = stringRedisTemplate.opsForHash().entries(testpaperId+"Questions");
		if(questionMap!=null && questionMap.size()==totalQuestionNum){
			for (Entry<Object, Object> q : questionMap.entrySet()) {
				questionId = (String) q.getKey();
				Object questionDTO = q.getValue();
				if (questionId.substring(0, 2).equals(IdType.ChoiceQuestion.getSign())) {
					// 选择题
					ChoiceQuestionDTO cqDTO = (ChoiceQuestionDTO) questionDTO;
					choiceQuestionList.add(cqDTO);
				}else {
					JudgeQuestionDTO jqDTO = (JudgeQuestionDTO) questionDTO;
					judgeQuestionList.add(jqDTO);
				}
			}
			
		}else {
			// redis中没有该套试卷的题目缓存，则从数据库中查询，并将查询到的题目、正确答案及题目分值放入redis中，以便之后的访问可直接从redis缓存中读取，
			// 而不用再查数据库了
			choiceQuestionList = examQuestionMappingMapper.selectChoiceQuestionByMapping(testpaperId);
			judgeQuestionList = examQuestionMappingMapper.selectJudgeQuestionByMapping(testpaperId);
			Map<Object, Object> questionsToPutIntoRedis = new HashMap<Object, Object>();
			Map<Object, Object> answerAndScore = new HashMap<Object, Object>();
			for (JudgeQuestionDTO judgeQuestionDTO : judgeQuestionList) {
				questionId = judgeQuestionDTO.getQuestionId();
				answer = judgeQuestionDTO.getAnswer();
				score = judgeQuestionDTO.getScore();
				
				// 将题号、题目对应的正确答案，分值放入redis，方便该套试卷后续考试访问以及之后交卷时自动判分来使用 
				questionsToPutIntoRedis.put(questionId, judgeQuestionDTO);
				answerAndScore.put(questionId, answer);
				answerAndScore.put(questionId+"Score", score);
			}
			for (ChoiceQuestionDTO choiceQuestionDTO : choiceQuestionList) {
				questionId = choiceQuestionDTO.getQuestionId();
				answer = choiceQuestionDTO.getAnswer();
				score = choiceQuestionDTO.getScore();
				
				// 将题号、题目对应的正确答案，分值放入redis，方便该套试卷后续考试访问以及之后交卷时自动判分来使用 
				questionsToPutIntoRedis.put(questionId, choiceQuestionDTO);
				answerAndScore.put(questionId, answer);
				answerAndScore.put(questionId+"Score", score);
			}
			// 将题目放入缓存中
			stringRedisTemplate.opsForHash().putAll(testpaperId+"Questions", questionsToPutIntoRedis);
			// 设置key的过期时间
			stringRedisTemplate.expire(testpaperId+"Questions", EXPIRE_TIME, TimeUnit.MINUTES);
			// 将正确答案及分值放入redis
			stringRedisTemplate.opsForHash().putAll(testpaperId, answerAndScore);
			// 设置key的过期时间
			stringRedisTemplate.expire(testpaperId, EXPIRE_TIME, TimeUnit.MINUTES);
		}
		
		Map<String, List<? extends Object>> resultMap = new HashMap<String, List<? extends Object>>();
		resultMap.put("choiceQuestionList", choiceQuestionList);
		resultMap.put("judgeQuestionList", judgeQuestionList);
		queryBase.setResultMap(resultMap);
	}
	
	// 按试卷号查出未发布试卷包含的所有题目 （选择和判断题），以便进行题目删除操作
	// 无需查缓存，因为是还未发布的试卷，所以redis中不可能有该套试卷的题目缓存 
	@Override
	public void selectQuestionByUnpublishedTestpaperId(QueryBase queryBase) {
		Map<String, Object> map = new HashMap<String, Object>();
		String testpaperId = (String) queryBase.getParameter("testpaperId");
		List<ChoiceQuestionDTO> choiceQuestionList = new ArrayList<ChoiceQuestionDTO>();
		List<JudgeQuestionDTO> judgeQuestionList = new ArrayList<JudgeQuestionDTO>();
		
		map.put("testpaperId", testpaperId);
		queryBase.setTotalRow(examQuestionMappingMapper.size(map));// 设置查询到的总题数
		// 从数据库中查询
		choiceQuestionList = examQuestionMappingMapper.selectChoiceQuestionByMapping(testpaperId);
		judgeQuestionList = examQuestionMappingMapper.selectJudgeQuestionByMapping(testpaperId);
		Map<String, List<? extends Object>> resultMap = new HashMap<String, List<? extends Object>>();
		resultMap.put("choiceQuestionList", choiceQuestionList);
		resultMap.put("judgeQuestionList", judgeQuestionList);
		queryBase.setResultMap(resultMap);
	}
	
	// 批量删除未发布试卷上的题目，同时更新该试卷的对应总分total_score
	@Override
	public int batchDelete(Map<String, Object> map){
		String testpaperId = (String) map.get("testpaperId");
		String questionIds = (String) map.get("questionIds");
		String[] questionIdArr = questionIds.split(",");
		if (questionIdArr==null || questionIdArr.length==0) {
			// 前端传递的要删除的题目题号为空
			return StatusType.DATA_INVALID.getValue();
		}
		int rows = 0;
		try {
			rows = txTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus status) {
					int sum = 0;
					List<String> questionIdList = new ArrayList<String>();
					for (String questionId : questionIdArr) {
						if (IdType.ChoiceQuestion.getSign().equals(questionId.substring(0, 2))) {
							// 选择题
							ExamChioceQuestion cq = examChioceQuestionMapper.selectByQuestionId(questionId);
							sum += cq.getScore();
						}else if (IdType.JudgeQuestion.getSign().equals(questionId.substring(0, 2))) {
							// 判断题 
							ExamJudgeQuestion jq = examJudgeQuestionMapper.selectByQuestionId(questionId);
							sum += jq.getScore();
						}else {
							// 题号无效 
							return 0;
						}
						questionIdList.add(questionId);
						
						
					}
					// 更新删除题目后的试卷总分值
					ExamTestpaper tp = examTestpaperMapper.selectByTestpaperId(testpaperId);
					int totalScore = tp.getTotalScore();
					totalScore -= sum;
					tp.setTotalScore(totalScore);
					tp.setUpdateTime(new Date());
					examTestpaperMapper.updateByPrimaryKeySelective(tp);
					
					// 删除试卷题目mapping关系
					Map<String, Object> deleteMap = new HashMap<String, Object>();
					deleteMap.put("testpaperId", testpaperId);
					deleteMap.put("questionId", questionIdList);
					return examQuestionMappingMapper.batchDelete(deleteMap);
				}
				
			});
		} catch (Exception e) {
			logger.error(
					"调用MappingServiceImpl.batchDelete出错,map={},exception={}",
					map, e);
		}
		return DAOResultUtil.getBatchResult(rows, questionIdArr.length).getValue();
	}
	
	// 向某套未发布试卷批量添加选择题 
	@Override
	public int batchAddChoice(Map<String, Object> map){
		String testpaperId = (String) map.get("testpaperId");
		String questionIds = (String) map.get("questionIds");
		String[] questionIdArr = questionIds.split(",");
		if (questionIdArr==null || questionIdArr.length==0) {
			// 前端传递的要添加的选择题题号为空
			return StatusType.DATA_INVALID.getValue();
		}
		int rows = 0;
		try {
			rows = txTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus status) {
					int sum = 0;
					int totalInsert = 0;
					for (String questionId : questionIdArr) {
						ExamChioceQuestion cq = examChioceQuestionMapper.selectByQuestionId(questionId);
						if (cq!=null) {
							sum += cq.getScore();
							ExamQuestionMapping mapping = new ExamQuestionMapping(questionId, testpaperId, 
									new Date(), new Date());
							// 插入试卷题目的对应关系mapping 
							totalInsert += examQuestionMappingMapper.insertSelective(mapping);
						}
					}
					// 更新批量添加选择题后的试卷总分值
					ExamTestpaper tp = examTestpaperMapper.selectByTestpaperId(testpaperId);
					int totalScore = tp.getTotalScore();
					totalScore += sum;
					tp.setTotalScore(totalScore);
					tp.setUpdateTime(new Date());
					examTestpaperMapper.updateByPrimaryKeySelective(tp);
					
					return totalInsert;
				}
				
			});
		} catch (Exception e) {
			logger.error(
					"调用MappingServiceImpl.batchAddChoice出错,map={},exception={}",
					map, e);
		}
		return DAOResultUtil.getBatchResult(rows, questionIdArr.length).getValue();
	}
	
	// 向某套未发布试卷批量添加判断题 
	@Override
	public int batchAddJudge(Map<String, Object> map){
		String testpaperId = (String) map.get("testpaperId");
		String questionIds = (String) map.get("questionIds");
		String[] questionIdArr = questionIds.split(",");
		if (questionIdArr==null || questionIdArr.length==0) {
			// 前端传递的要添加的选择题题号为空
			return StatusType.DATA_INVALID.getValue();
		}
		int rows = 0;
		try {
			rows = txTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus status) {
					int sum = 0;
					int totalInsert = 0;
					for (String questionId : questionIdArr) {
						ExamJudgeQuestion jq = examJudgeQuestionMapper.selectByQuestionId(questionId);
						if (jq!=null) {
							sum += jq.getScore();
							ExamQuestionMapping mapping = new ExamQuestionMapping(questionId, testpaperId, 
									new Date(), new Date());
							// 插入试卷题目的对应关系mapping 
							totalInsert += examQuestionMappingMapper.insertSelective(mapping);
						}
					}
					// 更新批量添加选择题后的试卷总分值
					ExamTestpaper tp = examTestpaperMapper.selectByTestpaperId(testpaperId);
					int totalScore = tp.getTotalScore();
					totalScore += sum;
					tp.setTotalScore(totalScore);
					tp.setUpdateTime(new Date());
					examTestpaperMapper.updateByPrimaryKeySelective(tp);
					
					return totalInsert;
				}
				
			});
		} catch (Exception e) {
			logger.error(
					"调用MappingServiceImpl.batchAddJudge出错,map={},exception={}",
					map, e);
		}
		return DAOResultUtil.getBatchResult(rows, questionIdArr.length).getValue();
	}
	
	// 添加题目时，已在试卷中的题目不能重复添加，应被显示为不可添加状态 
	
	
	
	
	
	
	
	
	
	
	
	
	
}