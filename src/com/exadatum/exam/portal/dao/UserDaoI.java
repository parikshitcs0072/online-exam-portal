package com.exadatum.exam.portal.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exadatum.exam.portal.pojo.Question;
import com.exadatum.exam.portal.pojo.User;
import com.exadatum.exam.portal.pojo.UserExamMapping;
import com.exadatum.exam.portal.pojo.UserQuestionMap;

import static com.exadatum.exam.portal.utility.Utility.*;
@Repository
public class UserDaoI implements UserDao {

	@Autowired
	private SessionFactory sf;
	@Override
	public User authenticateUser(User user, String loginTime) {
		System.out.println("in dao layer");
		User u = (User) sf
				.getCurrentSession()
				.createQuery(
						"select c from TB_EXAMINEE c where c.emailId=:aid")
				.setParameter("aid", user.getEmailId())
				.uniqueResult();
		System.out.println("User POJO" + u);
		if (u.getPassword().equals(user.getPassword())){
			//TODO Verify allocated login time
			return u;
		}
		return null;
	}

	@Override
	public void uploadAnswer(UserQuestionMap obj) {
		Session session = sf.getCurrentSession();
/*		Transaction txn = session.beginTransaction(); 
		txn.begin();*/
		session.persist(obj);
/*		session.flush();
		txn.commit();*/
	}

	@Override
	public Map<String, List<Question>> startExam(String userId, String examId,
			String startTime) {
		UserExamMapping usrExMp = (UserExamMapping)sf
		.getCurrentSession()
		.createQuery(
				"select c from TB_EXAM_USER_MAP c where c.userId=:aid and c.examId=:bid")
		.setParameter("aid",userId).setParameter("bid", examId)
		.uniqueResult();
		//TODO update exam start time
		usrExMp.setUserExamStartTime(startTime);
		@SuppressWarnings("unchecked")
		List<String> questionNoList =(List<String>)sf
		.getCurrentSession()
		.createQuery(
				"select c.qNo from TB_QUESTIONNAIRE c where c.qId=:aid")
		.setParameter("aid",usrExMp.getQuestionnaireId())
		.list();
		System.out.println(questionNoList);
		List<Question> questionList = new ArrayList<Question>();
		for(String qno:questionNoList){
			Question question =	(Question)sf.getCurrentSession().load(Question.class,qno);
			questionList.add(question);
		}
		System.out.println(questionList);
		Map<String,List<Question>> questionnaire = new HashMap<String, List<Question>>();
		questionnaire.put(usrExMp.getQuestionnaireId(), questionList);
		return questionnaire;
	}	
}
