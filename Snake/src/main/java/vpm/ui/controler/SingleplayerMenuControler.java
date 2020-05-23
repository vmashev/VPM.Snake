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
import vpm.ui.NewGame;
import vpm.ui.SingleplayerMenu;

public class SingleplayerMenuControler implements ActionListener{

	private SingleplayerMenu singleplayerMenu;
	private ClientSetup clientSetup;
	
	public SingleplayerMenuControler(SingleplayerMenu singleplayerMenu) {
		this.singleplayerMenu = singleplayerMenu;
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
		
			clientSetup = ClientSetup.createInstance();
	        try {
	        	Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
	    		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
	    		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
	    		
	    		GameInfo gameInfo = new GameInfo(clientSetup.getUserName(), width , height , speed);
	    		
				String message = JsonParser.parseFromGameInfo(gameInfo);
				CommunicationCommand sendCommand = new CommunicationCommand(10, message);
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
				singleplayerMenu.showMessage(e.getMessage());
			} 
		}
	}
	
	private void resumeGame() {
		if (singleplayerMenu.table.getSelectedRowCount() > 0) {
            int selectedRow = singleplayerMenu.table.getSelectedRow();
            
            LocalDateTime dateTime = LocalDateTime.parse(singleplayerMenu.table.getValueAt(selectedRow, 2).toString());
            
            try {
            	Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
            	ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            	ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        		
    			GameInfo gameInfo = new GameInfo();
    			gameInfo.setDateTime(dateTime);
    			
    			String message = JsonParser.parseFromGameInfo(gameInfo);
    			CommunicationCommand sendCommand = new CommunicationCommand(5, message);
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
    			singleplayerMenu.showMessage(e.getMessage());
    		} 
        
        }
	}
	
	public void getSavedGames() {
		
		ClientSetup clientSetup = ClientSetup.createInstance();
		
		try {
			Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
    		
			UserEntity user = new UserEntity(clientSetup.getUserName());
			String message = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(4, message);
			outputStream.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)inputStream.readObject();
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
		}  	
	}

}
