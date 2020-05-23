package vpm.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import vpm.comand.strategy.CommandExecuteFactory;
import vpm.comand.strategy.CommandExecuteStrategy;
import vpm.helper.ClientConnection;
import vpm.helper.Command;
import vpm.model.GameInfo;

public class GameHandler implements Runnable{

	private ArrayList<ClientConnection> clients;
	private ArrayList<GameOutHandler> gameOutHandlers;
	private ArrayBlockingQueue<Command> inCommands;

	private Command inCommand;
	private Command outCommand;
	private CommandExecuteStrategy executeStrategy;
	
	private GameInfo gameInfo;
	
	public GameHandler(ClientConnection client) throws IOException {
		this.clients = new ArrayList<ClientConnection>();
		this.clients.add(client);
		
		this.gameOutHandlers = new ArrayList<GameOutHandler>();
		this.inCommands = new ArrayBlockingQueue<Command>(1000);
		
		this.gameInfo = new GameInfo();	
	}
	
	@Override
	public void run() {
		try {

			for (ClientConnection clientConnection : clients) {
				GameOutHandler gameOutHandler = new GameOutHandler(clientConnection);
				gameOutHandlers.add(gameOutHandler);
				new Thread(gameOutHandler).run();
				
				GameInHandler gameInHandler = new GameInHandler(clientConnection, inCommands);
				new Thread(gameInHandler).run();
				
				inCommands.add(inCommand); //New Command Ready to Start!
			}

			while(true) {
				
				inCommand = inCommands.poll();
				if(inCommand != null) {
					
					executeStrategy = CommandExecuteFactory.createCommand(inCommand.getNumber() , gameInfo);
					outCommand = inCommand.execute(executeStrategy);
					
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
	
}

	
	
	