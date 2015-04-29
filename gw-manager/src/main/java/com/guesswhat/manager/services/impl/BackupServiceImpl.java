package com.guesswhat.manager.services.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.services.face.BackupService;
import com.guesswhat.manager.utils.MessageDialog;
import com.guesswhat.manager.utils.PropertyReader;

public class BackupServiceImpl implements BackupService {
	
	@Autowired
	private SecurityServiceImpl securityService;
	
	private String getBackupUrl() {
		String host = PropertyReader.getProperty("server.host");
		String port = PropertyReader.getProperty("server.port");
		String backupUrl = "http://" + host + ":" + port + "/backup";
		return backupUrl;
	}
	
	public void downloadBackup(String path) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getBackupUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("download").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization()).accept(MediaType.APPLICATION_OCTET_STREAM);
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("download", "backup");
			return;
		}
		
		byte[] backup = response.readEntity(byte [].class);
		
		try(FileOutputStream stream = new FileOutputStream(path)) {
		    stream.write(backup);
		} catch (IOException e) {
			MessageDialog.showErrorDialog("save", "backup");
		} 
	}
	
	public void uploadBackup(String path) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getBackupUrl());
		Response response = null;
		
		try (InputStream is = new FileInputStream(path)) {
			String sContentDisposition = "attachment; filename=\"" + "tempFile"+"\"";
			Builder invocationBuilder = webTarget.path("upload").request();
			invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getWriterAuthorization()).header("Content-Disposition", sContentDisposition);
			response = invocationBuilder.post(Entity.entity(is, MediaType.APPLICATION_OCTET_STREAM));
			if (response.getStatus() != 200) {
				MessageDialog.showErrorDialog("upload", "backup");
			}
		} catch (IOException e) {
			MessageDialog.showErrorDialog("read", "backup");
		}
		
	}

	public void setSecurityService(SecurityServiceImpl securityService) {
		this.securityService = securityService;
	}
	
}
