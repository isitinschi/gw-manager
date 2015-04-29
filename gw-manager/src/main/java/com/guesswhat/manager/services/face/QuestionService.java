package com.guesswhat.manager.services.face;

import java.util.List;

import com.guesswhat.manager.dto.ComposedQuestionDTO;
import com.guesswhat.manager.dto.QuestionDTO;

public interface QuestionService {
	
	void createQuestion(ComposedQuestionDTO composedQuestionDTO);
	List<QuestionDTO> findQuestions();
	void deleteQuestion(String questionId);
	
}
