package vpm.comand;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.model.GameInfo;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;
import vpm.server.GameHandler;
import vpm.server.ServerConectionManager;

//Processed on the server resuming saved game
//Start new GameHandler
//Input: GameInfo
//Output: GameInfo
public class ResumerSavedGameCommand extends GameCommand{

	public ResumerSavedGameCommand(ServerConectionManager gameManager) {
		super(gameManager);
	}

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		GameInfo requestGameInfo = GameInfo.parseJsonToGameInfo(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		
		gameInfo = gameInfoService.findByDateTime(requestGameInfo.getDateTime());
		gameInfo.setStatus(GameStatus.Ready);
		gameInfo.getSnakes().put(gameInfo.getPlayerOne().getUsername(), gameInfo.getPlayerOneSnake());
		
		String jsonMessage = gameInfo.parseToJson();	
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		GameHandler gHandler = new GameHandler(new ClientConnection(requestCommand.getUsername(), 
																	getServerConectionManager().getObjectOutput(), 
																	getServerConectionManager().getObjectInput()),gameInfo);
		new Thread(gHandler).start(); 
		getServerConectionManager().setRunnable(false);	
		
		return responseCommand;
	}

}
