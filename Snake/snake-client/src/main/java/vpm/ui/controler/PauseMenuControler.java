package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vpm.helper.GameStatus;
import vpm.ui.PauseMenu;

public class PauseMenuControler  implements ActionListener{

	private PauseMenu pauseMenu;
	
	public PauseMenuControler(PauseMenu pauseMenu) {
		this.pauseMenu = pauseMenu;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Resume":
			pauseMenu.board.getSnakeMove().setStatus(GameStatus.Run);
			pauseMenu.dispose();
			break;
		case "Quit":
			pauseMenu.board.getSnakeMove().setStatus(GameStatus.Save);
			pauseMenu.dispose();
			break;		
		}
	}

}
