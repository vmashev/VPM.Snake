package vpm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import vpm.helper.ServerSetup;

public class StartServer {

	private ArrayList<GameHandler> gameHandlers = new ArrayList<GameHandler>();
	
	public static void main(String[] args) throws Exception {
		
		new StartServer();
	}
	
	public StartServer() throws IOException, ClassNotFoundException {
		
		ServerSetup serverSetup = ServerSetup.createInstance();
		ServerSocket serverSocket = new ServerSocket(serverSetup.getServerPort());
		
		while (true) {
			Socket socket = serverSocket.accept(); 
			
			ServerConectionManager clientThread = new ServerConectionManager(socket,gameHandlers);
			new Thread(clientThread).start();

		}
		
	}
	
}