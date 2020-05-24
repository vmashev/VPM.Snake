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
import vpm.model.GameInfo;
import vpm.model.UserEntity;
import vpm.ui.Statistic;

public class StatisticControler implements ActionListener{

	private Statistic statistic;
	private ClientSetup clientSetup;
	
	public StatisticControler(Statistic statistic) {
		this.statistic = statistic;
		this.clientSetup = ClientSetup.createInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Search":
			if(!statistic.usernameFld.getText().equals("")) {
				search();
			}
			break;
		case "Close":
			statistic.dispose();
			break;		
		}
	}
	
	private void search() {
		
		ClientSetup clientSetup = ClientSetup.createInstance();
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;
		UserEntity user = null;
		
		try {

			Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());

			user = new UserEntity(statistic.usernameFld.getText());
			String message = user.parseToJson();
			
			CommunicationCommand sendCommand = new CommunicationCommand(1, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = UserEntity.parseJsonToUserEntity(receiveCommand.getMessage());

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
		if(user != null) {
			getStatistics(user);
		} else {
			statistic.showMessage("User not found.");
		}
	}
	
	public void getStatistics(UserEntity user) {
		
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;
		
		try {
			
			Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());
			
			String message = null;
			if(user != null) {
				message = user.parseToJson();
			}
			
			CommunicationCommand sendCommand = new CommunicationCommand(6, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			List<GameInfo> games = GameInfo.parseJsonToGameInfoList(receiveCommand.getMessage());

			
			while (statistic.model.getRowCount() != 0) {
				statistic.model.removeRow(0);	
			}
			
			for (int i = 0; i < games.size(); i++) {
				
				UserEntity playerOne = games.get(i).getPlayerOne();
				UserEntity playerTwo = games.get(i).getPlayerTwo();
				if(playerTwo == null) {
					playerTwo = new UserEntity("");
				}
				UserEntity winner = games.get(i).getWinnerPlayer();
				if(winner == null) {
					winner = new UserEntity("");
				}				
				statistic.model.addRow(new Object[]{ i+1 , 
											playerOne.getUsername(),
											games.get(i).getPlayerOneScore(),
											playerTwo.getUsername(),
											games.get(i).getPlayerTwoScore(),
											winner.getUsername(),
											games.get(i).getDateTime(),
											games.get(i).getHeight(), 
											games.get(i).getWidth(), 
											games.get(i).getSpeed()});
			}	
			
			
			sendCommand = new CommunicationCommand(1, message);
			objectOutput.writeObject(sendCommand);
			
			receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = UserEntity.parseJsonToUserEntity(receiveCommand.getMessage());
			
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
