package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;

public class ResumerSavedGameCommand extends CommandExecuteStrategy{

	public ResumerSavedGameCommand(GameInfo gameInfo) {
		super(gameInfo);
	}
	
	@Override
	public Command execute(Command requestCommand) {
		GameInfo requestGameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		
		gameInfo = gameInfoService.findByDateTime(requestGameInfo.getDateTime());
		gameInfo.setStatus(GameStatus.Ready);
		gameInfo.getSnakes().put(gameInfo.getHostUsername(), gameInfo.getHostSnake());
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		Command responseCommand = new Command(0, jsonMessage);
		
		return responseCommand;
	}

}
