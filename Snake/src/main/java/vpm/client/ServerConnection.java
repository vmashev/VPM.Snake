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
	private ObjectInputStream objectInput;
	
	public ServerConnection(Socket server, Board board) throws IOException {
		this.server = server;
		this.board = board;
		this.objectInput = new ObjectInputStream(server.getInputStream());
		
	}
	
	@Override
	public void run() {
		
		try {
			while (true) {
				
				Command receiveCommand = (Command)objectInput.readObject();
				GameInfo gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
				
				board.setGameInfo(gameInfo);
				board.requestRender();
				
				if(gameInfo.getStatus() == GameStatus.GameOver) {
					break;
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
