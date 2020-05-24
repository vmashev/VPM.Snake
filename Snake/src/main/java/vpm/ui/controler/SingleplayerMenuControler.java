package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.ConnectionSetup;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.ui.GameBoard;
import vpm.ui.NewGame;
import vpm.ui.SingleplayerMenu;

public class SingleplayerMenuControler implements ActionListener{

	private SingleplayerMenu singleplayerMenu;
	private ClientSetup clientSetup;
	
	public SingleplayerMenuControler(SingleplayerMenu singleplayerMenu) {
		this.singleplayerMenu = singleplayerMenu;
		this.clientSetup = ClientSetup.createInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "New Game":
			newGame();
			break;
		case "Resume":
			resumeGame();
			break;
		case "Close":
			singleplayerMenu.dispose();
			break;		
		}
		
	}
	
	private void newGame() {
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

	    		GameInfo gameInfo = new GameInfo(clientSetup.getUsername(), width , height , speed);
	    		
				String message = JsonParser.parseFromGameInfo(gameInfo);
				CommunicationCommand sendCommand = new CommunicationCommand(10, message);
				objectOutput.writeObject(sendCommand);
				
				CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
				gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
				
				GameBoard gameBoard = new GameBoard(gameInfo, objectOutput, objectinput);
				gameBoard.setVisible(true);
				
			} catch (IOException | ClassNotFoundException e) {
				singleplayerMenu.showMessage(e.getMessage());
			} 
		}
	}
	
	private void resumeGame() {
		if (singleplayerMenu.table.getSelectedRowCount() > 0) {
            int selectedRow = singleplayerMenu.table.getSelectedRow();
            
            LocalDateTime dateTime = LocalDateTime.parse(singleplayerMenu.table.getValueAt(selectedRow, 2).toString());
            
            try {
            	
            	Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
    			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream objectinput = new ObjectInputStream(socket.getInputStream());

        		GameInfo gameInfo = new GameInfo();
    			gameInfo.setDateTime(dateTime);
    			
    			String message = JsonParser.parseFromGameInfo(gameInfo);
    			CommunicationCommand sendCommand = new CommunicationCommand(5, message);
    			objectOutput.writeObject(sendCommand);
    			
    			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
    			gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
    			
				GameBoard gameBoard = new GameBoard(gameInfo, objectOutput, objectinput);
				gameBoard.setVisible(true);
    			
    		} catch (IOException | ClassNotFoundException e) {
    			singleplayerMenu.showMessage(e.getMessage());
    		} 
        
        }
	}
	
	public void getSavedGames() {
		
		ClientSetup clientSetup = ClientSetup.createInstance();
		
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;

		try {
    		
			Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());
			
			UserEntity user = new UserEntity(clientSetup.getUsername());
			String message = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(4, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			List<GameInfo> games = JsonParser.parseToGameInfoList(receiveCommand.getMessage());

			for (int i = 0; i < games.size(); i++) {
				singleplayerMenu.model.addRow(new Object[]{ i+1 , 
											games.get(i).getHostUsername(), 
											games.get(i).getDateTime(),
											games.get(i).getHeight(), 
											games.get(i).getWidth(), 
											games.get(i).getSpeed()});
			}		
		} catch (IOException | ClassNotFoundException e) {
			singleplayerMenu.showMessage(e.getMessage());
		}  finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				singleplayerMenu.showMessage(e.getMessage());
			}
		}
	}


}
