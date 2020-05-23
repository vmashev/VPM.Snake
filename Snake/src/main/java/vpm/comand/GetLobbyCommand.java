package vpm.comand;

import java.util.ArrayList;
import java.util.List;

import vpm.helper.CommunicationCommand;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.server.GameHandler;
import vpm.server.GameManager;

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
		String message = JsonParser.parseFromGameInfoList(games);
		responseCommand = new CommunicationCommand(0, message);
		
		return responseCommand;
	}



}
