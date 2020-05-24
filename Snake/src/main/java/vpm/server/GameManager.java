package vpm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Documented;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.TransientObjectException;

import vpm.comand.CommandFactory;
import vpm.comand.Command;
import vpm.helper.ClientConnection;
import vpm.helper.CommunicationCommand;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;

public class GameManager implements Runnable{

	private Socket socket ;
	private GameInfo gameInfo;
	private ArrayList<GameHandler> gameHandlers;

	private GameHandler gHandler;
	private CommunicationCommand inCommand;
	private CommunicationCommand outCommand;
	private Command executeStrategy;
	private ObjectOutputStream objectOutput ;
	private ObjectInputStream objectInput ;
	private boolean runnable;
	
	public GameManager(Socket socket, ArrayList<GameHandler> gameHandlers) {
		this.socket = socket;
		this.gameHandlers = gameHandlers;	
		this.gameInfo = new GameInfo();
	}
	
	@Override
	public void run() {
		runnable = true;
		
		try {
			
			this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
			this.objectInput = new ObjectInputStream(socket.getInputStream());

			while(runnable) {
				
				inCommand = (CommunicationCommand)objectInput.readObject();
				
				executeStrategy = CommandFactory.createCommand(inCommand.getNumber() , null, this);
				outCommand = executeStrategy.execute(inCommand);
				
				objectOutput.writeObject(outCommand);
									
			}
			
		} catch (Exception e) {
			outCommand = new CommunicationCommand(0, null);
			try {
				objectOutput.writeObject(outCommand);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
	}

	public ArrayList<GameHandler> getGameHandlers() {
		return gameHandlers;
	}

	public void setGameHandlers(ArrayList<GameHandler> gameHandlers) {
		this.gameHandlers = gameHandlers;
	}

	public boolean isRunnable() {
		return runnable;
	}

	public void setRunnable(boolean runnable) {
		this.runnable = runnable;
	}

	public ObjectOutputStream getObjectOutput() {
		return objectOutput;
	}

	public void setObjectOutput(ObjectOutputStream objectOutput) {
		this.objectOutput = objectOutput;
	}

	public ObjectInputStream getObjectInput() {
		return objectInput;
	}

	public void setObjectInput(ObjectInputStream objectInput) {
		this.objectInput = objectInput;
	}	
	
}

	
	
	