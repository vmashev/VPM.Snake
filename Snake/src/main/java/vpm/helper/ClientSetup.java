package vpm.helper;

public class ClientSetup {

	private static ClientSetup instance = new ClientSetup();
	private String username = null;
	
	private ClientSetup() {}
	
	public static ClientSetup createInstance() {
		return instance;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
