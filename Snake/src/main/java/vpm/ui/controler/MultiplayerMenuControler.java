package vpm.ui.controler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.Constants;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.ui.Board;
import vpm.ui.MultiplayerMenu;
import vpm.ui.NewGame;

public class MultiplayerMenuControler implements ActionListener{

	private MultiplayerMenu multiplayerMenu;
	private ClientSetup clientSetup;

	
	public MultiplayerMenuControler(MultiplayerMenu multiplayerMenu) {
		this.multiplayerMenu = multiplayerMenu;
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
		
			clientSetup = ClientSetup.createInstance();
	        try {
	        	Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
	        	ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
	    		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
	    		
	    		GameInfo gameInfo = new GameInfo(clientSetup.getUsername(), width , height , speed);
	    		
				String message = JsonParser.parseFromGameInfo(gameInfo);
				CommunicationCommand sendCommand = new CommunicationCommand(13, message);

				outputStream.writeObject(sendCommand);
				
				CommunicationCommand receiveCommand = (CommunicationCommand)inputStream.readObject();
				gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
				
				Board board = new Board(gameInfo, outputStream, inputStream);
	    		
	    		JFrame frame = new JFrame("Play");
	    		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    		frame.setContentPane(board);
	    		frame.setResizable(false);
	    		frame.pack();
	    		frame.setPreferredSize(new Dimension(gameInfo.getWidth(), gameInfo.getHeight()));
	    		frame.setLocationRelativeTo(null);
	    		frame.setVisible(true);	
				
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
            	Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
            	ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            	ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        		
    			String message = username;
    			CommunicationCommand sendCommand = new CommunicationCommand(14, message);
    			outputStream.writeObject(sendCommand);
    			
    			CommunicationCommand receiveCommand = (CommunicationCommand)inputStream.readObject();
    			GameInfo gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
    			
    			Board board = new Board(gameInfo, outputStream, inputStream);
        		
        		JFrame frame = new JFrame("Play");
        		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        		frame.setContentPane(board);
        		frame.setResizable(false);
        		frame.pack();
        		frame.setPreferredSize(new Dimension(gameInfo.getWidth(), gameInfo.getHeight()));
        		frame.setLocationRelativeTo(null);
        		frame.setVisible(true);	
    			
    		} catch (IOException | ClassNotFoundException e) {
    			multiplayerMenu.showMessage(e.getMessage());
    		} 
        }
	}
	
	public void getLobbies() {
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		try {
			Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
    		
    		CommunicationCommand sendCommand = new CommunicationCommand(15, null);
			outputStream.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)inputStream.readObject();
			List<GameInfo> games = JsonParser.parseToGameInfoList(receiveCommand.getMessage());

			for (int i = 0; i < games.size(); i++) {
				multiplayerMenu.model.addRow(new Object[]{ i+1 , 
											games.get(i).getHostUsername(), 
											games.get(i).getHeight(), 
											games.get(i).getWidth(), 
											games.get(i).getSpeed()});
			}		
		} catch (IOException | ClassNotFoundException e) {
			multiplayerMenu.showMessage(e.getMessage());
		} finally {
			try {
				if(outputStream != null) {
					outputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				multiplayerMenu.showMessage(e.getMessage());
			}
		}	 	
	}

}
