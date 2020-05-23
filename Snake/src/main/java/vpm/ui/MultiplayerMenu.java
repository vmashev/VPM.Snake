package vpm.ui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import vpm.ui.controler.MultiplayerMenuControler;

public class MultiplayerMenu extends JDialog {
	public JTable table;
	public DefaultTableModel model;
	private MultiplayerMenuControler controler;
	
	public MultiplayerMenu() {
		this.controler = new MultiplayerMenuControler(this);
		
		setBounds(100, 100, 710, 240);
		getContentPane().setLayout(null);
		setTitle("Multiplayer menu");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 674, 143);
		getContentPane().add(scrollPane);
		
		model = new DefaultTableModel( new Object[][] {}, new String[] { "Line No.", "Username", "Board Height", "Board Width", "Game Speed" } ) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		JButton newLobbyBtn = new JButton("New Lobby");
		newLobbyBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		newLobbyBtn.setBounds(385, 165, 101, 23);
		newLobbyBtn.addActionListener(controler);
		getContentPane().add(newLobbyBtn);
		
		JButton joinBtn = new JButton("Join");
		joinBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		joinBtn.setBounds(496, 165, 89, 23);
		joinBtn.addActionListener(controler);
		getContentPane().add(joinBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(595, 165, 89, 23);
		btnClose.addActionListener(controler);
		getContentPane().add(btnClose);
		
		controler.getLobbies();
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}
}
