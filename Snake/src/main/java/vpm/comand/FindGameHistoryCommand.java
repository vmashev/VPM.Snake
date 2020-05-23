package vpm.comand;

import java.util.List;

import vpm.helper.CommunicationCommand;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;

public class FindGameHistoryCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		List<GameInfo> games = gameInfoService.findGameHistoryByUsername(requestUserEntity.getUsername());
        
		String jsonMessage = JsonParser.parseFromGameInfoList(games);	
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		return responseCommand;
	}

}
