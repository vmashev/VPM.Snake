package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.Constants;
import vpm.helper.EncryptionUtils;
import vpm.helper.JsonParser;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.ui.SignUp;

public class SignUpControler implements ActionListener{

	private SignUp signUpPage;
	private Socket socket ;
	private ObjectOutputStream outputStream ;
	private ObjectInputStream inputStream ;

	public SignUpControler(SignUp signUp) {
		this.signUpPage = signUp;
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
		UserService userService;
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
				
		try {
			socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			
			String jsonMessage = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(2, jsonMessage);
			outputStream.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)inputStream.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				signUpPage.showMessage(receiveCommand.getErrorMessage());
				return;
			}
			
			ClientSetup clientSetup = ClientSetup.createInstance();
			clientSetup.setUserName(nickname);

			signUpPage.dispose();		
			
		} catch (IOException | ClassNotFoundException e) {
			signUpPage.showMessage(e.getMessage());
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				signUpPage.showMessage(e.getMessage());
			}
		}
	}
}
