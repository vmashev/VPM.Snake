package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;

//Thread for receiving game commands from the client during the game
//It is started on the server
//One per client
public class GameInHandler implements Runnable {

	private ClientConnection clientConnection;
	private ArrayBlockingQueue<CommunicationCommand> inputCommands;
	private CommunicationCommand command;
	private ObjectInputStream objectInput ;
	
	public GameInHandler(ClientConnection clientConnection, ArrayBlockingQueue<CommunicationCommand> inputCommands) throws IOException {
		this.clientConnection = clientConnection;
		this.inputCommands = inputCommands;
	}
	
	@Override
	public void run() {
		try {
			
			objectInput = clientConnection.getObjectInput();
			while(true) {
				
				command = (CommunicationCommand)objectInput.readObject();
				inputCommands.add(command);
				
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				objectInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

}
