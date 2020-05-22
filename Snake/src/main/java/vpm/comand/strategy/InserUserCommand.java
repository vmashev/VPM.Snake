package vpm.comand.strategy;

import javax.validation.ValidationException;

import vpm.helper.Command;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

public class InserUserCommand extends CommandExecuteStrategy{

	@Override
	public Command execute(Command requestCommand) {
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
		
		Command responseCommand = new Command(requestCommand.getNumber(), jsonMessage);
		responseCommand.setErrorMessage(errorMessage);
		
		return responseCommand;
	}

}
