package vpm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import vpm.helper.ConnectionSetup;
import vpm.helper.ServerSetup;

public class StartServer {

	private ArrayList<GameHandler> gameHandlers = new ArrayList<GameHandler>();
	
	public static void main(String[] args) throws Exception {
		
		new StartServer();
	}
	
	public StartServer() throws IOException, ClassNotFoundException {
		
		ServerSetup serverSetup = ServerSetup.createInstance();
		ServerSocket serverSocket = new ServerSocket(ConnectionSetup.PORT);
		
		while (true) {
			System.out.println("Server is running on port " + ConnectionSetup.PORT);
			Socket socket = serverSocket.accept(); 
			
			GameManager clientThread = new GameManager(socket,gameHandlers);
			new Thread(clientThread).start();

		}
		
	}
	
}