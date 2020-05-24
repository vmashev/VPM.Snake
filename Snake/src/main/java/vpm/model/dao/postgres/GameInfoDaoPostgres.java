package vpm.model.dao.postgres;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import vpm.helper.GameStatus;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.dao.GameInfoDao;

public class GameInfoDaoPostgres extends GenericDAOPostgreSQL<GameInfo, Long> implements GameInfoDao{

	public GameInfoDaoPostgres(EntityManager entityManager) {
		super(GameInfo.class, entityManager);
	}

	@Override
	public List<GameInfo> findSavedGameInfoByUsername(UserEntity user) {
		Query query = getEntityManager().createNamedQuery("findGameInfoByUsernameAndStatus");
		query.setParameter("user", user)
			.setParameter("status", GameStatus.Save);
		
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public GameInfo findByDateTime(LocalDateTime dateTime) {
		Query query = getEntityManager().createNamedQuery("findGameInfoByDateTime");
		query.setParameter("dateTime", dateTime);
		
		try {
			return (GameInfo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<GameInfo> findGameInfoHistoryByUsername(String username) {
		Query query = getEntityManager().createNamedQuery("findGameInfoByUsernameAndStatus");
		query.setParameter("username", username)
			.setParameter("status", GameStatus.GameOver);
		
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

}
