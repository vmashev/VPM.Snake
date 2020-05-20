package vpm.model.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.validation.ValidationException;

import vpm.helper.Setup;
import vpm.model.UserEntity;
import vpm.model.dao.UserDao;
import vpm.model.dao.postgres.UserDaoPostgres;
import vpm.model.service.UserService;

public class UserServiceImpl implements UserService {

	Setup setup = Setup.createInstance();
	EntityManager entityManager;
	UserDao userDao;
	
	@Override
	public void create(UserEntity user) throws ValidationException{
		entityManager = setup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		if(userDao.findByNickname(user.getNickname()) != null) {
			throw new RuntimeException("User with Nickname " + user.getNickname() + " already exist.");
		}
		
		userDao.create(user);
		
		transaction.commit();
		entityManager.close();
	}

	@Override
	public void remove(UserEntity user) throws ValidationException{
		entityManager = setup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		userDao.makeTransient(user);
		
		transaction.commit();
		entityManager.close();
	}

	@Override
	public UserEntity update(UserEntity user) throws ValidationException{
		entityManager = setup.getEntityManager();
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
		entityManager = setup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		entityManager.close();
		
		return userDao.findAll();	
	}

	@Override
	public UserEntity findByNickname(String nickname) {
		entityManager = setup.getEntityManager();
		userDao = new UserDaoPostgres(entityManager);
		
		UserEntity resultUserEntity ;
		
		resultUserEntity = userDao.findByNickname(nickname);
		
		entityManager.close();
		
		return resultUserEntity;
		
	}

}
