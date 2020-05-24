package vpm.comand;

import vpm.helper.CommunicationCommand;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

//Processed on the server for updating user
//Input: UserEntity 
//Output: UserEntity 
public class UpdateUserCommand extends Command{

	@Override
	public CommunicationCommand execute(CommunicationCommand requestCommand) {
		UserEntity requestUserEntity = UserEntity.parseJsonToUserEntity(requestCommand.getMessage());
		
		UserService userService = new UserServiceImpl();
		requestUserEntity = userService.update(requestUserEntity);
		
		String jsonMessage = null;
		if(requestUserEntity != null) {
			jsonMessage = requestUserEntity.parseToJson();
		}
		CommunicationCommand responseCommand = new CommunicationCommand(0, jsonMessage);
		
		return responseCommand;
	}

}
