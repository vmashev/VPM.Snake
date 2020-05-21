package vpm.model.dao;

import java.time.LocalDateTime;
import java.util.List;

import vpm.model.GameInfo;

public interface GameInfoDao extends GenericDao<GameInfo, Long> {

	List<GameInfo> findByUsernames(String username);
	GameInfo findByDateTime(LocalDateTime dateTime);
}
