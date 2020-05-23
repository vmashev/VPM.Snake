package vpm.comand;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.server.GameHandler;
import vpm.server.GameManager;

public class CreateLobbyCommand extends GameCommand{

	public CreateLobbyCommand(GameManager gameManager) {
		super(gameManager);
	}

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		gameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		gameInfo.setStatus(GameStatus.WaitingForOpponent);
		gameInfo.setApple(gameInfo.generateApple());
		gameInfo.getSnakes().put(requestCommand.getUsername(), Snake.createSnake(1, gameInfo.getWidth()));
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		CommunicationCommand responseCommand = new CommunicationCommand(1, jsonMessage);
		
		GameHandler gHandler = new GameHandler(new ClientConnection(requestCommand.getUsername(), 
																	getGameManager().getObjectOutput(), 
																	getGameManager().getObjectInput()),gameInfo);
		getGameManager().getGameHandlers().add(gHandler);
		getGameManager().setRunnable(false);
		
		return responseCommand;
	}
	
}
