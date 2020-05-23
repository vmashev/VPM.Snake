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

	private ExecutorService pool = Executors.newFixedThreadPool(1000);
	private ArrayList<GameHandler> gameHandlers = new ArrayList<GameHandler>();
	private ArrayList<ClientConnection> clientConnections = new ArrayList<ClientConnection>();
	
	public static void main(String[] args) throws Exception {
		
		new Server();
	}
	
	public StartServer() throws IOException, ClassNotFoundException {
		
		ServerSetup serverSetup = ServerSetup.createInstance();
		ServerSocket serverSocket = new ServerSocket(Constants.PORT);
		
		while (true) {
			System.out.println("Server is running on port " + Constants.PORT);
			Socket socket = serverSocket.accept(); 
			
			StartServerHandler clientThread = new StartServerHandler(socket,gameHandlers);
			
			pool.execute(clientThread);
		}
		
	}
	
}