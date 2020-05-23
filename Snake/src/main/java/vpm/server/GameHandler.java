package vpm.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.comand.strategy.CommandExecuteFactory;
import vpm.comand.strategy.CommandExecuteStrategy;
import vpm.helper.ClientConnection;
import vpm.helper.Command;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;

public class GameHandler implements Runnable{

	private ArrayList<ClientConnection> clients;
	private ArrayList<GameOutHandler> gameOutHandlers;
	private ArrayBlockingQueue<Command> inCommands;

	private Command inCommand;
	private Command outCommand;
	private CommandExecuteStrategy executeStrategy;
	private GameOutHandler gameOutHandler;
	private GameInHandler gameInHandler;
	private GameInfo gameInfo;
	
	public GameHandler(ClientConnection client, GameInfo gameInfo) {
		this.clients = new ArrayList<ClientConnection>();
		this.clients.add(client);
		
		this.gameOutHandlers = new ArrayList<GameOutHandler>();
		this.inCommands = new ArrayBlockingQueue<Command>(1000);
		
		this.gameInfo = gameInfo;						
	}
	
	@Override
	public void run() {

		try {
			
			String message = JsonParser.parseFromGameInfo(gameInfo);
			outCommand = new Command(1, message);
			
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
					executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
					outCommand = inCommand.execute(executeStrategy);
					
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
	
	public void joinGame(ClientConnection client) {
		clients.add(client);
	}

	public GameInfo getGameInfo() {
		return gameInfo;
	}
	
}

	
	
	