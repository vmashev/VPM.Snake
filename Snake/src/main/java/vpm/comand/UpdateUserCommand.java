package vpm.comand;

import vpm.helper.CommunicationCommand;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

public class UpdateUserCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		requestUserEntity = userService.update(requestUserEntity);
		
		String jsonMessage = JsonParser.parseFromUserEntity(requestUserEntity);	
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		return responseCommand;
	}

}
