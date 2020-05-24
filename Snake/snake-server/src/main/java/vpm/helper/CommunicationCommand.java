package vpm.helper;

import java.io.Serializable;

import vpm.model.UserEntity;

public class CommunicationCommand implements Serializable{

	private int number;
	private UserEntity username;
	private String message;
	private String errorMessage;
	
	public CommunicationCommand(UserEntity username, int number , String message) {
		this.number = number;
		this.message = message;
		this.username = username;
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
	
	public UserEntity getUsername() {
		return username;
	}

}
