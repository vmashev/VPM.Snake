package vpm.comand;

//Processed on the server for creating new multiplayer game
//Create GameHandler thread and wait for opponent
//Input: GameInfo 
//Output: GameInfo - position of the snake and apple, and game status = WaitingForOpponent
import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
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
		gameInfo = GameInfo.parseJsonToGameInfo(requestCommand.getMessage());
		gameInfo.setStatus(GameStatus.WaitingForOpponent);
		gameInfo.setApple(gameInfo.generateApple());
		gameInfo.getSnakes().put(requestCommand.getUsername().getUsername(), Snake.createSnake(1, gameInfo.getWidth()));
		gameInfo.setPlayerOne(requestCommand.getUsername());
		
		String jsonMessage = gameInfo.parseToJson();	
		CommunicationCommand responseCommand = new CommunicationCommand(1, jsonMessage);
		
		GameHandler gHandler = new GameHandler(new ClientConnection(requestCommand.getUsername(), 
																	getGameManager().getObjectOutput(), 
																	getGameManager().getObjectInput()),gameInfo);
		getGameManager().getGameHandlers().add(gHandler);
		getGameManager().setRunnable(false);
		
		return responseCommand;
	}
	
}
