package com.exadatum.exam.portal.service;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exadatum.exam.portal.dao.UserDao;
import com.exadatum.exam.portal.pojo.Question;
import com.exadatum.exam.portal.pojo.User;
import com.exadatum.exam.portal.pojo.UserQuestionMap;
@Service
@Transactional
public class UserService{
	@Autowired
	UserDao dao;
	
	public boolean authenticateUser(User user, String loginTime){
		System.out.println("in service layer");
		User authenticatedUser = dao.authenticateUser(user,loginTime);
		System.out.println(user);
		if(authenticatedUser != null)
			return true;
		return false;
	}

	public void uploadAnswer(UserQuestionMap obj) {
		dao.uploadAnswer(obj);
		System.out.println("in service");
	}
	
	public Map<String,List<Question>> startExam(String userId,String examId,String examStartTime){
		return dao.startExam(userId, examId, examStartTime);

	}

}
