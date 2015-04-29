package com.guesswhat.manager.services.face;

import java.awt.image.BufferedImage;

public interface ImageService {
	
	BufferedImage downloadQuestionImage(String questionId);	
	BufferedImage downloadAnswerImage(String questionId);	
	
}
