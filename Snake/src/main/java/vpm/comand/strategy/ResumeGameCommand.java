package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;

public class ResumeGameCommand extends CommandExecuteStrategy{

	public ResumeGameCommand(GameInfo gameInfo) {
		super(gameInfo);
	}
	
	@Override
	public Command execute(Command requestCommand) {
		gameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
Command responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
		return responseCommand;
	}

}
