package com.guesswhat.manager.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.dto.ComposedQuestionDTO;
import com.guesswhat.manager.dto.ImageDTO;
import com.guesswhat.manager.dto.ImageType;
import com.guesswhat.manager.dto.QuestionDTO;
import com.guesswhat.manager.service.face.QuestionService;
import com.guesswhat.manager.service.face.SecurityService;
import com.guesswhat.manager.utils.ImageUtils;
import com.guesswhat.manager.utils.MessageDialog;

public class ManagerController {

	@Autowired
	private SecurityService securityService;
	@Autowired
	private QuestionService questionService;
	
	@PostConstruct
	public void initializeManager() {
		securityService.handshake(); // register users
	}

	public void uploadQuestion(QuestionDTO questionDTO, String imageQuestion, String imageAnswer) {
		if (checkQuestion(questionDTO, imageQuestion, imageAnswer)) {
			ComposedQuestionDTO composedQuestionDTO = new ComposedQuestionDTO();
			composedQuestionDTO.setQuestionDTO(questionDTO);
			composedQuestionDTO.setImageQuestionDTO(buildImageDTO(imageQuestion));
			composedQuestionDTO.setImageAnswerDTO(buildImageDTO(imageAnswer));
			
			questionService.createQuestion(composedQuestionDTO);
		}
	}

	private ImageDTO buildImageDTO(String imagePath) {
		if (imagePath == null) {
			return null;
		}
		
		ImageDTO imageDTO = new ImageDTO();		
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			MessageDialog.showErrorDialog("read", "image");
		}
		
		for (ImageType imageType : ImageType.values()) {
			bufferedImage = ImageUtils.scaleImage(bufferedImage, imageType);
			byte [] bytes = ImageUtils.convertImageToByteArray(bufferedImage);
			switch (imageType) {
				case XXHDPI:	imageDTO.setXxhdpiImageId(bytes);break;
				case XHDPI:		imageDTO.setXhdpiImageId(bytes);break;
				case HDPI:		imageDTO.setHdpiImageId(bytes);break;
				case MDPI:		imageDTO.setMdpiImageId(bytes);break;
				case LDPI:		imageDTO.setLdpiImageId(bytes);break;
				default:   		return null;
			}
		}
		
		if (imageDTO.getLdpiImageId() == null || imageDTO.getMdpiImageId() == null
				 || imageDTO.getHdpiImageId() == null  || imageDTO.getXhdpiImageId() == null  
				 || imageDTO.getXxhdpiImageId() == null) {
			MessageDialog.showErrorDialog("build", "image DTO");
			return null;
		}
		
		return imageDTO;
	}

	private boolean checkQuestion(QuestionDTO questionDTO, String imageQuestion, String imageAnswer) {
		boolean correct = true;

		String answer1 = questionDTO.getAnswer1();
		String answer2 = questionDTO.getAnswer2();
		String answer3 = questionDTO.getAnswer3();
		String answer4 = questionDTO.getAnswer4();
		String correctAnswer = questionDTO.getCorrectAnswer();

		// emptiness
		if (answer1 == null || answer1.equals("")) {
			correct = false;
		}
		if (answer2 == null || answer2.equals("")) {
			correct = false;
		}
		if (answer3 == null || answer3.equals("")) {
			correct = false;
		}
		if (answer4 == null || answer4.equals("")) {
			correct = false;
		}
		if (correctAnswer == null || correctAnswer.equals("")) {
			correct = false;
		}
		if (imageQuestion == null || imageQuestion.equals("")) {
			correct = false;
		}
		if (imageAnswer != null && imageAnswer.equals("")) {
			correct = false;
		}
		
		if (!correct) {
			return correct;
		}

		// duplication
		if (imageAnswer != null && imageAnswer.equals(imageQuestion)) {
			correct = false;
		}
		if (answer1.equals(answer2) || answer1.equals(answer3)
				|| answer1.equals(answer4) || answer2.equals(answer3)
				|| answer2.equals(answer4) || answer3.equals(answer4)) {
			correct = false;
		}
		
		// resolution
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imageQuestion));
		} catch (IOException e) {
			MessageDialog.showErrorDialog("read", "image");
		}
		if (bufferedImage.getWidth() != 1920 || bufferedImage.getHeight() != 1080) {
			correct = false;
		}
		
		if (imageAnswer != null) {
			bufferedImage = null;
			try {
				bufferedImage = ImageIO.read(new File(imageAnswer));
			} catch (IOException e) {
				MessageDialog.showErrorDialog("read", "image");
			}
			if (bufferedImage.getWidth() != 1920 || bufferedImage.getHeight() != 1080) {
				correct = false;
			}
		}

		if (!correct) {
			JOptionPane.showMessageDialog(null, "Check all data!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}

		return correct;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
	
}
