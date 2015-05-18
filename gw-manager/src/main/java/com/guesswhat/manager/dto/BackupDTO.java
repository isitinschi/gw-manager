package com.guesswhat.manager.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class BackupDTO implements Serializable {

	private List<ComposedQuestionDTO> questionDTOList;
	private List<RecordDTO> recordDTOList;
	
	public BackupDTO() {
		
	}

	public List<ComposedQuestionDTO> getQuestionDTOList() {
		return questionDTOList;
	}

	public void setQuestionDTOList(List<ComposedQuestionDTO> questionDTOList) {
		this.questionDTOList = questionDTOList;
	}

	public List<RecordDTO> getRecordDTOList() {
		return recordDTOList;
	}

	public void setRecordDTOList(List<RecordDTO> recordDTOList) {
		this.recordDTOList = recordDTOList;
	}
		
}
