package vpm.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PauseMenu extends JDialog implements ActionListener{

	private JPanel contentPane;
	
	public PauseMenu(Board board) {
		
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
		resumerBtn.addActionListener(this);
		contentPane.add(resumerBtn);
		
		JButton quitBtn = new JButton("Quit");
		quitBtn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		quitBtn.setBounds(110, 11, 90, 23);
		quitBtn.addActionListener(this);
		contentPane.add(quitBtn);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Resume":
			dispose();
			break;
		case "Quit":
			
			dispose();
			break;		
		}
	}

}
