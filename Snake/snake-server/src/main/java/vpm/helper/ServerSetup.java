package vpm.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServerSetup {

	private static ServerSetup instance = new ServerSetup();
	protected EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("VPM");	
	public static final int SERVER_PORT = 1914;
	
	private ServerSetup() {}
	
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

}
