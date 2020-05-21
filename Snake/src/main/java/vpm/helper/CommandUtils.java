package vpm.helper;

import javax.validation.ValidationException;

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
		case 10: // Play... 
			//???
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
	
}
