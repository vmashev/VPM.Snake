package vpm.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import vpm.helper.EncryptionUtils;

public class ChangePassword extends JDialog implements ActionListener {

	private JPanel contentPane;
	private JPasswordField passwordField2;
	private JPasswordField passwordFld;
	private UserInformation userInformation;
	
	public ChangePassword(UserInformation userInformation) {
		this.userInformation = userInformation;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 160);
		setTitle("Change password");
		setModal(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnClose = new JButton("Cancel");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(135, 91, 89, 23);
		btnClose.addActionListener(this);
		contentPane.add(btnClose);
		
		JButton btnOK = new JButton("OK");
		btnOK.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnOK.setBounds(36, 91, 89, 23);
		btnOK.addActionListener(this);
		contentPane.add(btnOK);
		
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(80, 45, 140, 25);
		contentPane.add(passwordField2);
		
		JLabel lblPassword = new JLabel("Re-Enter");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPassword.setBounds(10, 47, 60, 20);
		contentPane.add(lblPassword);
		
		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 14, 60, 20);
		contentPane.add(lblNewLabel);
		
		passwordFld = new JPasswordField();
		passwordFld.setBounds(80, 11, 140, 25);
		contentPane.add(passwordFld);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "OK":
			
			String password = new String(passwordFld.getPassword());
			String rePassword = new String(passwordField2.getPassword());
			
			if(password == "") {
				JOptionPane.showMessageDialog (	this , "Password is empty." , "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(!passwordIsValid(password , rePassword)) {
				passwordField2.setText("");
				return;
			}
			
			setNewPassword(password);
			
			dispose();
			break;
		case "Cancel":
			dispose();
			break;		
		}
	}

	private boolean passwordIsValid(String password, String rePassword) {
		if(password == "") {
			JOptionPane.showMessageDialog (	this , "Password is empty." , "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if( !password.equals(rePassword)) {
			JOptionPane.showMessageDialog (	this , "Wrong re-entered password." , "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private void setNewPassword(String password) {
		password = EncryptionUtils.encryptMD5(password);
		userInformation.setNewPassword(password);
	}
	
}
