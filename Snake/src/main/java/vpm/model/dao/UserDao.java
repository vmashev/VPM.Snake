package vpm.model.dao;

import java.util.List;

import vpm.model.UserEntity;

public interface UserDao extends GenericDao<UserEntity, String> {

	UserEntity findByNickname(String nickname);
	List<UserEntity> findTopByWins(int top);
	
}
