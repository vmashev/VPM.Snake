package vpm.model.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ValidationException;


import vpm.model.GameInfo;
import vpm.model.UserEntity;

public interface GameInfoService {

	List<GameInfo> findSavedGamesByUsername(UserEntity user);
	List<GameInfo> findGameHistoryByUsername(UserEntity user);
	GameInfo findById(Long id);
	GameInfo findByDateTime(LocalDateTime dateTime);
	
	void create(GameInfo gameInfo) throws ValidationException;
	void remove(GameInfo gameInfo) ;	
	GameInfo update(GameInfo gameInfo) ;
}
