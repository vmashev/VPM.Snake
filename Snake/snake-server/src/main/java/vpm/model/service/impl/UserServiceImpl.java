package vpm.model.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.ValidationException;

import vpm.helper.ServerSetup;
import vpm.model.UserEntity;
import vpm.model.dao.UserDao;
import vpm.model.dao.postgres.UserDaoPostgres;
import vpm.model.service.UserService;

public class UserServiceImpl implements UserService {

	ServerSetup serverSetup = ServerSetup.createInstance();
	EntityManager entityManager;
	UserDao userDao;
	
	@Override
	public void create(UserEntity user) throws ValidationException {
		entityManager = serverSetup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		if(userDao.findByNickname(user.getUsername()) != null) {
			throw new ValidationException("User with Nickname " + user.getUsername() + " already exist.");
		}
		
		userDao.create(user);
		
		transaction.commit();
		entityManager.close();
	}

	@Override
	public void remove(UserEntity user) {
		entityManager = serverSetup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		userDao.makeTransient(user);
		
		transaction.commit();
		entityManager.close();
	}

	@Override
	public UserEntity update(UserEntity user) {
		entityManager = serverSetup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		UserEntity mergedUserEntity = userDao.merge(user);
		
		transaction.commit();
		entityManager.close();
		
		return mergedUserEntity;
	}

	@Override
	public List<UserEntity> findAll() {
		entityManager = serverSetup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		List<UserEntity> result = userDao.findAll();
		entityManager.close();
		
		return result;	
	}

	@Override
	public UserEntity findByUsername(String username) {
		entityManager = serverSetup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		UserEntity resultUserEntity = userDao.findByNickname(username);
		entityManager.close();
		
		return resultUserEntity;
		
	}

}
