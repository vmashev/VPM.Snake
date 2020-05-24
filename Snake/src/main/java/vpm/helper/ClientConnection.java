package vpm.helper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import vpm.model.UserEntity;

public class ClientConnection {

	private UserEntity username;
	private ObjectOutputStream objectOutput ;
	private ObjectInputStream objectInput ;
	
	public ClientConnection(UserEntity username , ObjectOutputStream objectOutput, ObjectInputStream objectInput) {
		this.username = username;
		this.objectOutput = objectOutput;
		this.objectInput = objectInput;
	}

	public UserEntity getUsername() {
		return username;
	}

	public void setUsername(UserEntity username) {
		this.username = username;
	}

	public ObjectOutputStream getObjectOutput() {
		return objectOutput;
	}

	public void setObjectOutput(ObjectOutputStream objectOutput) {
		this.objectOutput = objectOutput;
	}

	public ObjectInputStream getObjectInput() {
		return objectInput;
	}

	public void setObjectInput(ObjectInputStream objectInput) {
		this.objectInput = objectInput;
	}

}
