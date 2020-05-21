package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import vpm.helper.Command;
import vpm.helper.CommandUtils;
import vpm.model.GameInfo;


public class ClientHandler implements Runnable{

	Socket socket ;
	ArrayList<ClientHandler> clients;
	GameInfo gameInfo;
	
	ObjectOutputStream objectOutput ;
	ObjectInputStream objectInput ;
	
	public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) throws IOException {
		this.socket = socket;
		this.clients = clients;
		
		this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
		this.objectInput = new ObjectInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				
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

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}	
	
}

	
	
	