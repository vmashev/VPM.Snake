package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;

public class NewGameCommand extends CommandExecuteStrategy{

	public NewGameCommand(GameInfo gameInfo) {
		super(gameInfo);
	}
	
	@Override
	public Command execute(Command requestCommand) {
		gameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		
		gameInfo.createSnake(new String[] {gameInfo.getHostUsername()});
		gameInfo.setApple(gameInfo.generateApple());
		gameInfo.setStatus(GameStatus.Ready);
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		Command responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
		return responseCommand;
	}
	
}
