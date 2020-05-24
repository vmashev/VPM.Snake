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
import vpm.ui.SignUp;

public class SignUpControler implements ActionListener{

	private SignUp signUpPage;
	private ClientSetup clientSetup;

	public SignUpControler(SignUp signUp) {
		this.signUpPage = signUp;
		this.clientSetup = ClientSetup.createInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "SignUp":
			signUp();
			break;
		case "Close":
			signUpPage.dispose();
			break;		
		}
	}

	private void signUp() {
		UserEntity user;
		
		String nickname = signUpPage.userFld.getText();
		String password = new String(signUpPage.passwordField.getPassword());
		String rePassword = new String(signUpPage.passwordField2.getPassword());
		String firstName = signUpPage.firsNameFld.getText();
		String lastName = signUpPage.lastNameFld.getText();
		String email = signUpPage.emailFld.getText();
		
		if(nickname.equals("")) {
			signUpPage.showMessage("Username is empty.");
			return;
		}
		
		if(password.equals("")) {
			signUpPage.showMessage("Password is empty.");
			return;
		}
		
		if( !password.equals(rePassword)) {
			signUpPage.showMessage("Wrong re-entered password.");
			signUpPage.passwordField2.setText("");
			return;
		}
		
		password = EncryptionUtils.encryptMD5(password);
		
		user = new UserEntity(nickname);
		user.setEncryptedPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
			
		ObjectOutputStream objectOutput = null;
		ObjectInputStream objectinput = null;

		try {
			
			Socket socket = new Socket(clientSetup.getServerIp(), clientSetup.getServerPort());
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
    		objectinput = new ObjectInputStream(socket.getInputStream());
			
			String jsonMessage = user.parseToJson();
			
			CommunicationCommand sendCommand = new CommunicationCommand(2, jsonMessage);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = UserEntity.parseJsonToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				signUpPage.showMessage(receiveCommand.getErrorMessage());
				return;
			}
			
			jsonMessage = user.parseToJson();;
			sendCommand = new CommunicationCommand(1, jsonMessage);
			objectOutput.writeObject(sendCommand);
			
			receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = UserEntity.parseJsonToUserEntity(receiveCommand.getMessage());
			
			ClientSetup clientSetup = ClientSetup.createInstance();
			clientSetup.setUser(user);

			signUpPage.dispose();		
			
		} catch (IOException | ClassNotFoundException e) {
			signUpPage.showMessage(e.getMessage());
		} finally {
			try {
				if(objectOutput != null) {
					objectOutput.close();
				}
				if(objectinput != null) {
					objectinput.close();
				}
			} catch (IOException e) {
				signUpPage.showMessage(e.getMessage());
			}
		}
	}
}
