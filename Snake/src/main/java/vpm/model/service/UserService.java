package vpm.model.service;

import java.util.List;

import vpm.model.UserEntity;

public interface UserService {

	List<UserEntity> findAll();
	UserEntity findByNickname(String nickname);
	
	void create(UserEntity user) ;
	void remove(UserEntity user) ;	
	UserEntity update(UserEntity user) ;
	
}
