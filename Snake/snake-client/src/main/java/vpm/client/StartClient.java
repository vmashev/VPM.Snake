package vpm.client;

import javax.swing.SwingUtilities;

import vpm.ui.MainFrame;

public class StartClient {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new MainFrame();
				
			}
		});
	}
}
 