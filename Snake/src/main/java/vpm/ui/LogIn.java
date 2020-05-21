package vpm.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.EncryptionUtils;
import vpm.helper.JsonParser;
import vpm.model.UserEntity;

public class LogIn extends JDialog implements ActionListener {

	private JPanel contentPane;
	private JTextField userFld;
	private JPasswordField passwordField;

	public LogIn() {
		setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 160);
		setTitle("Login");
		setModal(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 14, 60, 20);
		contentPane.add(lblNewLabel);
		
		userFld = new JTextField();
		userFld.setBounds(80, 11, 140, 25);
		contentPane.add(userFld);
		userFld.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPassword.setBounds(10, 47, 60, 20);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(80, 45, 140, 25);
		contentPane.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnLogin.setBounds(36, 91, 89, 23);
		btnLogin.addActionListener(this);
		contentPane.add(btnLogin);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(135, 91, 89, 23);
		btnClose.addActionListener(this);
		contentPane.add(btnClose);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Login":
			logIn();
			break;
		case "Close":
			dispose();
			break;		
		}
	}
	
	private void logIn() {
		String nickname = userFld.getText();
		String password = new String(passwordField.getPassword());
		password = EncryptionUtils.encryptMD5(password);
		
		if((userFld.getText().equals("")) || (passwordField.getPassword().length == 0)) {
			JOptionPane.showMessageDialog (	this , "The username or password is empty." , "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			
			ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectinput = new ObjectInputStream(socket.getInputStream());
			
			UserEntity user = new UserEntity(nickname);
			String message = JsonParser.parseFromUserEntity(user);
			
			Command sendCommand = new Command(1, message);
			objectOutput.writeObject(sendCommand);
			
			Command receiveCommand = (Command)objectinput.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				JOptionPane.showMessageDialog (	this , "The username does not exist." , "Error", JOptionPane.ERROR_MESSAGE);
				return;			
			}
			
			if(!user.getEncryptedPassword().equals(password)) {
				JOptionPane.showMessageDialog (	this , "The password is incorect." , "Error", JOptionPane.ERROR_MESSAGE);
				return;			
			}
			
			ClientSetup clientSetup = ClientSetup.createInstance();
			clientSetup.setUserName(nickname);
	
			dispose();
			
			
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		}		
	}
}
