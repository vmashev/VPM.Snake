package vpm.ui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import vpm.ui.controler.UserInformationControler;

public class UserInformation extends JDialog {

	private JPanel contentPane;
	private String newPassword;
	public JTextField userNameFld;
	public JTextField firstNameFld;
	public JTextField lastNameFld;
	public JTextField emailFld;
	public JTextField maxScoreFld;
	
	private UserInformationControler controler;
	
	public UserInformation() {
		this.controler = new UserInformationControler(this);
		
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
		saveBtn.addActionListener(controler);
		contentPane.add(saveBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(475, 115, 89, 23);
		btnClose.addActionListener(controler);
		contentPane.add(btnClose);
		
		JButton changePassBtn = new JButton("Change Password");
		changePassBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		changePassBtn.setBounds(246, 115, 120, 23);
		changePassBtn.addActionListener(controler);
		contentPane.add(changePassBtn);
		
		JLabel maxScoreLbl = new JLabel("Max Score");
		maxScoreLbl.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		maxScoreLbl.setBounds(289, 12, 79, 20);
		contentPane.add(maxScoreLbl);
		
		maxScoreFld = new JTextField();
		maxScoreFld.setText((String) null);
		maxScoreFld.setColumns(10);
		maxScoreFld.setBounds(378, 11, 180, 25);
		maxScoreFld.setEditable(false);
		contentPane.add(maxScoreFld);
		
		controler.getUserInfo();
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}
	
}
