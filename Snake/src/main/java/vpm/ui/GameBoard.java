package vpm.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import vpm.model.GameInfo;

public class GameBoard extends JFrame {

	private JPanel contentPane;

	public GameBoard(GameInfo gameInfo,ObjectOutputStream objectOutput, ObjectInputStream objectInput) throws IOException {
		Board board = new Board(gameInfo, objectOutput, objectInput);

		this.contentPane = board;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, gameInfo.getWidth(), gameInfo.getHeight());
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Play");
		setContentPane(contentPane);
	}

}
