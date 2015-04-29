package com.guesswhat.manager.services.face;

public interface SecurityService {

	String getAdminAuthorization();	
	String getReaderAuthorization();	
	String getWriterAuthorization();
	
	void handshake();
	
}
