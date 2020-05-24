package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import vpm.helper.CommunicationCommand;
import vpm.helper.ConnectionSetup;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.ui.Statistic;

public class StatisticControler implements ActionListener{

	private Statistic statistic;
	
	public StatisticControler(Statistic statistic) {
		this.statistic = statistic;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Search":
			getStatistics(statistic.usernameFld.getText());
			break;
		case "Close":
			statistic.dispose();
			break;		
		}
	}
	
	public void getStatistics(String username) {
		
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;
		
		try {
			
			Socket socket = new Socket(ConnectionSetup.SERVER_IP, ConnectionSetup.PORT);
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());
			
			
			UserEntity user = new UserEntity(username);
			String message = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(6, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			List<GameInfo> games = JsonParser.parseToGameInfoList(receiveCommand.getMessage());

			for (int i = 0; i < statistic.model.getRowCount(); i++) {
				statistic.model.removeRow(i);	
			}
			
			for (int i = 0; i < games.size(); i++) {
				statistic.model.addRow(new Object[]{ i+1 , 
											games.get(i).getHostUsername(), 
											games.get(i).getHostSnake().getScore(),
											games.get(i).getDateTime(),
											games.get(i).getHeight(), 
											games.get(i).getWidth(), 
											games.get(i).getSpeed()});
			}	
			
			
			sendCommand = new CommunicationCommand(1, message);
			objectOutput.writeObject(sendCommand);
			
			receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			statistic.usernameFld.setText(user.getUsername());
			statistic.maxScoreFld.setText(String.valueOf(user.getMaxScore()));
			
		} catch (IOException | ClassNotFoundException e) {
			statistic.showMessage(e.getMessage());
		} finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				statistic.showMessage(e.getMessage());
			}
		}	 	
	}
}
