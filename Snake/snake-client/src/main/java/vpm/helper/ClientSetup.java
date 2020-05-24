package vpm.helper;

import vpm.model.UserEntity;

public class ClientSetup {

	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 1914;
	
	private static ClientSetup instance = new ClientSetup();
	private UserEntity user = null;
	
	private ClientSetup() {
		//setServerInfo();
	}
	
	public static ClientSetup createInstance() {
		return instance;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}
