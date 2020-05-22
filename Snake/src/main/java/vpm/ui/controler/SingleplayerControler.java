package vpm.ui.controler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import vpm.ui.Board;
import vpm.ui.NewSingleplayerGame;

public class SingleplayerControler  implements ActionListener {

	private NewSingleplayerGame singleplayer;
	
	public SingleplayerControler(NewSingleplayerGame singleplayer) {
		this.singleplayer = singleplayer;
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
			singleplayer.dispose();
			break;
		case "Cancel":
			singleplayer.dispose();
			break;		
		}
	}

	private void startGame() throws UnknownHostException, IOException {
		int height = Integer.valueOf(singleplayer.heightFld.getText());
		int width = Integer.valueOf(singleplayer.widthFld.getText());
		int speed = Integer.valueOf(singleplayer.speedFld.getText());
		
		if(height < 300 || width < 300) {
			singleplayer.showMessage("Width and Height must me greater than 300.");
		} else {
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
	
}
