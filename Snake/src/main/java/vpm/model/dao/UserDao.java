package vpm.model.dao;

import java.util.List;

import vpm.model.UserEntity;

public interface UserDao extends GenericDao<UserEntity, Long> {

	UserEntity findByNickname(String username);
	List<UserEntity> findTopByWins(int top);
	
}
