package vpm.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import vpm.model.UserEntity;

public class ClientSetup {

	private String serverIp ;
	private int serverPort ;
	
	private static ClientSetup instance = new ClientSetup();
	private UserEntity user = null;
	
	private ClientSetup() {
		setServerInfo();
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

	private void setServerInfo() {
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appConfigPath = rootPath + "config.properties";
		Properties appProps = new Properties();
		
		try {

			appProps.load(new FileInputStream(appConfigPath));
		    serverPort = Integer.valueOf(appProps.getProperty("SERVER_PORT"));
			serverIp = appProps.getProperty("SERVER_IP");
			
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	public String getServerIp() {
		return serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}
	
}
