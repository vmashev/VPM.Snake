package vpm.comand;

import java.util.Map;

import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.helper.SnakeMoveInfo;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.model.UserEntity;
import vpm.model.service.GameInfoService;
import vpm.model.service.UserService;
import vpm.model.service.impl.GameInfoServiceImpl;
import vpm.model.service.impl.UserServiceImpl;

//The command contains SnakeMoveInfo for one snake move during the game
//Return GameInfo with position of the snakes, apple and game status
//Processed on the server
public class MoveCommand extends Command{

	public MoveCommand(GameInfo gameInfo) {
		super(gameInfo);
	}
	
	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		CommunicationCommand responseCommand;
		SnakeMoveInfo requestSnakeMove = SnakeMoveInfo.parseJsonToSnakeMoveInfo(requestCommand.getMessage());
		
		//If the game is not started or it is in pause mode
		if((requestSnakeMove.getStatus() == null) || (requestSnakeMove.getStatus() == GameStatus.Pause)){
			responseCommand = new CommunicationCommand(0, null);
			return responseCommand;
		}

		switch (requestSnakeMove.getStatus()) {
		case Run:
			if(requestSnakeMove.getDirection() != null) {
				gameInfo.setStatus(GameStatus.Run);
				//Move snake or return false if has collison
				if(!gameInfo.updateSnake(requestSnakeMove.getUsername(), requestSnakeMove.getDirection())) {
					
					//Set winner if it is multiplayer
					if(gameInfo.getSnakes().size() > 1) {
						UserEntity winner = requestSnakeMove.getUsername().equals(gameInfo.getPlayerOne().getUsername()) ? gameInfo.getPlayerTwo():gameInfo.getPlayerOne();
						gameInfo.setWinnerPlayer(winner);
					}
					
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
		
		String jsonMessage = createDeltaGameInfo().parseToJson();	
		responseCommand = new CommunicationCommand(2, jsonMessage);
		
		return responseCommand;
	}

	private GameInfo createDeltaGameInfo() {
		GameInfo deltaGameInfo = new GameInfo();
		deltaGameInfo.setSnakes(gameInfo.getSnakes());
		deltaGameInfo.setApple(gameInfo.getApple());
		deltaGameInfo.setStatus(gameInfo.getStatus());
		
		return deltaGameInfo;
	}
	
	//Save the game on Quit or when the game is over
	private void saveGame(GameInfo gameInfo) {
		gameInfo.setPlayerOneSnake(gameInfo.getSnakes().get(gameInfo.getPlayerOne().getUsername()));
		
		updateMaxScore(gameInfo);
		
		GameInfoService gameInfoService = new GameInfoServiceImpl();
		gameInfoService.create(gameInfo);

	}

	//Update max score in UserEntity when the game is saved
	private void updateMaxScore(GameInfo gameInfo) {
		
		for (Map.Entry<String,Snake> snakeEntry : gameInfo.getSnakes().entrySet()) {
			
			UserService userService = new UserServiceImpl();
			UserEntity user = userService.findByUsername(snakeEntry.getKey());
			
			if(user != null) {
				if(user.getMaxScore() < snakeEntry.getValue().getScore()) {
					user.setMaxScore(snakeEntry.getValue().getScore());
					userService.update(user);
				}
			}
			
			if(snakeEntry.getKey().equals(gameInfo.getPlayerOne().getUsername())) {
				gameInfo.setPlayerOneScore(snakeEntry.getValue().getScore());
			} else {
				gameInfo.setPlayerTwoScore(snakeEntry.getValue().getScore());
			}
		}
		
		
	}
	
}
