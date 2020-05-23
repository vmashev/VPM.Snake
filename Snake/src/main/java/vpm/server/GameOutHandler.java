package vpm.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.helper.ClientConnection;
import vpm.helper.Command;
import vpm.model.GameInfo;

public class GameOutHandler implements Runnable {

	private ClientConnection clientConnection;
	private ArrayBlockingQueue<Command> outCommands;
	private ObjectOutputStream objectOutput ;
	
	public GameOutHandler(ClientConnection clientConnection) throws IOException {
		this.clientConnection = clientConnection;
		this.outCommands = new ArrayBlockingQueue<Command>(1000);
	}
	
	@Override
	public void run() {
		
		try {
			
			objectOutput = new ObjectOutputStream(clientConnection.getSocket().getOutputStream());
			
			while(true) {
				
				Command command = outCommands.poll();
				
				if(command != null) {						
					objectOutput.writeObject(command);
					System.out.println("Send Json to: " + clientConnection.getUsername() + 
									 	", Command: " + command.getNumber() + 
									 	", Message: " + command.getMessage());
				}
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				objectOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

	public void addCommand(Command command) {
		outCommands.add(command);
	}
}
