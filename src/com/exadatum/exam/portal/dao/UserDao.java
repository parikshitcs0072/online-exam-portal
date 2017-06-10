package com.exadatum.exam.portal.dao;

import java.util.List;
import java.util.Map;

import com.exadatum.exam.portal.pojo.Question;
import com.exadatum.exam.portal.pojo.User;
import com.exadatum.exam.portal.pojo.UserQuestionMap;

public interface UserDao {
	
	public Map<String,List<Question>> startExam(String userId,String examId,String startTime);
	public User authenticateUser(User user, String loginTime);
	public void uploadAnswer(UserQuestionMap obj);

}
