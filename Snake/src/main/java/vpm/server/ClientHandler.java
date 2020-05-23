package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import vpm.comand.strategy.CommandExecuteFactory;
import vpm.comand.strategy.CommandExecuteStrategy;
import vpm.helper.Command;
import vpm.model.GameInfo;

public class ClientHandler implements Runnable{

	Socket socket ;
	GameInfo gameInfo;
	
	ObjectOutputStream objectOutput ;
	ObjectInputStream objectInput ;
	
	public ClientHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.gameInfo = new GameInfo();
		
		this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
		this.objectInput = new ObjectInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				
				Command inCommand = (Command)objectInput.readObject();
				System.out.println("Received Command: " + inCommand.getNumber() + ", Message: " + inCommand.getMessage());
				
				CommandExecuteStrategy executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
				Command outCommand = inCommand.execute(executeStrategy);
				
				objectOutput.writeObject(outCommand);
				System.out.println("Send Json: " + outCommand.getNumber() + ", Message: " + outCommand.getMessage());

				if(executeStrategy.getGameInfo() != null) {
					gameInfo = executeStrategy.getGameInfo();
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectOutput.close();
				objectInput.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}	
	
	
	
}

	
	
	