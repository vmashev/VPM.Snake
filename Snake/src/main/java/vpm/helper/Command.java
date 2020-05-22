package vpm.helper;

import java.io.Serializable;

import vpm.comand.strategy.CommandExecuteStrategy;

public class Command implements Serializable{

	private int number;
	private String message;
	private String errorMessage;
	
	public Command(int number , String message) {
		this.number = number;
		this.message = message;
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
	
	public Command execute(CommandExecuteStrategy executeStrategy) {
		return executeStrategy.execute(this);
	}
}
