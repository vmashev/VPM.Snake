package vpm.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import vpm.model.GameInfo;

public class ServerConnection implements Runnable{

	private Socket server;
	private GameInfo gameInfo;
	private BufferedReader in ;
	
	public ServerConnection(Socket server, GameInfo gameInfo) throws IOException {
		this.server = server;
		this.gameInfo = gameInfo;
		this.in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}
	
	@Override
	public void run() {
		
		try {
			while (true) {
				String serverResponse =	serverResponse = in.readLine();
				
				//Update GameInfo
				
			}
		} catch (IOException e) {
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
