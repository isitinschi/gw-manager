package com.guesswhat.manager.service.impl;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.dto.RecordDTOListWrapper;
import com.guesswhat.manager.service.face.RecordService;
import com.guesswhat.manager.utils.MessageDialog;
import com.guesswhat.manager.utils.PropertyReader;

public class RecordServiceImpl implements RecordService {
	
	@Autowired
	private SecurityServiceImpl securityService;
	
	private String getRecordUrl() {
		String host = PropertyReader.getProperty("server.host");
		String port = PropertyReader.getProperty("server.port");
		String recordUrl = "http://" + host + ":" + port + "/records";
		return recordUrl;
	}
	
	@Override
	public RecordDTOListWrapper downloadRecordBackup() {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("backup").path("download").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("download", "record backup");
		}
		
		RecordDTOListWrapper recordDTOListWrapper = response.readEntity(RecordDTOListWrapper.class);
		
		return recordDTOListWrapper;
	}
	
	@Override
	public void uploadRecordBackup(RecordDTOListWrapper recordDTOListWrapper) {
		deleteRecords();
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("backup").path("upload").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());
		
		response = invocationBuilder.post(Entity.entity(recordDTOListWrapper, MediaType.APPLICATION_JSON_TYPE));
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("upload", "record backup");
		}
	}
	
	private void deleteRecords() {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("delete").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization());
		
		response = invocationBuilder.delete();
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("delete", "records");
		}
	}

	public void setSecurityService(SecurityServiceImpl securityService) {
		this.securityService = securityService;
	}
	
}
