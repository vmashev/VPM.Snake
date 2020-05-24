package vpm.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.comand.CommandFactory;
import vpm.comand.Command;
import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.Snake;

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
			
			String message = JsonParser.parseFromGameInfo(gameInfo);
			outCommand = new CommunicationCommand(1, message);
			
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
					
					gameInfo = executeStrategy.getGameInfo();
					
					
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

	
	
	