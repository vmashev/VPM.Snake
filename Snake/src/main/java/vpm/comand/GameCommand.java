package vpm.comand;

import vpm.server.GameManager;

public abstract class GameCommand extends Command{

	private GameManager gameManager;
	
	public GameCommand(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

}
