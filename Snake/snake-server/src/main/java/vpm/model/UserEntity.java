package vpm.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;

@Entity
public class UserEntity implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false,length = 30)
	private String username;
	
	@Column(length = 50)
	private String firstName;
	
	@Column(length = 50)
	private String lastName;
	
	@Column(length = 100)
	private String email;
	
	private String encryptedPassword;
	
	private int maxScore;
	
	public UserEntity() {}
	
	public UserEntity(String username) {
		this.username = username;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String nickname) {
		this.username = nickname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	public int getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof UserEntity)) {
			return false;
		}
		
		UserEntity other = (UserEntity) obj;
		
		return Objects.equals(this.getUsername() , other.getUsername()) ;
	}
	
	public static UserEntity parseJsonToUserEntity(String jsonString) {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		UserEntity userEntity = gson.fromJson(jsonString, UserEntity.class);

		return userEntity;
	}
	
	public String parseToJson() {
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		
		return jsonString;
	}
}
