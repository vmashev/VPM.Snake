package vpm.helper;

import java.io.Serializable;

import vpm.comand.Command;

public class CommunicationCommand implements Serializable{

	private int number;
	private String username;
	private String message;
	private String errorMessage;
	
	public CommunicationCommand(int number , String message) {
		this.number = number;
		this.message = message;
		
		ClientSetup setup = ClientSetup.createInstance();
		this.username = setup.getUserName();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getUsername() {
		return username;
	}

}
