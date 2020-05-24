package vpm.model.service;

import java.util.List;

import javax.validation.ValidationException;

import vpm.model.UserEntity;

public interface UserService {

	List<UserEntity> findAll();
	UserEntity findByUsername(String username);
	
	void create(UserEntity user) throws ValidationException;
	void remove(UserEntity user) ;	
	UserEntity update(UserEntity user) ;
	
}
