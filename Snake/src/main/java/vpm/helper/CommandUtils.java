package vpm.helper;

import java.util.List;

import javax.validation.ValidationException;

import org.jboss.logging.Message;

import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.GameInfoService;
import vpm.model.service.UserService;
import vpm.model.service.impl.GameInfoServiceImpl;
import vpm.model.service.impl.UserServiceImpl;

public class CommandUtils {

	private static Command requestCommand;
	private static Command responseCommand;
	
	public static Command execute(Command command) {
		requestCommand = command;
		responseCommand = null;
		
		switch (command.getNumber()) {
		case 1: // Find User by Nickname
			getUserEntity();
			break;
		case 2: // Insert User
			inserUserEntity();
			break;
		case 3: // Update User
			updateUserEntity();
			break;
		case 4: // Get saved games to list
			getSavedGames();
			break;
		case 5: // Get selected game to resume
			getGameByDateTime();
			break;			
		case 10: // New Game
			newGame();
			break;
		case 11: // Play Game
			playGame();
			break;			
		default:
			break;
		}
				
		return responseCommand;
	}
	
	private static void getUserEntity() {
		
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		requestUserEntity = userService.findByUsername(requestUserEntity.getUsername());
		
		String jsonMessage = JsonParser.parseFromUserEntity(requestUserEntity);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
	}
	
	private static void inserUserEntity() {
		String jsonMessage = null;
		String errorMessage = null;
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		
		
		try {
			userService.create(requestUserEntity);
			jsonMessage = requestCommand.getMessage();	
			
		} catch (ValidationException e) {
			errorMessage = e.getMessage();
		}
		
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		responseCommand.setErrorMessage(errorMessage);
	}	
	
	private static void updateUserEntity() {
		
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		requestUserEntity = userService.update(requestUserEntity);
		
		String jsonMessage = JsonParser.parseFromUserEntity(requestUserEntity);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
	}
	
	private static void newGame() {
		
		GameInfo requestGameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		requestGameInfo.setSnake(requestGameInfo.createSnake());
		requestGameInfo.setApple(requestGameInfo.generateApple());
		requestGameInfo.setStatus(GameStatus.Run);
		
		String jsonMessage = JsonParser.parseFromGameInfo(requestGameInfo);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
	}
	
	private static void playGame() {
		
		GameInfo requestGameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());
		
		if((requestGameInfo.getStatus() == null) || (requestGameInfo.getStatus() == GameStatus.Pause)){
			responseCommand = requestCommand;
			return;
		}

		switch (requestGameInfo.getStatus()) {
		case Run:
			if(requestGameInfo.getDirection() != null) {
				if(!requestGameInfo.update(0)) {
					requestGameInfo.setStatus(GameStatus.GameOver);
					saveGame(requestGameInfo);	
				}
			}
			break;
		case SetPause:
			requestGameInfo.setStatus(GameStatus.Pause);
			break;
		case Save:
			saveGame(requestGameInfo);
			requestGameInfo.setStatus(GameStatus.GameOver);
			break;	
		default:
			break;
		}
		
		String jsonMessage = JsonParser.parseFromGameInfo(requestGameInfo);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
	}
	
	private static void saveGame(GameInfo gameInfo) {
		GameInfoService gameInfoService = new GameInfoServiceImpl();
		gameInfoService.create(gameInfo);
	}
	
	private static void getSavedGames() {
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		List<GameInfo> games = gameInfoService.findSavedGamesByUsername(requestUserEntity.getUsername());
        
		String jsonMessage = JsonParser.parseFromGameInfoList(games);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
	}
	
	private static void getGameByDateTime() {
		GameInfo requestGameInfo = JsonParser.parseToGameInfo(requestCommand.getMessage());

		GameInfoService gameInfoService = new GameInfoServiceImpl();
		GameInfo gameInfo = gameInfoService.findByDateTime(requestGameInfo.getDateTime());
        
		String jsonMessage = JsonParser.parseFromGameInfo(gameInfo);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
	}
}
