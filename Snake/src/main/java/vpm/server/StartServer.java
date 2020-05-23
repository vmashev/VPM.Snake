package vpm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vpm.helper.ClientConnection;
import vpm.helper.Constants;
import vpm.helper.ServerSetup;

public class StartServer {

	private ArrayList<GameHandler> gameHandlers = new ArrayList<GameHandler>();
	
	public static void main(String[] args) throws Exception {
		
		new StartServer();
	}
	
	public StartServer() throws IOException, ClassNotFoundException {
		
		ServerSetup serverSetup = ServerSetup.createInstance();
		ServerSocket serverSocket = new ServerSocket(Constants.PORT);
		
		while (true) {
			System.out.println("Server is running on port " + Constants.PORT);
			Socket socket = serverSocket.accept(); 
			
			GameManager clientThread = new GameManager(socket,gameHandlers);
			new Thread(clientThread).start();

		}
		
	}
	
}