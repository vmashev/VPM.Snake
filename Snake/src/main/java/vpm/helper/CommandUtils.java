package vpm.helper;

import javax.validation.ValidationException;

import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
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
		case 3: // Insert User
			updateUserEntity();
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
		requestUserEntity = userService.findByNickname(requestUserEntity.getUsername());
		
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
				requestGameInfo.update(0);
			}
			break;
		case SetPause:
			requestGameInfo.setStatus(GameStatus.Pause);
			break;
		case Save:
			//Save open game for SP
			break;	
		default:
			break;
		}
		
		String jsonMessage = JsonParser.parseFromGameInfo(requestGameInfo);	
		responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		
	}
}
