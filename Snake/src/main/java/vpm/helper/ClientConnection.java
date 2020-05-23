package vpm.helper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientConnection {

	private String username;
	private ObjectOutputStream objectOutput ;
	private ObjectInputStream objectInput ;
	
	public ClientConnection(String username , ObjectOutputStream objectOutput, ObjectInputStream objectInput) {
		this.username = username;
		this.objectOutput = objectOutput;
		this.objectInput = objectInput;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
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
