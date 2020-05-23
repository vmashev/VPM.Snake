package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import vpm.comand.strategy.CommandExecuteFactory;
import vpm.comand.strategy.CommandExecuteStrategy;
import vpm.helper.ClientConnection;
import vpm.helper.Command;
import vpm.model.GameInfo;

public class StartServerHandler implements Runnable{

	private Socket socket ;
	private GameInfo gameInfo;
	private ArrayList<GameHandler> gameHandlers;

	private GameHandler gHandler;
	private ObjectOutputStream objectOutput ;
	private ObjectInputStream objectInput ;
	private boolean runnable;
	
	public StartServerHandler(Socket socket, ArrayList<GameHandler> gameHandlers) {
		this.socket = socket;
		this.gameHandlers = gameHandlers;
	}
	
	@Override
	public void run() {
		runnable = true;
		
		try {
			
			this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
			this.objectInput = new ObjectInputStream(socket.getInputStream());

			while(runnable) {
				
				Command inCommand = (Command)objectInput.readObject();
				System.out.println("Received Command: " + inCommand.getNumber() + ", Message: " + inCommand.getMessage());
				
				CommandExecuteStrategy executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
				Command outCommand = inCommand.execute(executeStrategy);
				
				objectOutput.writeObject(outCommand);
				System.out.println("Send Json: " + outCommand.getNumber() + ", Message: " + outCommand.getMessage());

				if(executeStrategy.getGameInfo() != null) {
					gameInfo = executeStrategy.getGameInfo();
				}
				
				
				switch ("Command") {
				case "Singleplayer":
					gHandler = new GameHandler(new ClientConnection(inCommand.getMessage(), socket));
					new Thread(gHandler).run(); //start new game and close this thread..
					runnable = false;
					break;
				case "Get Lobby":
					inCommand.execute(executeStrategy); // returns gameHandlers...
					break;
				case "Join Lobby":
					inCommand.execute(executeStrategy); // return index 
					int index= 0;
					gameHandlers.get(index);
					new Thread(gHandler).run(); //start new game and close this thread..
					break;
				case "New Lobby":
					gHandler = new GameHandler(new ClientConnection(inCommand.getMessage(), socket));
					gameHandlers.add(gHandler);
					runnable = false;
					break;
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

	
	
	