package vpm.model.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ValidationException;


import vpm.model.GameInfo;

public interface GameInfoService {

	List<GameInfo> findSavedGamesByUsername(String username);
	GameInfo findById(Long id);
	GameInfo findByDateTime(LocalDateTime dateTime);
	
	void create(GameInfo gameInfo) throws ValidationException;
	void remove(GameInfo gameInfo) ;	
	GameInfo update(GameInfo gameInfo) ;
}
