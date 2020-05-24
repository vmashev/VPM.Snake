package vpm.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vpm.helper.ClientSetup;

public class MainFrame extends JFrame implements ActionListener{
	
	JButton logInButton;
	JButton signInButton;
	JButton singlePlayerButton;
	JButton multiPlayerButton;
	JButton quitButton;
	JButton logOutButton;
	JButton userInfoButton;
	JButton statisticButton;
	ClientSetup clientSetup;
	boolean userIsLogged;
	
	public MainFrame() {
		
		super("Snake");
		setLayout(new GridLayout(8,1));
		setLocationRelativeTo(null);
		
		logInButton = new JButton("LogIn");
		logInButton.addActionListener(this);
		
		signInButton = new JButton("SignUp");
		signInButton.addActionListener(this);
		
		singlePlayerButton = new JButton("Singleplayer");
		singlePlayerButton.setEnabled(false);
		singlePlayerButton.addActionListener(this);
		
		multiPlayerButton = new JButton("Multiplayer");
		multiPlayerButton.setEnabled(false);
		multiPlayerButton.addActionListener(this);
		
		userInfoButton = new JButton("User Information");
		userInfoButton.setEnabled(false);
		userInfoButton.addActionListener(this);
		
		statisticButton = new JButton("Statistic");
		statisticButton.setEnabled(false);
		statisticButton.addActionListener(this);
		
		logOutButton = new JButton("LogOut");
		logOutButton.setEnabled(false);
		logOutButton.addActionListener(this);
		
		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		
		add(logInButton);
		add(signInButton);
		add(singlePlayerButton);
		add(multiPlayerButton);
		add(userInfoButton);
		add(statisticButton);
		add(logOutButton);
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
			userIsLogged = clientSetup.getUser() != null;

			updateButtonEnabled();
			
			break;
		case "LogOut": 
			clientSetup = clientSetup.createInstance();
			clientSetup.setUser(null);
			userIsLogged = false;

			updateButtonEnabled();
			
			break;			
		case "SignUp": 
			SignUp signUp = new SignUp();
			signUp.setVisible(true);
			
			clientSetup = clientSetup.createInstance();
			userIsLogged = clientSetup.getUser() != null;
			
			updateButtonEnabled();
			
			break;			
		case "Singleplayer": 
			SingleplayerMenu singleplayerMenu = new SingleplayerMenu();
			singleplayerMenu.setVisible(true);
			break;
		case "Multiplayer":
			MultiplayerMenu multiplayerMenu = new MultiplayerMenu();
			multiplayerMenu.setVisible(true);
			break;
		case "User Information": 
			UserInformation userInformation = new UserInformation();
			userInformation.setVisible(true);
			break;	
		case "Statistic": 
			Statistic statistic = new Statistic();
			statistic.setVisible(true);
			break;				
		case "Quit": 
			dispose();
			break;			
		}
	}
	
	private void updateButtonEnabled() {
		logInButton.setEnabled(!userIsLogged);
		signInButton.setEnabled(!userIsLogged);
		singlePlayerButton.setEnabled(userIsLogged);
		multiPlayerButton.setEnabled(userIsLogged);
		logOutButton.setEnabled(userIsLogged);
		userInfoButton.setEnabled(userIsLogged);
		statisticButton.setEnabled(userIsLogged);
	}
}
