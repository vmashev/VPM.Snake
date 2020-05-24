package vpm.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.model.GameInfo;


//Thread for sending response commands to a client during the game
//It is started on the server
//One per client
public class GameOutHandler implements Runnable {

	private ClientConnection clientConnection;
	private ArrayBlockingQueue<CommunicationCommand> outCommands;
	private ObjectOutputStream objectOutput ;
	
	public GameOutHandler(ClientConnection clientConnection) throws IOException {
		this.clientConnection = clientConnection;
		this.outCommands = new ArrayBlockingQueue<CommunicationCommand>(1000);
	}
	
	@Override
	public void run() {
		
		try {
			
			objectOutput = clientConnection.getObjectOutput();
			
			while(true) {
				CommunicationCommand command = outCommands.poll();
				
				if(command != null) {						
					objectOutput.writeObject(command);
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

	public void addCommand(CommunicationCommand command) {
		outCommands.add(command);
	}
}
