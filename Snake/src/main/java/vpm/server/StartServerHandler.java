package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Documented;
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
	private Command inCommand;
	private Command outCommand;
	private CommandExecuteStrategy executeStrategy;
	private ObjectOutputStream objectOutput ;
	private ObjectInputStream objectInput ;
	private boolean runnable;
	
	public StartServerHandler(Socket socket, ArrayList<GameHandler> gameHandlers) {
		this.socket = socket;
		this.gameHandlers = gameHandlers;	
		this.gameInfo = new GameInfo();
	}
	
	@Override
	public void run() {
		runnable = true;
		
		try {
			
			this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
			this.objectInput = new ObjectInputStream(socket.getInputStream());

			while(runnable) {
				
				inCommand = (Command)objectInput.readObject();
				System.out.println("Received Command: " + inCommand.getNumber() + ", Message: " + inCommand.getMessage());
				
				switch (inCommand.getNumber()) {
				//New Singleplayer Game - start new game and close this thread
				case 10:
					executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
					inCommand.execute(executeStrategy);
					
					gHandler = new GameHandler(new ClientConnection("vm", objectOutput, objectInput),executeStrategy.getGameInfo());
					Thread gameThread = new Thread(gHandler); 
					gameThread.start();
					runnable = false;
					break;
				//Create Lobby
				case 13:
					executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
					inCommand.execute(executeStrategy);
					
					gHandler = new GameHandler(new ClientConnection("vm", objectOutput, objectInput),executeStrategy.getGameInfo());
					gameHandlers.add(gHandler);
					runnable = false;
					break;
				//Join Lobby - start new game and close this thread
				case 14:
					inCommand.execute(executeStrategy); // return index 
					int index= 0;
					gHandler = gameHandlers.get(index);
					gHandler.joinGame(new ClientConnection("vm", objectOutput, objectInput));
					new Thread(gHandler).run(); //..
					runnable = false;
					break;	
				//Get Lobby - start new game and close this thread
				case 15:
					//inCommand.execute(executeStrategy); // return gameHandlers
					//int index= 0;
					gameHandlers.get(0);
					new Thread(gHandler).run(); 
					break;						
				default:
					
					executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
					outCommand = inCommand.execute(executeStrategy);
					
					objectOutput.writeObject(outCommand);
					
					break;
				}	
				
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}	
	
	
	
}

	
	
	