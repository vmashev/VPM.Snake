package vpm.comand;

import java.util.List;

import vpm.helper.CommunicationCommand;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;

//Processed on the server for finding game history for user
//Input: UserEntity 
//Output: List of GameInfo
public class FindGameHistoryCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		UserEntity requestUserEntity = UserEntity.parseJsonToUserEntity(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		List<GameInfo> games = gameInfoService.findGameHistoryByUsername(requestUserEntity);
        
		String jsonMessage = GameInfo.parseGameInfoListToJson(games);	
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		return responseCommand;
	}

}
