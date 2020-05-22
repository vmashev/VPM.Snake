package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;

public class FindGameByDateTimeCommand extends CommandExecuteStrategy{

	@Override
	public Command execute(Command requestCommand) {
		GameInfo requestGameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		GameInfo gameInfo = gameInfoService.findByDateTime(requestGameInfo.getDateTime());
        
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		Command responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
		return responseCommand;
	}

}
