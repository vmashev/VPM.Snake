package vpm.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.command.Command;
import vpm.command.CommandFactory;
import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.model.GameInfo;

//Thread which handle the communication with the client during the game
//It is started on the server
//Create one thread for send-GameOutHandler and receive-GameInHandler messages for every client
//Process incoming commands from GameInHandlers who add received commands in ArrayBlockingQueue - inCommands
//Add response commands in every GameOutHandler
public class GameHandler implements Runnable{

	private ArrayList<ClientConnection> clients;
	private ArrayList<GameOutHandler> gameOutHandlers;
	private ArrayBlockingQueue<CommunicationCommand> inCommands;

	private CommunicationCommand inCommand;
	private CommunicationCommand outCommand;
	private Command executeStrategy;
	private GameOutHandler gameOutHandler;
	private GameInHandler gameInHandler;
	private GameInfo gameInfo;
	
	public GameHandler(ClientConnection client, GameInfo gameInfo) {
		this.clients = new ArrayList<ClientConnection>();
		this.clients.add(client);
		
		this.gameOutHandlers = new ArrayList<GameOutHandler>();
		this.inCommands = new ArrayBlockingQueue<CommunicationCommand>(1000);
		
		this.gameInfo = gameInfo;						
	}
	
	@Override
	public void run() {

		try {
			String message = null;
			if(gameInfo != null) {
				message = gameInfo.parseToJson();
			}
			outCommand = new CommunicationCommand(null,1, message);
			
			for (ClientConnection clientConnection : clients) {
				gameOutHandler = new GameOutHandler(clientConnection);
				gameOutHandlers.add(gameOutHandler);
				new Thread(gameOutHandler).start();
				
				gameInHandler = new GameInHandler(clientConnection, inCommands);
				new Thread(gameInHandler).start();
				
				gameOutHandler.addCommand(outCommand);
			}
			
			while(true) {
				inCommand = inCommands.poll();
				if(inCommand != null) {
					executeStrategy = CommandFactory.createCommand(inCommand.getNumber() , gameInfo, null);
					outCommand = executeStrategy.execute(inCommand);
					
					for (GameOutHandler gameOutHandler : gameOutHandlers) {
						gameOutHandler.addCommand(outCommand);
					}
				}
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}	
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	public ArrayList<ClientConnection> getClients() {
		return clients;
	}

	public void setClients(ArrayList<ClientConnection> clients) {
		this.clients = clients;
	}

	public void setGameInfo(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}
	
	
}

	
	
	