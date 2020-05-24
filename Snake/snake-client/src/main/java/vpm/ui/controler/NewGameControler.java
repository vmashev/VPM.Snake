package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vpm.ui.NewGame;

public class NewGameControler  implements ActionListener {

	private NewGame newGame;
	
	public NewGameControler(NewGame newGame) {
		this.newGame = newGame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "OK":
			int height = Integer.valueOf(newGame.heightFld.getText());
			int width = Integer.valueOf(newGame.widthFld.getText());
			int speed = Integer.valueOf(newGame.speedFld.getText());
			
			if(height < 300 || width < 300) {
				newGame.showMessage("Width and Height must me greater than 300.");
			} else {
				
				
				newGame.dispose();
			}
			break;
		case "Cancel":
			newGame.heightFld.setText("");
			newGame.widthFld.setText("");
			newGame.speedFld.setText("");
			newGame.dispose();
			break;		
		}
	}

	
}
