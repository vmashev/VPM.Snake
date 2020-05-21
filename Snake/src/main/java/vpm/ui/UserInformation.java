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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.JsonParser;
import vpm.model.UserEntity;
import vpm.model.service.UserService;
import vpm.model.service.impl.UserServiceImpl;

public class UserInformation extends JDialog implements ActionListener {

	private JPanel contentPane;
	private JTextField userNameFld;
	private JTextField firstNameFld;
	private JTextField lastNameFld;
	private JTextField emailFld;
	private String newPassword;
	private UserEntity user;
	private UserService userService;
	
	public UserInformation() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 590, 190);
		setTitle("User Information");
		setModal(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 11, 79, 20);
		contentPane.add(lblNewLabel);
		
		userNameFld = new JTextField();
		userNameFld.setColumns(10);
		userNameFld.setBounds(99, 11, 140, 25);
		contentPane.add(userNameFld);
		
		JLabel lblNewLabel_1 = new JLabel("First Name");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 43, 79, 20);
		contentPane.add(lblNewLabel_1);
		
		firstNameFld = new JTextField();
		firstNameFld.setColumns(10);
		firstNameFld.setBounds(99, 42, 180, 25);
		contentPane.add(firstNameFld);
		
		JLabel lblNewLabel_3 = new JLabel("Last Name");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(289, 43, 79, 20);
		contentPane.add(lblNewLabel_3);
		
		lastNameFld = new JTextField();
		lastNameFld.setColumns(10);
		lastNameFld.setBounds(378, 42, 180, 25);
		contentPane.add(lastNameFld);
		
		JLabel lblNewLabel_4 = new JLabel("E-Mail");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(10, 74, 79, 20);
		contentPane.add(lblNewLabel_4);
		
		emailFld = new JTextField();
		emailFld.setColumns(10);
		emailFld.setBounds(99, 75, 180, 25);
		contentPane.add(emailFld);
		
		JButton saveBtn = new JButton("Save");
		saveBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		saveBtn.setBounds(376, 115, 89, 23);
		saveBtn.addActionListener(this);
		contentPane.add(saveBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(475, 115, 89, 23);
		btnClose.addActionListener(this);
		contentPane.add(btnClose);
		
		JButton changePassBtn = new JButton("Change Password");
		changePassBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		changePassBtn.setBounds(246, 115, 120, 23);
		changePassBtn.addActionListener(this);
		contentPane.add(changePassBtn);
		
		getUserInfo();
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Save":
			save();
			dispose();
			break;		
		case "Change Password":
			ChangePassword changePassword = new ChangePassword(this);
			changePassword.setVisible(true);
			break;
		case "Close":
			dispose();
			break;		
		}
	}
	
	private void save() {
		String nickname = userNameFld.getText();
		String firstName = firstNameFld.getText();
		String lastName = lastNameFld.getText();
		String email = emailFld.getText();
		
		if(nickname.equals("")) {
			JOptionPane.showMessageDialog (	this , "Username is empty." , "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		user.setUsername(nickname);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		if(newPassword != null) {
			user.setEncryptedPassword(newPassword);
		}
		
		
		try {
			Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			
			String message = JsonParser.parseFromUserEntity(user);
			
			Command sendCommand = new Command(3, message);
			outputStream.writeObject(sendCommand);
			
			Command receiveCommand = (Command)inputStream.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		}		


	}
	
	private void getUserInfo() {
		ClientSetup clientSetup = ClientSetup.createInstance();
		
		try {
			Socket socket = new Socket(Constants.SERVER_IP , Constants.PORT);
			
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			
			user = new UserEntity(clientSetup.getUserName());
			String message = JsonParser.parseFromUserEntity(user);
			
			Command sendCommand = new Command(1, message);
			outputStream.writeObject(sendCommand);
			
			Command receiveCommand = (Command)inputStream.readObject();
			user = JsonParser.parseToUserEntity(receiveCommand.getMessage());
			
			if(user == null) {
				JOptionPane.showMessageDialog (	this , "The username does not exist." , "Error", JOptionPane.ERROR_MESSAGE);
				return;			
			}
			
			userNameFld.setText(user.getUsername());
			firstNameFld.setText(user.getFirstName());
			lastNameFld.setText(user.getLastName());
			emailFld.setText(user.getEmail());
			
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		}		
		

	}
	
}