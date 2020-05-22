package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.model.GameInfo;

public abstract class CommandExecuteStrategy {
	
	protected GameInfo gameInfo;
	
	public CommandExecuteStrategy() {}
	public CommandExecuteStrategy(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}
	
	public abstract Command execute(Command requestCommand);
	
	public GameInfo getGameInfo() {
		return this.gameInfo;
	}
	
}
