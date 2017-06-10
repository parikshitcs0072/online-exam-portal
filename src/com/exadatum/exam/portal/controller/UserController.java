package com.exadatum.exam.portal.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exadatum.exam.portal.pojo.LoginDetail;
import com.exadatum.exam.portal.pojo.Question;
import com.exadatum.exam.portal.pojo.User;
import com.exadatum.exam.portal.pojo.UserQuestionMap;
import com.exadatum.exam.portal.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	private String fileLocation = "/home/parikshit/Desktop/answers";

	public String showLoginPage() {
		return "login";
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public String authenticateUser(@RequestBody String loginCredential) {
		LoginDetail loginDetail = null;
		User user = new User();
		ObjectMapper obj = new ObjectMapper();
		try {
			loginDetail = obj.readValue(loginCredential, LoginDetail.class);
			System.out.println(loginDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setEmailId(loginDetail.getUserId());
		user.setPassword(loginDetail.getPassword());
		boolean isValid = service.authenticateUser(user,
				loginDetail.getLoginTime());
		if (isValid)
			return "Valid Login";
		return "Invalid Login";
	}

	@RequestMapping(value = "/uploadanswer", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	public ResponseEntity<String> uploadAnswer(
			@RequestParam("userId") String userId,
			@RequestParam("qId") String questionId,
			@RequestParam("submitTime") String submitTime,
			@RequestParam("questionnaireID") String questionnaireId,
			@RequestParam("file") MultipartFile inputFile) {
		HttpHeaders headers = new HttpHeaders();
		UserQuestionMap obj = null;
		if (!inputFile.isEmpty()) {
			try {
				String originalFilename = inputFile.getOriginalFilename();
				String location = fileLocation +"/"+userId +"/"+ questionId;
				File destinationFile = new File(location + File.separator
						+ originalFilename);
				destinationFile.mkdirs();
				inputFile.transferTo(destinationFile);
				obj = new UserQuestionMap(userId,questionnaireId,questionId,submitTime,location);
				System.out.println(obj);
				service.uploadAnswer(obj);
				System.out.println("back to controller");
				headers.add("File Uploaded Successfully - ", originalFilename);
				return new ResponseEntity<String>("fileuploaded", headers,
						HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/startexam",method = RequestMethod.POST)
	public String startExam(@RequestParam("userId") String userId,
			@RequestParam("examId") String examId,
			@RequestParam("examStartTime") String examStartTime) {
		Map<String,List<Question>> questionnaire = service.startExam(userId, examId, examStartTime);
		System.out.println(questionnaire);
		return "";
	}

	@RequestMapping(value = "/helloworld")
	public String helloWorld() {
		return "Hello World";
	}
}
