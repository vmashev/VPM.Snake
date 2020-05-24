package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.ui.GameBoard;
import vpm.ui.MultiplayerMenu;
import vpm.ui.NewGame;

public class MultiplayerMenuControler implements ActionListener{

	private MultiplayerMenu multiplayerMenu;
	private ClientSetup clientSetup;

	
	public MultiplayerMenuControler(MultiplayerMenu multiplayerMenu) {
		this.multiplayerMenu = multiplayerMenu;
		this.clientSetup = ClientSetup.createInstance();
     
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "New Lobby":
			newLobby();
			break;
		case "Join":
			joinLobby();
			break;
		case "Close":
			multiplayerMenu.dispose();
			break;		
		}
	}

	private void newLobby() {
		NewGame newGame = new NewGame();
		newGame.setVisible(true);
		
		if(!newGame.widthFld.getText().equals("")) {
			int width = Integer.valueOf(newGame.widthFld.getText());
			int height = Integer.valueOf(newGame.heightFld.getText());
			int speed = Integer.valueOf(newGame.speedFld.getText());
		
			try {
	        	Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
				ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
	    		ObjectInputStream objectinput = new ObjectInputStream(socket.getInputStream());
	    		
	    		GameInfo gameInfo = new GameInfo(clientSetup.getUser(), width , height , speed);
	    		
				String message = JsonParser.parseFromGameInfo(gameInfo);
				CommunicationCommand sendCommand = new CommunicationCommand(13, message);

				objectOutput.writeObject(sendCommand);
				
				CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
				gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
				
				GameBoard gameBoard = new GameBoard(gameInfo, objectOutput, objectinput);
				gameBoard.setVisible(true);
				
			} catch (IOException | ClassNotFoundException e) {
				multiplayerMenu.showMessage(e.getMessage());
			} 
		}
	}
	
	private void joinLobby() {
		if (multiplayerMenu.table.getSelectedRowCount() > 0) {
            int selectedRow = multiplayerMenu.table.getSelectedRow();
            
            String username = (String) multiplayerMenu.table.getValueAt(selectedRow, 1);
            
            try {
            	Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
				ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
	    		ObjectInputStream objectinput = new ObjectInputStream(socket.getInputStream());
	    		
    			String message = username;
    			CommunicationCommand sendCommand = new CommunicationCommand(14, message);
    			objectOutput.writeObject(sendCommand);
    			
    			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
    			GameInfo gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
    			
				GameBoard gameBoard = new GameBoard(gameInfo, objectOutput, objectinput);
				gameBoard.setVisible(true);
        		
    		} catch (IOException | ClassNotFoundException e) {
    			multiplayerMenu.showMessage(e.getMessage());
    		} 
        }
	}
	
	public void getLobbies() {
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;
		
		try {
			
			Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
    		objectinput = new ObjectInputStream(socket.getInputStream());
    		
    		CommunicationCommand sendCommand = new CommunicationCommand(15, null);
    		objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			List<GameInfo> games = JsonParser.parseToGameInfoList(receiveCommand.getMessage());

			for (int i = 0; i < games.size(); i++) {
				multiplayerMenu.model.addRow(new Object[]{ i+1 , 
											games.get(i).getPlayerOne(), 
											games.get(i).getHeight(), 
											games.get(i).getWidth(), 
											games.get(i).getSpeed()});
			}		
		} catch (IOException | ClassNotFoundException e) {
			multiplayerMenu.showMessage(e.getMessage());
		} finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				multiplayerMenu.showMessage(e.getMessage());
			}
		}	 	
	}

}
