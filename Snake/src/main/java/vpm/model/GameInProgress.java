package vpm.model;

import java.util.ArrayList;
import java.util.List;

import vpm.helper.Direction;
import vpm.helper.Dot;
import vpm.helper.Snake;

public class GameInProgress {
	
	private String userName;
	private int width;
	private int height;
	private int score;
	private int speed;
	private int numberOfPlayers;
	private Dot apple;
	private int newRow;
	private int newCol;
	private Direction direction = null;
	private List<Snake> snakes = new ArrayList<Snake>();
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	public Dot getApple() {
		return apple;
	}
	public void setApple(Dot apple) {
		this.apple = apple;
	}
	public int getNewRow() {
		return newRow;
	}
	public void setNewRow(int newRow) {
		this.newRow = newRow;
	}
	public int getNewCol() {
		return newCol;
	}
	public void setNewCol(int newCol) {
		this.newCol = newCol;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public List<Snake> getSnakes() {
		return snakes;
	}
	public void setSnakes(List<Snake> snakes) {
		this.snakes = snakes;
	}
	
}
