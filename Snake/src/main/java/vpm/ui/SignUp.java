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
import vpm.model.service.UserService;
import vpm.ui.controler.SignUpControler;

public class SignUp extends JDialog {

	private JPanel contentPane;
	private JLabel lblPassword;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	public JTextField userFld;
	public JPasswordField passwordField;
	public JPasswordField passwordField2;
	public JTextField firsNameFld;
	public JTextField lastNameFld;
	public JTextField emailFld;
	private SignUpControler controler;

	public SignUp() {
		controler = new SignUpControler(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 310, 310);
		setTitle("SignUp");
		setModal(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 14, 79, 20);
		contentPane.add(lblNewLabel);
		
		userFld = new JTextField();
		userFld.setBounds(99, 14, 140, 25);
		contentPane.add(userFld);
		userFld.setColumns(10);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPassword.setBounds(10, 48, 79, 20);
		contentPane.add(lblPassword);
		
		lblNewLabel_2 = new JLabel("Re-Enter");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 82, 79, 20);
		contentPane.add(lblNewLabel_2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(99, 49, 140, 25);
		contentPane.add(passwordField);
		
		passwordField2 = new JPasswordField();
		passwordField2.setBounds(99, 83, 140, 25);
		contentPane.add(passwordField2);
		
		JButton loginBtn = new JButton("SignUp");
		loginBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		loginBtn.setBounds(88, 239, 89, 23);
		loginBtn.addActionListener(controler);
		
		contentPane.add(loginBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(187, 239, 89, 23);
		btnClose.addActionListener(controler);
		contentPane.add(btnClose);
		
		lblNewLabel_1 = new JLabel("First Name");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 130, 79, 20);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_3 = new JLabel("Last Name");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(10, 166, 79, 20);
		contentPane.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("E-Mail");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(10, 202, 79, 20);
		contentPane.add(lblNewLabel_4);
		
		firsNameFld = new JTextField();
		firsNameFld.setColumns(10);
		firsNameFld.setBounds(99, 129, 180, 25);
		contentPane.add(firsNameFld);
		
		lastNameFld = new JTextField();
		lastNameFld.setColumns(10);
		lastNameFld.setBounds(99, 167, 180, 25);
		contentPane.add(lastNameFld);
		
		emailFld = new JTextField();
		emailFld.setColumns(10);
		emailFld.setBounds(99, 203, 180, 25);
		contentPane.add(emailFld);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}
}
