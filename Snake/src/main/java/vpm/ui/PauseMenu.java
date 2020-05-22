package vpm.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vpm.helper.GameStatus;
import vpm.ui.controler.PauseMenuControler;

public class PauseMenu extends JDialog{

	private JPanel contentPane;
	public Board board;
	private PauseMenuControler controler;
	
	public PauseMenu(Board board) {
		this.board = board;
		this.controler = new PauseMenuControler(this);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 220, 80);
		setTitle("Pause");
		setModal(true);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton resumerBtn = new JButton("Resume");
		resumerBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		resumerBtn.setBounds(10, 11, 90, 23);
		resumerBtn.addActionListener(controler);
		contentPane.add(resumerBtn);
		
		JButton quitBtn = new JButton("Quit");
		quitBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		quitBtn.setBounds(110, 11, 90, 23);
		quitBtn.addActionListener(controler);
		contentPane.add(quitBtn);
	}

}
