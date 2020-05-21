package vpm.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Singleplayer extends JDialog implements ActionListener {

	private JPanel contentPane;
	private JTextField speedFld;
	private JTextField heightFld;
	private JTextField widthFld;

	public Singleplayer() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 190);
		setTitle("New singleplayer game");
		setModal(true);
		
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
		
		JButton loginBtn = new JButton("Start");
		loginBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		loginBtn.setBounds(36, 121, 89, 23);
		loginBtn.addActionListener(this);
		contentPane.add(loginBtn);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnCancel.setBounds(135, 121, 89, 23);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel_2 = new JLabel("Width");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 82, 60, 20);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblPassword = new JLabel("Height");
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

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Start":
			try {
				startGame();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			dispose();
			break;
		case "Cancel":
			dispose();
			break;		
		}
	}

	private void startGame() throws UnknownHostException, IOException {
		int height = Integer.valueOf(heightFld.getText());
		int width = Integer.valueOf(widthFld.getText());
		int speed = Integer.valueOf(speedFld.getText());
		
		Board board = new Board(width, height , speed);
		
		JFrame frame = new JFrame("Singleplayer");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setContentPane(board);
		frame.setResizable(false);
		frame.pack();
		frame.setPreferredSize(new Dimension(width, height));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
