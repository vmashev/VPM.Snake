package vpm.comand.strategy;

import java.util.List;

import vpm.helper.Command;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;

public class FindSavedGamesCommand extends CommandExecuteStrategy{

	@Override
	public Command execute(Command requestCommand) {
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		List<GameInfo> games = gameInfoService.findSavedGamesByUsername(requestUserEntity.getUsername());
        
		String jsonMessage = JsonParser.parseFromGameInfoList(games);	
		Command responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
		return responseCommand;
	}

}
