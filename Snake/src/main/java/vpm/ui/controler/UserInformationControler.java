package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.ConnectionSetup;
import vpm.helper.JsonParser;
import vpm.model.UserEntity;
import vpm.ui.ChangePassword;
import vpm.ui.UserInformation;

public class UserInformationControler implements ActionListener{

	private UserInformation userInformation;
	private UserEntity user;
	
	public UserInformationControler(UserInformation userInformation) {
		this.userInformation = userInformation;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Save":
			save();
			userInformation.dispose();
			break;		
		case "Change Password":
			ChangePassword changePassword = new ChangePassword(userInformation);
			changePassword.setVisible(true);
			break;
		case "Close":
			userInformation.dispose();
			break;		
		}
	}
	
	private void save() {
		String nickname = userInformation.userNameFld.getText();
		String firstName = userInformation.firstNameFld.getText();
		String lastName = userInformation.lastNameFld.getText();
		String email = userInformation.emailFld.getText();
		
		if(nickname.equals("")) {
			userInformation.showMessage("Username is empty.");
			return;
		}
		
		user.setUsername(nickname);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		if(userInformation.getNewPassword() != null) {
			user.setEncryptedPassword(userInformation.getNewPassword());
		}
		
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;
		
		try {
			
			Socket socket = new Socket(ConnectionSetup.SERVER_IP, ConnectionSetup.PORT);
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());
			
			String message = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(3, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
		} catch (IOException | ClassNotFoundException e) {
			userInformation.showMessage(e.getMessage());
		} finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				userInformation.showMessage(e.getMessage());
			}
		}	
	}
	
	public void getUserInfo() {
		ClientSetup clientSetup = ClientSetup.createInstance();
		
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;
		
		try {
			
			Socket socket = new Socket(ConnectionSetup.SERVER_IP, ConnectionSetup.PORT);
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());
			
			user = new UserEntity(clientSetup.getUsername());
			String message = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(1, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				userInformation.showMessage("The username does not exist.");
				return;			
			}
			
			userInformation.userNameFld.setText(user.getUsername());
			userInformation.firstNameFld.setText(user.getFirstName());
			userInformation.lastNameFld.setText(user.getLastName());
			userInformation.emailFld.setText(user.getEmail());
			userInformation.maxScoreFld.setText(String.valueOf(user.getMaxScore()));
			
		} catch (IOException | ClassNotFoundException e) {
			userInformation.showMessage(e.getMessage());
		} finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				userInformation.showMessage(e.getMessage());
			}
		}			
	}
}
