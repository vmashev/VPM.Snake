package vpm.model.dao.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import vpm.model.UserEntity;
import vpm.model.dao.UserDao;

public class UserDaoPostgres extends GenericDAOPostgreSQL<UserEntity, String> implements UserDao{

	public UserDaoPostgres(EntityManager entityManager) {
		super(UserEntity.class, entityManager);
	}

	@Override
	public List<UserEntity> findTopByWins(int top) {
		Query query = getEntityManager().createNamedQuery("findTopUserByWins");
		query.setParameter("username", top);
		
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public UserEntity findByNickname(String username) {
		Query query = getEntityManager().createNamedQuery("findUserByNickname");
		query.setParameter("username", username);
		
		try {
			return (UserEntity)query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
