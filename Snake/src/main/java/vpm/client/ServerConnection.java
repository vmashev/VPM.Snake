package vpm.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable{

	private Socket server;
	BufferedReader in ;
	
	public ServerConnection(Socket server) throws IOException {
		this.server = server;
		this.in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}
	
	@Override
	public void run() {
		
		try {
			while (true) {
				String serverResponse =	serverResponse = in.readLine();
				System.out.println("Server says: " + serverResponse);
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
