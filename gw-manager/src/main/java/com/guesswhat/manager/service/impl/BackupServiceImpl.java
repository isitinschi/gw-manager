package com.guesswhat.manager.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesswhat.manager.dto.BackupDTO;
import com.guesswhat.manager.dto.ComposedQuestionDTO;
import com.guesswhat.manager.dto.RecordDTOListWrapper;
import com.guesswhat.manager.service.face.BackupService;
import com.guesswhat.manager.service.face.QuestionService;
import com.guesswhat.manager.service.face.RecordService;
import com.guesswhat.manager.utils.MessageDialog;

public class BackupServiceImpl implements BackupService {
	
	@Autowired
	private RecordService recordService;
	@Autowired
	private QuestionService questionService;
	
	public void downloadBackup(String path) {
		RecordDTOListWrapper recordBackup = recordService.downloadRecordBackup();
		List<ComposedQuestionDTO> questionBackup = questionService.downloadQuestionBackup();
		
		BackupDTO backupDTO = new BackupDTO();
		backupDTO.setQuestionDTOList(questionBackup);
		backupDTO.setRecordDTOList(recordBackup.getRecordDTOList());
		
		try (FileOutputStream fos = new FileOutputStream(path)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(backupDTO);
			}
		} catch (IOException e) {
			MessageDialog.showErrorDialog("download", "backup");
		}
	}
	
	public void uploadBackup(String path) {
		try (FileInputStream fis = new FileInputStream(path)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				BackupDTO backupDTO = (BackupDTO) ois.readObject();
				recordService.uploadRecordBackup(new RecordDTOListWrapper(backupDTO.getRecordDTOList()));
				questionService.uploadQuestionBackup(backupDTO.getQuestionDTOList());
			}
		} catch (Exception e) {
			MessageDialog.showErrorDialog("upload", "backup");
		}		
	}

	public void setRecordService(RecordService recordService) {
		this.recordService = recordService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
	
}
