package com.guesswhat.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.dto.ComposedQuestionDTO;
import com.guesswhat.manager.dto.QuestionDTO;
import com.guesswhat.manager.service.face.QuestionService;
import com.guesswhat.manager.utils.MessageDialog;
import com.guesswhat.manager.utils.PropertyReader;

public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private SecurityServiceImpl securityService;
	
	private String getQuestionUrl() {
		String host = PropertyReader.getProperty("server.host");
		String port = PropertyReader.getProperty("server.port");
		String questionUrl = "http://" + host + ":" + port + "/questions";
		return questionUrl;
	}
	
	@Override
	public void createQuestion(ComposedQuestionDTO composedQuestionDTO) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());
		
		response = invocationBuilder.put(Entity.entity(composedQuestionDTO, MediaType.APPLICATION_JSON_TYPE));
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("upload", "question");
		}
	}

	@Override
	public List<QuestionDTO> findQuestions() {
		String questionUrl = getQuestionUrl();
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(questionUrl);
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("findall").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getReaderAuthorization());
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("get", "questions");
			return null;
		}
		
		List<QuestionDTO> questions = response.readEntity(new GenericType<List<QuestionDTO>>() {});
		
		return questions;
	}

	@Override
	public void deleteQuestion(String questionId) {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("delete").path(questionId).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());		
		response = invocationBuilder.delete();
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("delete", "question");
		}
	}

	@Override
	public List<ComposedQuestionDTO> downloadQuestionBackup() {
		List<QuestionDTO> questions = findQuestions();
		List<ComposedQuestionDTO> composedQuestions = new ArrayList<ComposedQuestionDTO>();
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl()).path("download");
		Response response = null;
		for (QuestionDTO questionDTO : questions) {
			Builder invocationBuilder = webTarget.path(questionDTO.getId()).request();
			invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());
			
			response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
			if (response.getStatus() != 200) {
				MessageDialog.showErrorDialog("download", "question backup");
			}
			
			ComposedQuestionDTO composedQuestionDTO = response.readEntity(ComposedQuestionDTO.class);
			composedQuestions.add(composedQuestionDTO);
		}
		
		return composedQuestions;
	}

	@Override
	public void uploadQuestionBackup(List<ComposedQuestionDTO> composedQuestions) {
		deleteQuestions();
		for (ComposedQuestionDTO composedQuestionDTO : composedQuestions) {
			createQuestion(composedQuestionDTO);
		}
	}
	
	private void deleteQuestions() {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("delete").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());
		
		response = invocationBuilder.delete();
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("delete", "questions");
		}
	}
	
	public void setSecurityService(SecurityServiceImpl securityService) {
		this.securityService = securityService;
	}
	
}
