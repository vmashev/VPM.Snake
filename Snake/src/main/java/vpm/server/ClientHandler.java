package vpm.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import vpm.helper.Command;
import vpm.helper.CommandUtils;
import vpm.helper.JsonParser;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;


public class ClientHandler implements Runnable{

	Socket socket ;
	ArrayList<ClientHandler> clients;
	BufferedReader in ;
	PrintWriter out;
	
	ObjectOutputStream objectOutput ;
	ObjectInputStream objectInput ;
	
	
	public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) throws IOException {
		this.socket = socket;
		this.clients = clients;
		
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
		
		this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
		this.objectInput = new ObjectInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				
				//Update GameMove and return GameInfo
				//Objects
				Command inCommand = (Command)objectInput.readObject();
				System.out.println("Received Json: " + inCommand.getMessage());
				
				Command outCommand = CommandUtils.execute(inCommand);
				objectOutput.writeObject(outCommand);
				System.out.println("Send Json: " + outCommand.getMessage());
					
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectOutput.close();
				objectInput.close();
				out.close();
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

	
	
	