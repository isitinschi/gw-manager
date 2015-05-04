package com.guesswhat.manager.service.face;

import java.awt.image.BufferedImage;

public interface ImageService {
	
	BufferedImage downloadQuestionImage(String questionId);	
	BufferedImage downloadAnswerImage(String questionId);	
	
}
