package vpm.helper;

public class ClientSetup {

	private static ClientSetup instance = new ClientSetup();
	private String userName = null;
	
	private ClientSetup() {}
	
	public static ClientSetup createInstance() {
		return instance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
