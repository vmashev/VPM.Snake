package vpm.model;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import vpm.helper.Direction;
import vpm.helper.GameStatus;


@Entity
public class GameInfo implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private UserEntity winnerPlayer;
	@ManyToOne
	private UserEntity playerOne;
	@ManyToOne
	private UserEntity playerTwo;
	private Integer playerOneScore;
	private Integer playerTwoScore;
	private Snake playerOneSnake;
	private LocalDateTime dateTime;
	private Integer width;
	private Integer height;
	private Integer speed;

	
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
	
	public GameInfo(UserEntity username, int width, int height, int speed) {
		this.playerOne = username;
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
	
	public Integer getPlayerOneScore() {
		return playerOneScore;
	}

	public void setPlayerOneScore(Integer playerOneScore) {
		this.playerOneScore = playerOneScore;
	}

	public Integer getPlayerTwoScore() {
		return playerTwoScore;
	}

	public void setPlayerTwoScore(Integer playerTwoScore) {
		this.playerTwoScore = playerTwoScore;
	}

	public Snake getPlayerOneSnake() {
		return playerOneSnake;
	}

	public void setPlayerOneSnake(Snake playerOneSnake) {
		this.playerOneSnake = playerOneSnake;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
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
	public UserEntity getWinnerPlayer() {
		return winnerPlayer;
	}

	public void setWinnerPlayer(UserEntity winnerPlayer) {
		this.winnerPlayer = winnerPlayer;
	}

	public UserEntity getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(UserEntity playerOne) {
		this.playerOne = playerOne;
	}

	public UserEntity getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(UserEntity playerTwo) {
		this.playerTwo = playerTwo;
	}

	@Override
	public String toString() {
		return "Username: " + getPlayerOne() + 
				", Speed: " + getSpeed()+ 
				", Board Width: " + getWidth() + 
				", Board Height: " + getHeight();
	}
	
	//Generate new random apple possition
	public Dot generateApple() {
		Dot randomAppleDot = null;
		boolean collison = true;
		
		while(collison) {

			int randomRow = ThreadLocalRandom.current().nextInt(0, height / Dot.RENDER_SIZE );
			int randomCol = ThreadLocalRandom.current().nextInt(0, width / Dot.RENDER_SIZE );
			randomAppleDot = new Dot(randomRow * Dot.RENDER_SIZE, randomCol * Dot.RENDER_SIZE);
			
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
	
	//Check for collison between snakes
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
	
	//Check for collison
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
	
	//Check for collison and update snake possition
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
	
	public static GameInfo parseJsonToGameInfo(String jsonString) {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		GameInfo gameInfo = gson.fromJson(jsonString, GameInfo.class);

		return gameInfo;
	}
	
	public static List<GameInfo> parseJsonToGameInfoList(String jsonString) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<GameInfo>>(){}.getType();
	    
		List<GameInfo> games = gson.fromJson(jsonString, type);
		
		return games;
	}
	
	public String parseToJson() {
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		
		return jsonString;
	}
	
	public static String parseGameInfoListToJson(List<GameInfo> games) {
		
		if(games == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(games);
		
		return jsonString;
	}
}
