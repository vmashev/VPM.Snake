package vpm.comand;

import java.util.ArrayList;
import java.util.List;

import vpm.helper.CommunicationCommand;
import vpm.model.GameInfo;
import vpm.server.GameHandler;
import vpm.server.GameManager;

//Processed on the server for finding created lobbies
//Input: null
//Output: List of GameInfo
public class GetLobbyCommand extends GameCommand {

	public GetLobbyCommand(GameManager gameManager) {
		super(gameManager);
	}

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		CommunicationCommand responseCommand;
		
		List<GameInfo> games = new ArrayList<GameInfo>();
		for (GameHandler gameHandler : getGameManager().getGameHandlers()) {
			games.add(gameHandler.getGameInfo());
		}
		String message = GameInfo.parseGameInfoListToJson(games);
		responseCommand = new CommunicationCommand(0, message);
		
		return responseCommand;
	}



}
