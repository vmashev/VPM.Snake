package vpm.comand;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.server.GameHandler;
import vpm.server.GameManager;

public class SingleplayerNewGameCommand extends GameCommand{


	public SingleplayerNewGameCommand(GameManager gameManager) {
		super(gameManager);
	}

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		gameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		gameInfo.getSnakes().put(requestCommand.getUsername().getUsername(), Snake.createSnake(1, gameInfo.getWidth()));
		gameInfo.setApple(gameInfo.generateApple());
		gameInfo.setStatus(GameStatus.Ready);
		gameInfo.setPlayerOne(requestCommand.getUsername());
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		GameHandler gHandler = new GameHandler(new ClientConnection(requestCommand.getUsername(), 
																	getGameManager().getObjectOutput(), 
																	getGameManager().getObjectInput()),gameInfo);
		new Thread(gHandler).start(); 
		getGameManager().setRunnable(false);
		
		return responseCommand;
	}
	
}
