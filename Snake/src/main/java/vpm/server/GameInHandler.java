package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.helper.ClientConnection;
import vpm.helper.Command;

public class GameInHandler implements Runnable {

	private ClientConnection clientConnection;
	private ArrayBlockingQueue<Command> inputCommands;
	private Command command;
	private ObjectInputStream objectInput ;
	
	public GameInHandler(ClientConnection clientConnection, ArrayBlockingQueue<Command> inputCommands) throws IOException {
		this.clientConnection = clientConnection;
		this.inputCommands = inputCommands;
	}
	
	@Override
	public void run() {
		
		try {
			
			objectInput = new ObjectInputStream(clientConnection.getSocket().getInputStream());

			while(true) {
				
				command = (Command)objectInput.readObject();
				inputCommands.add(command);
				
				System.out.println("Received Command from: " + clientConnection.getUsername() + 
									", Number: " + command.getNumber() + 
									", Message: " + command.getMessage());

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
