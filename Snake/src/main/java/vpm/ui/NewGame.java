package vpm.ui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import vpm.ui.controler.NewGameControler;

public class NewGame extends JDialog{

	private JPanel contentPane;
	public JTextField speedFld;
	public JTextField heightFld;
	public JTextField widthFld;
	private NewGameControler controler;
	
	public NewGame() {
		this.controler = new NewGameControler(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 190);
		setTitle("New singleplayer game");
		setModal(true);
		setLocationRelativeTo(null);
		
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		speedFld = new JTextField();
		speedFld.setColumns(10);
		speedFld.setBounds(80, 11, 140, 25);
		speedFld.setText(String.valueOf(5));
		contentPane.add(speedFld);
		
		JButton okBtn = new JButton("OK");
		okBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		okBtn.setBounds(36, 121, 89, 23);
		okBtn.addActionListener(controler);
		contentPane.add(okBtn);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnCancel.setBounds(135, 121, 89, 23);
		btnCancel.addActionListener(controler);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel_2 = new JLabel("Width (min 300)");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 82, 60, 20);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblPassword = new JLabel("Height (min 300)");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblPassword.setBounds(10, 48, 60, 20);
		contentPane.add(lblPassword);
		
		JLabel lblNewLabel = new JLabel("Speed");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 14, 60, 20);
		contentPane.add(lblNewLabel);
		
		heightFld = new JTextField();
		heightFld.setColumns(10);
		heightFld.setBounds(80, 46, 140, 25);
		heightFld.setText(String.valueOf(300));
		contentPane.add(heightFld);
		
		widthFld = new JTextField();
		widthFld.setColumns(10);
		widthFld.setBounds(80, 80, 140, 25);
		widthFld.setText(String.valueOf(300));
		contentPane.add(widthFld);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}
}
