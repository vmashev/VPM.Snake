package vpm.helper;

import java.io.Serializable;

import com.google.gson.Gson;

public class SnakeMoveInfo implements Serializable{

	private String username;
	private Direction direction ;
	private GameStatus status ;

	public SnakeMoveInfo(String username, GameStatus status, Direction direction) {
		this.username = username;
		this.direction = direction;
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	public static SnakeMoveInfo parseJsonToSnakeMoveInfo(String jsonString) {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		SnakeMoveInfo snakeMove = gson.fromJson(jsonString, SnakeMoveInfo.class);

		return snakeMove;
	}
	
	public String parseToJson() {
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		
		return jsonString;
	}
}
