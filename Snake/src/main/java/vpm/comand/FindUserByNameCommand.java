package vpm.comand;

import vpm.helper.CommunicationCommand;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

//Processed on the server for finding user by name
//Input: UserEntity - username
//Output: UserEntity or null
public class FindUserByNameCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		UserEntity requestUserEntity = UserEntity.parseJsonToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		requestUserEntity = userService.findByUsername(requestUserEntity.getUsername());
		
		String jsonMessage = null;
		if(requestUserEntity != null) {
			jsonMessage = requestUserEntity.parseToJson();
		}
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		return responseCommand;
	}

}
