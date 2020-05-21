package vpm.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import vpm.helper.ClientSetup;

public class MainFrame extends JFrame implements ActionListener{
	
	JButton logInButton;
	JButton signInButton;
	JButton singlePlayerButton;
	JButton multiPlayerButton;
	JButton quitButton;
	JButton logOutButton;
	JButton userInfoButton;
	ClientSetup clientSetup;
	boolean userIsLogged;
	
	public MainFrame() {
		
		super("Snake");
		setLayout(new GridLayout(7,1));
		
		logInButton = new JButton("LogIn");
		logInButton.addActionListener(this);
		
		logOutButton = new JButton("LogOut");
		logOutButton.setEnabled(false);
		logOutButton.addActionListener(this);
		
		signInButton = new JButton("SignUp");
		signInButton.addActionListener(this);
		
		singlePlayerButton = new JButton("Singleplayer");
		singlePlayerButton.addActionListener(this);
		
		multiPlayerButton = new JButton("Multiplayer");
		multiPlayerButton.addActionListener(this);
		
		userInfoButton = new JButton("User Information");
		userInfoButton.setEnabled(false);
		userInfoButton.addActionListener(this);
		
		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		
		add(logInButton);
		add(logOutButton);
		add(signInButton);
		add(singlePlayerButton);
		add(multiPlayerButton);
		add(userInfoButton);
		add(quitButton);
		
		setSize(300	, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "LogIn": 
			LogIn logIn = new LogIn();
			logIn.setVisible(true);
			
			clientSetup = clientSetup.createInstance();
			userIsLogged = clientSetup.getUserName() != null;
			
			logInButton.setEnabled(!userIsLogged);
			signInButton.setEnabled(!userIsLogged);
			logOutButton.setEnabled(userIsLogged);
			userInfoButton.setEnabled(userIsLogged);
			
			break;
		case "LogOut": 
			clientSetup = clientSetup.createInstance();
			clientSetup.setUserName(null);
			
			logInButton.setEnabled(true);
			signInButton.setEnabled(true);
			logOutButton.setEnabled(false);
			userInfoButton.setEnabled(false);
			
			break;			
		case "SignUp": 
			SignUp signUp = new SignUp();
			signUp.setVisible(true);
			
			clientSetup = clientSetup.createInstance();
			userIsLogged = clientSetup.getUserName() != null;
			
			logInButton.setEnabled(!userIsLogged);
			signInButton.setEnabled(!userIsLogged);
			logOutButton.setEnabled(userIsLogged);
			userInfoButton.setEnabled(userIsLogged);
			
			break;			
		case "Singleplayer": 
			Singleplayer singleplayer = new Singleplayer();
			singleplayer.setVisible(true);
			break;
		case "Multiplayer":
			break;
		case "User Information": 
			UserInformation userInformation = new UserInformation();
			userInformation.setVisible(true);
			break;	
		case "Quit": 
			dispose();
			break;			
		}
		
	}
}
