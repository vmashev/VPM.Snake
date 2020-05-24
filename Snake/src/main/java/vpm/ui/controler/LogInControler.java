package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import vpm.helper.ClientSetup;
import vpm.helper.CommunicationCommand;
import vpm.helper.Constants;
import vpm.helper.EncryptionUtils;
import vpm.helper.JsonParser;
import vpm.model.UserEntity;
import vpm.ui.LogIn;

public class LogInControler implements ActionListener{

	private Socket socket;
	private ObjectOutputStream objectOutput ;
	private ObjectInputStream objectinput ;
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
		
		try {
			socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectinput = new ObjectInputStream(socket.getInputStream());
			
			UserEntity user = new UserEntity(nickname);
			String message = JsonParser.parseFromUserEntity(user);
			
			CommunicationCommand sendCommand = new CommunicationCommand(1, message);
			objectOutput.writeObject(sendCommand);
			
			CommunicationCommand receiveCommand = (CommunicationCommand)objectinput.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				logInPage.showMessage("The username does not exist.");
				return;			
			}
			
			if(!user.getEncryptedPassword().equals(password)) {
				logInPage.showMessage("The password is incorect.");
				return;			
			}
			
			ClientSetup clientSetup = ClientSetup.createInstance();
			clientSetup.setUsername(nickname);
	
			logInPage.dispose();
			
			
		} catch (IOException | ClassNotFoundException e) {
			logInPage.showMessage(e.getMessage());
		} finally {
			try {
				objectOutput.close();
				objectinput.close();
			} catch (IOException e) {
				logInPage.showMessage(e.getMessage());
			}
		}
	}
}
