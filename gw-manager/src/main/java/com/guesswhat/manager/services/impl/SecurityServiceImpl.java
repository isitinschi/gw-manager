package com.guesswhat.manager.services.impl;

import java.nio.charset.Charset;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;

import com.guesswhat.manager.services.face.SecurityService;
import com.guesswhat.manager.utils.MessageDialog;
import com.guesswhat.manager.utils.PropertyReader;
import com.guesswhat.manager.utils.UserRole;

public class SecurityServiceImpl implements SecurityService {

	private String getSecurityUrl() {
		String host = PropertyReader.getProperty("server.host");
		String port = PropertyReader.getProperty("server.port");
		String url = "http://" + host + ":" + port + "/security";
		return url;
	}
	
	@Override
	public String getAdminAuthorization() {
		String username = PropertyReader.getProperty("user.admin.login");
		String password = PropertyReader.getProperty("user.admin.password");
		return getAuthorization(username,password);
	}
	
	@Override
	public String getReaderAuthorization() {
		String username = PropertyReader.getProperty("user.reader.login");
		String password = PropertyReader.getProperty("user.reader.password");
		return getAuthorization(username,password);
	}
	
	@Override
	public String getWriterAuthorization() {
		String username = PropertyReader.getProperty("user.writer.login");
		String password = PropertyReader.getProperty("user.writer.password");
		return getAuthorization(username,password);
	}
	
	private String getAuthorization(String username, String password) {
		return "Basic " + new String(Base64.encode(String.valueOf(username + ":" + password).getBytes()), Charset.forName("ASCII"));
	}

	@Override
	public void handshake() {
		Client client = ClientBuilder.newClient();		
		WebTarget webTarget = client.target(getSecurityUrl());

		// admin
		String username = PropertyReader.getProperty("user.admin.login");
		String password = PropertyReader.getProperty("user.admin.password");
		
		Form f = new Form();
		f.param("username", username);
		f.param("password", password);
		Builder invocationBuilder = webTarget.path("create").path("admin").request();	
		Response response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("create", "admin user");
			return;
		}

		// reader
		username = PropertyReader.getProperty("user.reader.login");
		password = PropertyReader.getProperty("user.reader.password");
		f = new Form();
		f.param("username", username);
		f.param("password", password);
		f.param("role", UserRole.READER.toString());
		
		invocationBuilder = webTarget.path("delete").path(username).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.delete();
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("delete", "reader user");
			return;
		}
		
		invocationBuilder = webTarget.path("create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("create", "reader user");
			return;
		}

		// writer
		username = PropertyReader.getProperty("user.writer.login");
		password = PropertyReader.getProperty("user.writer.password");
		f = new Form();
		f.param("username", username);
		f.param("password", password);
		f.param("role", UserRole.WRITER.toString());

		invocationBuilder = webTarget.path("delete").path(username).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.delete();
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("delete", "writer user");
			return;
		}
		
		invocationBuilder = webTarget.path("create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("create", "writer user");
			return;
		}
	}
	
}
