package vpm.comand;

import javax.validation.ValidationException;

import vpm.helper.CommunicationCommand;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

public class InserUserCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
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
		
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		responseCommand.setErrorMessage(errorMessage);
		
		return responseCommand;
	}

}
