package vpm.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

import vpm.helper.Command;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.ui.Board;

public class ServerConnection implements Runnable{

	private Socket server;
	private Board board;
	private BufferedReader in ;
	private ObjectInputStream objectInput;
	
	public ServerConnection(Socket server, Board board) throws IOException {
		this.server = server;
		this.board = board;
		this.in = new BufferedReader(new InputStreamReader(server.getInputStream()));
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
				//Update GameInfo
				
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

}
