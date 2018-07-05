package com.uestc.www.service.impl.exam;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.uestc.www.common.IdType;
import com.uestc.www.common.QueryBase;
import com.uestc.www.dao.exam.ExamQuestionMappingMapper;
import com.uestc.www.pojo.dto.exam.ChoiceQuestionDTO;
import com.uestc.www.pojo.dto.exam.JudgeQuestionDTO;
import com.uestc.www.service.exam.MappingService;



@Service("mappingService")
public class MappingServiceImpl implements MappingService {

	@Autowired
	private ExamQuestionMappingMapper mappingMapper;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
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
		long totalQuestionNum = mappingMapper.size(map); // 该套试卷的总题数
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
			choiceQuestionList = mappingMapper.selectChoiceQuestionByMapping(testpaperId);
			judgeQuestionList = mappingMapper.selectJudgeQuestionByMapping(testpaperId);
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
	
}