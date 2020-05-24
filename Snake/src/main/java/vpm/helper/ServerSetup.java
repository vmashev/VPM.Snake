package vpm.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServerSetup {

	private static ServerSetup instance = new ServerSetup();
	protected EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("VPM");	
	private int serverPort ;
	
	private ServerSetup() {
		setServerInfo();
	}
	
	public static ServerSetup createInstance() {
		return instance;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
	
	public EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
	
	public void closeEntitiManagerFactory() {
		entityManagerFactory.close();
	}

	public int getServerPort() {
		return serverPort;
	}

	private void setServerInfo() {
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appConfigPath = rootPath + "config.properties";
		Properties appProps = new Properties();
		
		try {

			appProps.load(new FileInputStream(appConfigPath));
		    serverPort = Integer.valueOf(appProps.getProperty("SERVER_PORT"));
			System.out.println(serverPort);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
