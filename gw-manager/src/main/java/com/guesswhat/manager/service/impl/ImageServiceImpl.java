package com.guesswhat.manager.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.dto.ImageType;
import com.guesswhat.manager.service.face.ImageService;
import com.guesswhat.manager.utils.MessageDialog;
import com.guesswhat.manager.utils.PropertyReader;

public class ImageServiceImpl implements ImageService {
	
	@Autowired
	private SecurityServiceImpl securityService;
	
	private String getImageUrl() {
		String host = PropertyReader.getProperty("server.host");
		String port = PropertyReader.getProperty("server.port");
		String questionUrl = "http://" + host + ":" + port + "/images";
		return questionUrl;
	}

	@Override
	public BufferedImage downloadQuestionImage(String questionId) {
		String url = getImageUrl() + "/find/question/" + questionId + "/" + ImageType.XXHDPI;
		return downloadImage(url);
	}
	
	@Override
	public BufferedImage downloadAnswerImage(String questionId) {
		String url = getImageUrl() + "/find/answer/" + questionId + "/" + ImageType.XXHDPI;
		return downloadImage(url);
	}

	private BufferedImage downloadImage(String url) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = null;
		
		Builder invocationBuilder = webTarget.request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, securityService.getReaderAuthorization()).accept(MediaType.APPLICATION_OCTET_STREAM);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		if (response.getStatus() != 200) {
			MessageDialog.showErrorDialog("download", "image");
			return null;
		}
		
		byte[] bytes = response.readEntity(byte [].class);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(bais);
		} catch (IOException e) {
			MessageDialog.showErrorDialog("write", "image");
			return null;
		}
		
		return image;
	}

	public void setSecurityService(SecurityServiceImpl securityService) {
		this.securityService = securityService;
	}
	
}
