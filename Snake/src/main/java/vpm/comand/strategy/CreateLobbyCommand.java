package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;

public class CreateLobbyCommand extends CommandExecuteStrategy{

	public CreateLobbyCommand(GameInfo gameInfo) {
		super(gameInfo);
	}
	
	@Override
	public Command execute(Command requestCommand) {
		gameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		gameInfo.setStatus(GameStatus.WaitingForOpponent);
		
		//gameInfo.createSnake(new String[] {gameInfo.getHostUsername()});
		//gameInfo.setApple(gameInfo.generateApple());
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		Command responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
		return responseCommand;
	}
	
}
