package com.guesswhat.manager.service.face;

import com.guesswhat.manager.dto.RecordDTOListWrapper;

public interface RecordService {
	
	RecordDTOListWrapper downloadRecordBackup();
	void uploadRecordBackup(RecordDTOListWrapper recordDTOListWrapper);
	
}
