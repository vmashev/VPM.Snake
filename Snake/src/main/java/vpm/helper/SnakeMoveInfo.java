package vpm.helper;

import java.io.Serializable;

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
	
}
