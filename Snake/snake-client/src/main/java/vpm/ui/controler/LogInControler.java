package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.EncryptionUtils;
import vpm.model.UserEntity;
import vpm.ui.LogIn;

public class LogInControler implements ActionListener{

	private LogIn logInPage;
	
	public LogInControler(LogIn logIn) {
		this.logInPage = logIn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Login":
			logIn();
			break;
		case "Close":
			logInPage.dispose();
			break;		
		}
	}
	
	private void logIn() {
		
		String nickname = logInPage.userFld.getText();
		String password = new String(logInPage.passwordField.getPassword());
		password = EncryptionUtils.encryptMD5(password);
		
		if((logInPage.userFld.getText().equals("")) || (logInPage.passwordField.getPassword().length == 0)) {
			logInPage.showMessage("The username or password is empty.");
			return;
		}
		
		ClientSetup clientSetup = ClientSetup.createInstance();
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;

		try {

			Socket socket = new Socket(ClientSetup.SERVER_IP, ClientSetup.SERVER_PORT);
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());

			UserEntity user = new UserEntity(nickname);
			String jsonMessage = user.parseToJson();
			
			CommunicationCommand sendCommand = new CommunicationCommand(clientSetup.getUser(), 1, jsonMessage);
			objectOutput.writeObject(sendCommand);
		
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = UserEntity.parseJsonToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				logInPage.showMessage("The username does not exist.");
				return;			
			}
			
			if(!user.getEncryptedPassword().equals(password)) {
				logInPage.showMessage("The password is incorect.");
				return;			
			}
			
			clientSetup.setUser(user);
	
			logInPage.dispose();
			
			
		} catch (IOException | ClassNotFoundException e) {
			logInPage.showMessage(e.getMessage());
		} finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				logInPage.showMessage(e.getMessage());
			}
		}
	}
}
