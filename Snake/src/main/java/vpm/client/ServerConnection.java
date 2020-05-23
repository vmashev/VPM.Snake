package vpm.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import vpm.helper.Command;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.ui.Board;

public class ServerConnection implements Runnable{

	private Socket server;
	private Board board;
	private GameInfo gameInfo;
	private ObjectInputStream objectInput;
	private boolean running;
	
	public ServerConnection(Socket server, Board board) throws IOException {
		this.server = server;
		this.board = board;
		this.objectInput = new ObjectInputStream(server.getInputStream());
	}
	
	@Override
	public void run() {
		running = true;
		
		try {
			while (running) {
				
				Command receiveCommand = (Command)objectInput.readObject();
				gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
				
				switch (receiveCommand.getNumber()) {
				case 1: // Start game
					board.setGameInfo(gameInfo);
					board.getSnakeMove().setStatus(gameInfo.getStatus());
					break;
				case 2: // Update move
					GameInfo boardGameInfo = board.getGameInfo();
					boardGameInfo.setSnakes(gameInfo.getSnakes());
					boardGameInfo.setApple(gameInfo.getApple());
					boardGameInfo.setStatus(gameInfo.getStatus());
					board.getSnakeMove().setStatus(gameInfo.getStatus());
					break;
				}
				
				board.requestRender();
							
				if(gameInfo.getStatus() == GameStatus.GameOver) {
					running = false;
				}
			
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectInput.close();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

}
