package vpm.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import vpm.helper.ClientSetup;
import vpm.ui.controler.StatisticControler;

public class Statistic extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public JTextField usernameFld;
	public JTextField maxScoreFld;
	public JTable table;
	public DefaultTableModel model;
	private StatisticControler controler;
	
	public Statistic() {
		this.controler = new StatisticControler(this);
		
		setBounds(100, 100, 710, 270);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 11, 79, 20);
		contentPanel.add(lblNewLabel);
		
		usernameFld = new JTextField();
		usernameFld.setColumns(10);
		usernameFld.setBounds(99, 11, 140, 25);
		contentPanel.add(usernameFld);
		
		JLabel lblMaxScore = new JLabel("Max Score");
		lblMaxScore.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblMaxScore.setBounds(249, 11, 79, 20);
		contentPanel.add(lblMaxScore);
		
		maxScoreFld = new JTextField();
		maxScoreFld.setColumns(10);
		maxScoreFld.setBounds(338, 11, 140, 25);
		maxScoreFld.setEditable(false);
		contentPanel.add(maxScoreFld);
		
		JButton searchBtn = new JButton("Search");
		searchBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		searchBtn.setBounds(496, 197, 89, 23);
		searchBtn.addActionListener(controler);
		contentPanel.add(searchBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(595, 197, 89, 23);
		btnClose.addActionListener(controler);
		contentPanel.add(btnClose);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 674, 145);
		contentPanel.add(scrollPane);
		
		model = new DefaultTableModel( new Object[][] {}, new String[] { "Line No.", "Username", "Score", "DateTime", "Board Height", "Board Width", "Game Speed" } ) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false	};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		ClientSetup clientSetup = ClientSetup.createInstance();
		controler.getStatistics(clientSetup.getUserName());
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog (	this , msg , "Error", JOptionPane.ERROR_MESSAGE);
	}
}
