package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.ui.MultiplayerMenu;
import vpm.ui.NewGame;

public class MultiplayerMenuControler implements ActionListener{

	private MultiplayerMenu multiplayerMenu;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	public MultiplayerMenuControler(MultiplayerMenu multiplayerMenu) {
		this.multiplayerMenu = multiplayerMenu;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "New Lobby":
			NewGame newGame = new NewGame(true);
			newGame.setVisible(true);
			break;
		case "Join":
			//resumeGame();
			break;
		case "Close":
			multiplayerMenu.dispose();
			break;		
		}
	}

	public void getLobbies() {
		
		try {
			socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
    		inputStream = new ObjectInputStream(socket.getInputStream());
    		
    		Command sendCommand = new Command(15, null);
			outputStream.writeObject(sendCommand);
			
			Command receiveCommand = (Command)inputStream.readObject();
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
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				multiplayerMenu.showMessage(e.getMessage());
			}
		}	 	
	}

}
