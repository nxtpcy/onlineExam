package com.uestc.www.service.impl.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import com.uestc.www.common.IdType;
import com.uestc.www.common.QueryBase;
import com.uestc.www.common.StatusType;
import com.uestc.www.dao.exam.ExamChioceQuestionMapper;
import com.uestc.www.dao.exam.ExamGradeMapper;
import com.uestc.www.dao.exam.ExamJudgeQuestionMapper;
import com.uestc.www.dao.exam.ExamRecordMapper;
import com.uestc.www.dao.exam.ExamTestpaperMapper;
import com.uestc.www.dao.user.UserMapper;
import com.uestc.www.pojo.dto.exam.ChoiceQuestionRecordDTO;
import com.uestc.www.pojo.dto.exam.JudgeQuestionRecordDTO;
import com.uestc.www.pojo.dto.exam.StuAnswerDTO;
import com.uestc.www.pojo.exam.ExamChioceQuestion;
import com.uestc.www.pojo.exam.ExamGrade;
import com.uestc.www.pojo.exam.ExamJudgeQuestion;
import com.uestc.www.pojo.exam.ExamRecord;
import com.uestc.www.service.exam.ExamRecordService;
import com.uestc.www.common.DAOResultUtil;

@Service("examRecordService")
public class ExamRecordServiceImpl implements ExamRecordService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private TransactionTemplate txTemplate;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ExamTestpaperMapper examTestpaperMapper;
	
	@Autowired
	private ExamGradeMapper examGradeMapper;
	
	@Autowired
	private ExamRecordMapper examRecordMapper;
	
	@Autowired
	private ExamChioceQuestionMapper examChioceQuestionMapper;
	
	@Autowired
	private ExamJudgeQuestionMapper examJudgeQuestionMapper;
	
	// 提交试卷计算分数，往成绩表和考试记录表中插入或者更新信息  
	@Override
	public int submitPaperAndUpdateGrade(StuAnswerDTO stuAnswerDTO) {    
		String stuId = stuAnswerDTO.getStuId();
		String testpaperId = stuAnswerDTO.getTestpaperId();
		
		Map<String, String> questionStuAnswerMap = stuAnswerDTO.getQuestionStuAnswerMap();
		//  要操作到数据库表的总行数：一条成绩加上总的新增学生作答记录的行数
		int standardRows = 1 + questionStuAnswerMap.size();
				
		//验证参数的合法性，不合法则直接返回错误信息 
		QueryBase queryBase = new QueryBase();
		queryBase.addParameter("userNum", stuId);
		if (stuId == null || "".equals(stuId) || userMapper.selectBySelective(queryBase) == null 
				|| testpaperId == null || "".equals(testpaperId) 
				|| examTestpaperMapper.selectByTestpaperId(testpaperId) == null || questionStuAnswerMap == null
				|| questionStuAnswerMap.size() == 0) {
			logger.error("调用ExamRecordServiceImpl.submitPaperAndUpdateGrade传入的参数有误,stuId={}, testpaperId={}, questionStuAnswerMap={}", 
					stuId, testpaperId, questionStuAnswerMap);
			return StatusType.DATA_INVALID.getValue();
		}
		
		Map<Object, Object> allAnswerAndScore = stringRedisTemplate.opsForHash().entries(testpaperId);
		
		
		int rows = txTemplate.execute(new TransactionCallback<Integer>() {

			@Override
			public Integer doInTransaction(TransactionStatus status) {
				int totalRows = 0;
				String questionId = "";
				String stuAnswer = ""; //某题目学生的答案
				String correctAnswer = ""; // 某题目的正确答案
				int score = 0; // 某题目对应的分值
				int stuGrade = 0;
				
				for (Map.Entry<String,String> entry : questionStuAnswerMap.entrySet()) {
					int stuScore = 0;
					
					questionId = entry.getKey();
					stuAnswer = entry.getValue();
					// 先查redis，若没有才查数据库，提高速度 
					if(allAnswerAndScore!=null && allAnswerAndScore.size()==(2*questionStuAnswerMap.size())){
						// redis缓存中有正确答案及题目分值 ，则不用去查数据库了
						correctAnswer =  (String) allAnswerAndScore.get(questionId);
						score = (int) allAnswerAndScore.get(questionId+"Score");
					}else if(questionId.substring(0, 2).equals(IdType.ChoiceQuestion.getSign())){  //redis缓存中没有该题目对应的分值和正确答案，要从数据库中查询
						 // 选择题
						ExamChioceQuestion examChioceQuestion = examChioceQuestionMapper.selectByQuestionId(questionId);
						if (examChioceQuestion != null) {
							correctAnswer = examChioceQuestion.getAnswer();
							score = examChioceQuestion.getScore();
						}else {
							// 题号对应的题目不存在 ，记录错误日志 
							logger.error("调用ExamRecordServiceImpl.submitPaperAndUpdateGrade传入的选择题题目id有误, questionId={}", questionId);
							// 回滚事务
							status.setRollbackOnly();
							return StatusType.DATA_INVALID.getValue();
						}
						
					}else if (questionId.substring(0, 2).equals(IdType.JudgeQuestion.getSign())) {
						// 判断题
						ExamJudgeQuestion examJudgeQuestion = examJudgeQuestionMapper.selectByQuestionId(questionId);
						if (examJudgeQuestion != null) {
							correctAnswer = examJudgeQuestion.getAnswer();
							score = examJudgeQuestion.getScore();
						}else {
							// 题号对应的题目不存在 ，记录错误日志 
							logger.error("调用ExamRecordServiceImpl.submitPaperAndUpdateGrade传入的判断题题目id有误, questionId={}", questionId);
							// 回滚事务
							status.setRollbackOnly();
							return StatusType.DATA_INVALID.getValue();
						}
					}else {
						// 题号不以CQ或JQ开头 ，题号无效，记录错误日志 
						logger.error("调用ExamRecordServiceImpl.submitPaperAndUpdateGrade传入的题目id有误, 题号不是以CQ或JQ开头，questionId={}", questionId);
						// 回滚事务
						status.setRollbackOnly();
						return StatusType.DATA_INVALID.getValue();
					}
					// 判断学生答案正确与否，并同时累加grade
					if (!correctAnswer.trim().equals("") && correctAnswer.trim().equals(stuAnswer.trim())) {
						// 学生答案正确，grade累加上该题对应的分值score 
						stuGrade += score;
						stuScore = score;
					}
					// 将学生的答题记录插入exam_record表中，先判断是否已有记录，若有直接更新学生答案，学生得分，更新时间等
					// 若无记录，才新插入一条 
					QueryBase queryBase = new QueryBase();
					queryBase.addParameter("stuId", stuId);
					queryBase.addParameter("testpaperId", testpaperId);
					queryBase.addParameter("questionId", questionId);
					List<ExamRecord> recordList = examRecordMapper.selectByPageAndSelections(queryBase);
					Date now = new Date();
					ExamRecord record = null;
					
					if (recordList != null) {
						// 更新对应的考试记录
						record = recordList.get(0);
						record.setUpdateTime(now);
						record.setStuAnswer(stuAnswer);
						record.setStuScore(stuScore);
						totalRows += examRecordMapper.updateByStuIdAndTestpaperIdAndQuestionIdSelective(record);
					}else {
						// 直接新插入一条新纪录
						record = new ExamRecord(stuId, questionId, testpaperId, stuAnswer, stuScore, now, now);
						totalRows += examRecordMapper.insertSelective(record);
					}
				}
				//  插入一条成绩 ,同样要先判断是否已有成绩记录，若有直接更新，否则新增一条成绩
				QueryBase queryBase = new QueryBase();
				queryBase.addParameter("stuId", stuId);
				queryBase.addParameter("testpaperId", testpaperId);
				List<ExamGrade> gradeList = examGradeMapper.selectByPageAndSelections(queryBase);
				ExamGrade grade = null;
				Date nowTime = new Date();
				if (gradeList != null && (grade=gradeList.get(0)) != null) {
					// 成绩存在，直接更新该成绩即可 
					grade.setUpdateTime(nowTime);
					grade.setGrade(stuGrade);
					totalRows += examGradeMapper.updateByStuIdAndTestpaperIdSelective(grade);
				}else {
					// 成绩不存在，新增一条成绩 
					grade = new ExamGrade(stuId, testpaperId, stuGrade, nowTime, nowTime);
					totalRows += examGradeMapper.insertSelective(grade);
				}
				return totalRows;
			}
			
			
		});
		return DAOResultUtil.getBatchResult(rows, standardRows).getValue();
	}

	// 通过试卷号和学号查询一个学生的某套试卷的作答记录  （可分页）
	@Override
	public void selectRecordByStuIdAndTestpaperIdByPage(QueryBase queryBase) {

		queryBase.setTotalRow(examRecordMapper.size(queryBase.getParameters()));  // 获取查询到的总的作答记录数 
		
		List<ChoiceQuestionRecordDTO> choiceQuestionRecordList = examRecordMapper
				.selectChoiceAndRecordByPageAndStuIdAndTestpaperId(queryBase);
		List<JudgeQuestionRecordDTO> judgeQuestionRecordList = examRecordMapper
				.selectJudgeAndRecordByPageAndStuIdAndTestpaperId(queryBase);
		
		Map<String, List<? extends Object>> resultMap = new HashMap<String, List<? extends Object>>();
		resultMap.put("choiceQuestionRecordList", choiceQuestionRecordList);
		resultMap.put("judgeQuestionRecordList", judgeQuestionRecordList);
		queryBase.setResultMap(resultMap);
	}	

}