package vpm.command;

import vpm.server.ServerConectionManager;

public abstract class GameCommand extends Command{

	private ServerConectionManager serverConectionManager;

	public GameCommand(ServerConectionManager serverConectionManager) {
		this.serverConectionManager = serverConectionManager;
	}
	
	public ServerConectionManager getServerConectionManager() {
		return serverConectionManager;
	}

	public void setServerConectionManager(ServerConectionManager serverConectionManager) {
		this.serverConectionManager = serverConectionManager;
	}
	
}
