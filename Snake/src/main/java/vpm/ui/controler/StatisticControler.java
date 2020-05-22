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
import vpm.ui.Statistic;

public class StatisticControler implements ActionListener{

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
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
		
		try {
			socket = new Socket(Constants.SERVER_IP , Constants.PORT);
    		outputStream = new ObjectOutputStream(socket.getOutputStream());
    		inputStream = new ObjectInputStream(socket.getInputStream());
    		
			UserEntity user = new UserEntity(username);
			String message = JsonParser.parseFromUserEntity(user);
			
			Command sendCommand = new Command(6, message);
			outputStream.writeObject(sendCommand);
			
			Command receiveCommand = (Command)inputStream.readObject();
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
			
			
			sendCommand = new Command(1, message);
			outputStream.writeObject(sendCommand);
			
			receiveCommand = (Command)inputStream.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			statistic.usernameFld.setText(user.getUsername());
			statistic.maxScoreFld.setText(String.valueOf(user.getMaxScore()));
			
		} catch (IOException | ClassNotFoundException e) {
			statistic.showMessage(e.getMessage());
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				statistic.showMessage(e.getMessage());
			}
		}	 	
	}
}
