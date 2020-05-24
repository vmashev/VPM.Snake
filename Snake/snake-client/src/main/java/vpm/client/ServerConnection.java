package vpm.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.model.GameInfo;
import vpm.ui.Board;

//Thread which handle incoming communication with the server during the game.
//It is started on the client
//Process incoming commands and update the board
public class ServerConnection implements Runnable{

	private Board board;
	private GameInfo gameInfo;
	private ObjectInputStream objectInput;
	private boolean running;
	
	public ServerConnection(Board board) throws IOException {
		this.board = board;
		this.objectInput = board.getObjectInput();
	}
	
	@Override
	public void run() {
		running = true;
		ClientSetup setup = ClientSetup.createInstance();
		try {
			while (running) {
				
				CommunicationCommand receiveCommand = (CommunicationCommand)objectInput.readObject();
				gameInfo = GameInfo.parseJsonToGameInfo(receiveCommand.getMessage());
				
				switch (receiveCommand.getNumber()) {
				case 1: // Start game
					board.setGameInfo(gameInfo);
					board.getSnakeMove().setStatus(gameInfo.getStatus());
					board.requestRender();
					break;
				case 2: // Update move
					GameInfo boardGameInfo = board.getGameInfo();
					boardGameInfo.setSnakes(gameInfo.getSnakes());
					boardGameInfo.setApple(gameInfo.getApple());
					boardGameInfo.setStatus(gameInfo.getStatus());
					boardGameInfo.setWinnerPlayer(gameInfo.getWinnerPlayer());
					board.getSnakeMove().setStatus(gameInfo.getStatus());
					board.requestRender();
					break;
				}
							
				if(gameInfo.getStatus() == GameStatus.GameOver) {
					running = false;
				}
			
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

}
