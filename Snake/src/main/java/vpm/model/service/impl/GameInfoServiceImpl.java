package vpm.model.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.validation.ValidationException;

import vpm.helper.ServerSetup;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.dao.GameInfoDao;
import vpm.model.dao.postgres.GameInfoDaoPostgres;
import vpm.model.service.GameInfoService;

public class GameInfoServiceImpl implements GameInfoService{

	ServerSetup serverSetup = ServerSetup.createInstance();
	EntityManager entityManager;
	GameInfoDao gameInfoDao;
	
	@Override
	public void create(GameInfo gameInfo) throws ValidationException {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		if(gameInfo.getId() != null) {
			gameInfoDao.merge(gameInfo);
		} else {
			gameInfoDao.create(gameInfo);
		}
		
		transaction.commit();
		entityManager.close();
	}

	@Override
	public void remove(GameInfo gameInfo) {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		gameInfoDao.makeTransient(gameInfo);
		
		transaction.commit();
		entityManager.close();
	}

	@Override
	public GameInfo update(GameInfo gameInfo) {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		GameInfo mergedGameInfo = gameInfoDao.merge(gameInfo);
		
		transaction.commit();
		entityManager.close();
		
		return mergedGameInfo;
	}
	
	@Override
	public List<GameInfo> findSavedGamesByUsername(UserEntity user) {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		List<GameInfo> result = gameInfoDao.findSavedGameInfoByUsername(user);
		entityManager.close();
		
		return result;
	}

	@Override
	public GameInfo findById(Long id) {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		GameInfo gameInfo = gameInfoDao.findById(id);
		entityManager.close();
		
		return gameInfo;
	}

	@Override
	public GameInfo findByDateTime(LocalDateTime dateTime) {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		GameInfo gameInfo = gameInfoDao.findByDateTime(dateTime);
		entityManager.close();
		
		return gameInfo;
	}

	@Override
	public List<GameInfo> findGameHistoryByUsername(UserEntity user) {
		entityManager = serverSetup.getEntityManager();
		gameInfoDao = new GameInfoDaoPostgres(entityManager);
		
		List<GameInfo> result = gameInfoDao.findGameInfoHistoryByUsername(user.getUsername());
		entityManager.close();
		
		return result;
	}

}
