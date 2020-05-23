package vpm.comand.strategy;

import vpm.helper.Command;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

public class FindUserByNameCommand extends CommandExecuteStrategy{

	@Override
	public Command execute(Command requestCommand) {
		UserEntity requestUserEntity = JsonParser.parseToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		requestUserEntity = userService.findByUsername(requestUserEntity.getUsername());
		
		String jsonMessage = JsonParser.parseFromUserEntity(requestUserEntity);	
		Command responseCommand = new Command(0, jsonMessage);
		
		return responseCommand;
	}

}
