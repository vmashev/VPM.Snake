package vpm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vpm.helper.Constants;

public class Server {

	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private ExecutorService pool = Executors.newFixedThreadPool(4);
	
	public static void main(String[] args) throws Exception {
		
		new Server();
		
	}
	
	
	public Server() throws IOException, ClassNotFoundException {
		
		ServerSocket serverSocket = new ServerSocket(Constants.PORT);
		
		while (true) {
			System.out.println("Server is running on port " + Constants.PORT);
			Socket socket = serverSocket.accept(); //Keeps the program running!
			
			ClientHandler clientThread = new ClientHandler(socket, clients);
			clients.add(clientThread);
			
			pool.execute(clientThread);
		}
		

		
		
	}
	
}
