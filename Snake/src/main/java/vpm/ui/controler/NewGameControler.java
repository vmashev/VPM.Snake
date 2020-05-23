package vpm.ui.controler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import vpm.ui.Board;
import vpm.ui.NewGame;

public class NewGameControler  implements ActionListener {

	private NewGame newGame;
	
	public NewGameControler(NewGame newGame) {
		this.newGame = newGame;
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
			newGame.dispose();
			break;
		case "Cancel":
			newGame.dispose();
			break;		
		}
	}

	private void startGame() throws UnknownHostException, IOException{
		int height = Integer.valueOf(newGame.heightFld.getText());
		int width = Integer.valueOf(newGame.widthFld.getText());
		int speed = Integer.valueOf(newGame.speedFld.getText());
		
		if(height < 300 || width < 300) {
			newGame.showMessage("Width and Height must me greater than 300.");
		} else {
			Board board = new Board(width, height , speed, newGame.multiplayer);
			
			JFrame frame = new JFrame("Play");
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
