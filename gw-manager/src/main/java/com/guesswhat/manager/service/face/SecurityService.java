package com.guesswhat.manager.service.face;

public interface SecurityService {

	String getAdminAuthorization();	
	String getReaderAuthorization();	
	String getWriterAuthorization();
	
	void handshake();
	
}
