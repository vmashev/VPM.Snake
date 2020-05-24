package vpm.comand;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.server.GameHandler;
import vpm.server.GameManager;

public class JoinLobbyCommand extends GameCommand {

	public JoinLobbyCommand(GameManager gameManager) {
		super(gameManager);
	}

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		CommunicationCommand responseCommand = null;
		
		for (int i = 0; i < getGameManager().getGameHandlers().size(); i++) {
			GameHandler gHandler = getGameManager().getGameHandlers().get(i);
			if(gHandler.getGameInfo().getHostUsername().equals(requestCommand.getMessage())) {
				
				GameInfo gameInfo = gHandler.getGameInfo();
				gameInfo.setStatus(GameStatus.Ready);
				
				String message = JsonParser.parseFromGameInfo(gameInfo);
				responseCommand = new CommunicationCommand(0, message);
				
				getGameManager().setRunnable(false);
				
				getGameManager().getGameHandlers().remove(i);
				gHandler.getClients().add(new ClientConnection(requestCommand.getUsername(), getGameManager().getObjectOutput(), getGameManager().getObjectInput()));
				gHandler.getGameInfo().getSnakes().put(requestCommand.getUsername(), Snake.createSnake(2, gameInfo.getWidth()));
				gHandler.getGameInfo().setPlayerTwo(requestCommand.getUsername());
				new Thread(gHandler).start();
				
				break;
			}
		}
		
		return responseCommand;
	}



}
