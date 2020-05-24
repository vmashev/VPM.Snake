package vpm.comand;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.server.GameHandler;
import vpm.server.ServerConectionManager;

//Processed on the server for joining lobby
//Star GameHandler thread which was created from player one
//Input: username
//Output: GameInfo
public class JoinLobbyCommand extends GameCommand {

	public JoinLobbyCommand(ServerConectionManager gameManager) {
		super(gameManager);
	}

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		CommunicationCommand responseCommand = null;
		
		for (int i = 0; i < getServerConectionManager().getGameHandlers().size(); i++) {
			GameHandler gHandler = getServerConectionManager().getGameHandlers().get(i);
			if(gHandler.getGameInfo().getPlayerOne().getUsername().equals(requestCommand.getMessage())) {
				
				GameInfo gameInfo = gHandler.getGameInfo();
				gameInfo.setStatus(GameStatus.Ready);
				
				String message = gameInfo.parseToJson();
				responseCommand = new CommunicationCommand(0, message);
				
				getServerConectionManager().setRunnable(false);
				
				getServerConectionManager().getGameHandlers().remove(i);
				gHandler.getClients().add(new ClientConnection(requestCommand.getUsername(), 
																getServerConectionManager().getObjectOutput(), 
																getServerConectionManager().getObjectInput()));
				gHandler.getGameInfo().getSnakes().put(requestCommand.getUsername().getUsername(), Snake.createSnake(2, gameInfo.getWidth()));
				gHandler.getGameInfo().setPlayerTwo(requestCommand.getUsername());
				new Thread(gHandler).start();
				
				break;
			}
		}
		
		return responseCommand;
	}



}
