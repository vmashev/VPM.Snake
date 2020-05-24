package vpm.command;

import vpm.helper.CommunicationCommand;
import vpm.model.GameInfo;

public abstract class Command {
	
	protected GameInfo gameInfo;
	
	public Command() {}
	public Command(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}
	
	public abstract CommunicationCommand execute(CommunicationCommand requestCommand);
	
	public GameInfo getGameInfo() {
		return this.gameInfo;
	}
	
}
