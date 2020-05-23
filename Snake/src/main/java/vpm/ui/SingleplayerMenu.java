package vpm.ui;

import java.awt.Font;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import vpm.ui.controler.SingleplayerMenuControler;

public class SingleplayerMenu extends JDialog{

	private JPanel contentPane;
	public JTable table;
	public DefaultTableModel model;
	private SingleplayerMenuControler controler;

	
	public SingleplayerMenu() {
		this.controler = new SingleplayerMenuControler(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 710, 240);
		setTitle("Singleplayer Menu");
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton joinBtn = new JButton("Resume");
		joinBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		joinBtn.setBounds(496, 165, 89, 23);
		joinBtn.addActionListener(controler);
		contentPane.add(joinBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(595, 165, 89, 23);
		btnClose.addActionListener(controler);
		contentPane.add(btnClose);
		
		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		newGameBtn.setBounds(397, 165, 89, 23);
		newGameBtn.addActionListener(controler);
		contentPane.add(newGameBtn);
		
		model = new DefaultTableModel( new Object[][] {}, new String[] { "Line No.", "Username", "DateTime", "Board Height", "Board Width", "Game Speed" } ) {
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 674, 143);
		contentPane.add(scrollPane);
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		table.setAutoResizeMode(5);

		controler.getSavedGames();
		
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}

}
