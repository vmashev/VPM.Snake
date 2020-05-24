package vpm.model.dao;

import java.time.LocalDateTime;
import java.util.List;

import vpm.model.GameInfo;
import vpm.model.UserEntity;

public interface GameInfoDao extends GenericDao<GameInfo, Long> {

	List<GameInfo> findSavedGameInfoByUsername(UserEntity user);
	List<GameInfo> findGameInfoHistoryByUsername(String username);
	GameInfo findByDateTime(LocalDateTime dateTime);
}
