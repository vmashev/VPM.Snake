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
		gameInfo.setApple(gameInfo.generateApple());
		gameInfo.createSnake(new String[] {gameInfo.getHostUsername(),"vm2"});
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		Command responseCommand = new Command(1, jsonMessage);
		
		return responseCommand;
	}
	
}
