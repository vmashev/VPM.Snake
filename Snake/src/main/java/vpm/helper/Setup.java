package vpm.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.bytebuddy.asm.Advice.This;

public class Setup {

	private static Setup instance = new Setup();
	private String userName = null;
	protected EntityManagerFactory entityManagerFactory = null;	
	
	private Setup() {}
	
	public static Setup createInstance() {
		return instance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		if(entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("VPM");
		}
		
		return entityManagerFactory;
	}
	
	public EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
	
	public void closeEntitiManagerFactory() {
		entityManagerFactory.close();
	}

}
