package vpm.ui.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vpm.helper.EncryptionUtils;
import vpm.ui.ChangePassword;

public class ChangePasswordControler  implements ActionListener{

	private ChangePassword changePassword;
	
	public ChangePasswordControler(ChangePassword changePassword) {
		this.changePassword = changePassword;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "OK":
			
			String password = new String(changePassword.passwordFld.getPassword());
			String rePassword = new String(changePassword.passwordField2.getPassword());
			
			if(password == "") {
				changePassword.showMessage("Password is empty.");
				return;
			}
			
			if(!passwordIsValid(password , rePassword)) {
				changePassword.passwordField2.setText("");
				return;
			}
			
			setNewPassword(password);
			
			changePassword.dispose();
			break;
		case "Cancel":
			changePassword.dispose();
			break;		
		}
	}

	private boolean passwordIsValid(String password, String rePassword) {
		if(password == "") {
			changePassword.showMessage("Password is empty.");
			return false;
		}
		
		if( !password.equals(rePassword)) {
			changePassword.showMessage("Wrong re-entered password.");
			return false;
		}
		
		return true;
	}
	
	private void setNewPassword(String password) {
		password = EncryptionUtils.encryptMD5(password);
		changePassword.userInformation.setNewPassword(password);
	}
}
