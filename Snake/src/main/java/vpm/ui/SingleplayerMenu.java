package vpm.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.model.UserEntity;
import vpm.ui.controler.SingleplayerMenuControler;

public class SingleplayerMenu extends JDialog{

	private JPanel contentPane;
	public JTable table;
	public DefaultTableModel model;
	private SingleplayerMenuControler controler;

	
	public SingleplayerMenu() throws UnknownHostException, IOException {
		this.controler = new SingleplayerMenuControler(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 570, 230);
		setTitle("Singleplayer Menu");
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton joinBtn = new JButton("Resume");
		joinBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		joinBtn.setBounds(366, 165, 89, 23);
		joinBtn.addActionListener(controler);
		contentPane.add(joinBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(465, 165, 89, 23);
		btnClose.addActionListener(controler);
		contentPane.add(btnClose);
		
		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		newGameBtn.setBounds(267, 165, 89, 23);
		newGameBtn.addActionListener(controler);
		contentPane.add(newGameBtn);
		
		model = new DefaultTableModel( new Object[][] {}, new String[] { "Line No.", "Username", "DateTime", "Board Height", "Board Width", "Game Speed"} );
		
		model.setColumnIdentifiers(new String[] { "Line No.", "Username", "DateTime", "Board Height", "Board Width", "Game Speed"});
		table = new JTable(model);
		table.setBounds(10, 11, 544, 143);
		table.setAutoResizeMode(5);

		contentPane.add(table);

		controler.getSavedGames();
		
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}

}
