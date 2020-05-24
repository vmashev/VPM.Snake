package vpm.command;

import javax.validation.ValidationException;

import vpm.helper.CommunicationCommand;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

//Processed on the server for inserting new user
//Input: UserEntity
//Output: UserEntity or null, errorMessage if the user already exist
public class InserUserCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		String jsonMessage = null;
		String errorMessage = null;
		
		UserEntity requestUserEntity = UserEntity.parseJsonToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		
		try {
			userService.create(requestUserEntity);
			jsonMessage = requestCommand.getMessage();	
			
		} catch (ValidationException e) {
			errorMessage = e.getMessage();
		}
		
		CommunicationCommand responseCommand = new CommunicationCommand(null, 0, jsonMessage);
		responseCommand.setErrorMessage(errorMessage);
		
		return responseCommand;
	}

}
