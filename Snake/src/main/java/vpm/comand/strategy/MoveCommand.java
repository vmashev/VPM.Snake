package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.helper.SnakeMoveInfo;
import vpm.model.GameInfo;
import vpm.model.service.GameInfoService;
import vpm.model.service.impl.GameInfoServiceImpl;

public class MoveCommand extends CommandExecuteStrategy{

	public MoveCommand(GameInfo gameInfo) {
		super(gameInfo);
	}
	
	@Override
	public Command execute(Command requestCommand) {
		Command responseCommand;
		SnakeMoveInfo requestSnakeMove = JsonParser.parseToSnakeMoveInfo(requestCommand.getMessage());
		
		if((requestSnakeMove.getStatus() == null) || (requestSnakeMove.getStatus() == GameStatus.Pause)){
			responseCommand = requestCommand;
			responseCommand.setMessage(null);
			return responseCommand;
		}

		switch (requestSnakeMove.getStatus()) {
		case Run:
			if(requestSnakeMove.getDirection() != null) {
				gameInfo.setStatus(GameStatus.Run);
				if(!gameInfo.updateSnake(requestSnakeMove.getUsername(), requestSnakeMove.getDirection())) {
					
					gameInfo.setStatus(GameStatus.GameOver);
					saveGame(gameInfo);	
				}
			}
			break;
		case SetPause:
			gameInfo.setStatus(GameStatus.Pause);
			break;
		case Save:
			gameInfo.setStatus(GameStatus.Save);
			saveGame(gameInfo);
			gameInfo.setStatus(GameStatus.GameOver);
			break;	
		default:
			break;
		}
		
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
		return responseCommand;
	}

	private static void saveGame(GameInfo gameInfo) {
		gameInfo.setHostSnake(gameInfo.getSnakes().get(gameInfo.getHostUsername()));
		
		GameInfoService gameInfoService = new GameInfoServiceImpl();
		gameInfoService.create(gameInfo);
	}

}
