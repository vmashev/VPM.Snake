package vpm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import vpm.helper.Constants;
import vpm.helper.Direction;
import vpm.helper.GameStatus;

@Entity
public class GameInfo implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String hostUsername;
	private Snake hostSnake;
	private LocalDateTime dateTime;
	private Integer width;
	private Integer height;
	private Integer speed;
	private String winnerPlayer;
	private String playerOne;
	private String playerTwo;
	
	@Transient
	private Map<String,Snake> snakes = new HashMap<String, Snake>();
	
	@AttributeOverrides({
		@AttributeOverride(name = "row" , column = @Column(name = "apple_row")),
		@AttributeOverride(name = "col" , column = @Column(name = "apple_col"))
	})
	private Dot apple;

	@Enumerated(EnumType.STRING)
	private GameStatus status = null;
	
	public GameInfo() {	}
	
	public GameInfo(String username, int width, int height, int speed) {
		this.hostUsername = username;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.dateTime = LocalDateTime.now();
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Snake getHostSnake() {
		return hostSnake;
	}

	public void setHostSnake(Snake hostSnake) {
		this.hostSnake = hostSnake;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getHostUsername() {
		return hostUsername;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getSpeed() {
		return speed;
	}
	public Dot getApple() {
		return apple;
	}
	public void setApple(Dot apple) {
		this.apple = apple;
	}
	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}
	public Map<String, Snake> getSnakes() {
		return snakes;
	}
	public void setSnakes(Map<String, Snake> snakes) {
		this.snakes = snakes;
	}
	public String getWinnerPlayer() {
		return winnerPlayer;
	}

	public void setWinnerPlayer(String winnerPlayer) {
		this.winnerPlayer = winnerPlayer;
	}

	public String getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(String playerOne) {
		this.playerOne = playerOne;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(String playerTwo) {
		this.playerTwo = playerTwo;
	}

	@Override
	public String toString() {
		return "Username: " + getHostUsername() + 
				", Speed: " + getSpeed()+ 
				", Board Width: " + getWidth() + 
				", Board Height: " + getHeight();
	}
	
	
	public Dot generateApple() {
		Dot randomAppleDot = null;
		boolean collison = true;
		
		while(collison) {

			int randomRow = ThreadLocalRandom.current().nextInt(0, height / Constants.SIZE );
			int randomCol = ThreadLocalRandom.current().nextInt(0, width / Constants.SIZE );
			randomAppleDot = new Dot(randomRow * Constants.SIZE, randomCol * Constants.SIZE);
			
			collison = false;
			for (Snake snake : getSnakes().values()) {
				if (hasSnakeCollison(snake, randomAppleDot)) {
					collison = true;
					break;
				}				
			}		
		}
		
		return randomAppleDot;
	}
	
	private boolean hasSnakeCollison(Snake snake , Dot dot) {
		if(snake == null) {
			return false;
		}
		
		for (int i = 0 ; i < snake.getList().size() -1 ; i++) {
			Dot snakeDot = snake.getList().get(i);
			if(snakeDot.equals(dot)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasCollison(Dot headDot) {
		
		if((headDot.getRow() < 0) || (headDot.getRow() == height) ||
			(headDot.getCol() < 0) || (headDot.getCol() == width)) {
			return true;
		}
		
		for (Snake snake : getSnakes().values()) {
			if (hasSnakeCollison(snake, headDot)) {
				return true;
			}
		}

		return false;
	}
	
	public boolean updateSnake(String username, Direction direction) {
		Snake snake;
		if((snake = snakes.get(username)) != null) {
			snake.setDirection(direction);
			snake.move();
			
			if(snake.getHead().equals(getApple())) {
				snake.setScore(snake.getScore()+1);
				setApple(generateApple());
			} else {
				if(hasCollison(snake.getHead())) {
					return false;
				}
				snake.getList().remove(0);
			}
		} else {
			return false;
		}
		
		return true;
	}
}
