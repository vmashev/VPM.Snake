package vpm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable{

	Socket socket ;
	ArrayList<ClientHandler> clients;
	BufferedReader in ;
	PrintWriter out;
	
	public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) throws IOException {
		this.socket = socket;
		this.clients = clients;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				String request = in.readLine();
				
				if(request.contains("name")) {
					out.println("Valentin");
				} else if(request.startsWith("all")) {
					outToAll(request);
				} else {
					out.println("tell me name");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void outToAll(String message) {
		for (ClientHandler clientHandler : clients) {
			clientHandler.out.println(message);
		}
	}
}

	
	
	