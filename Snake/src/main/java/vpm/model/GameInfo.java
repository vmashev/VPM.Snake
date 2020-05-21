package vpm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import vpm.helper.Constants;
import vpm.helper.Direction;
import vpm.helper.GameStatus;

@Entity
public class GameInfo implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String username;
	private LocalDateTime dateTime;
	private int width;
	private int height;
	private int speed;
	
	private int score;
	private int rowChange;
	private int colChange;
	
	private Snake snake ;
	
	@AttributeOverrides({
		@AttributeOverride(name = "row" , column = @Column(name = "apple_row")),
		@AttributeOverride(name = "col" , column = @Column(name = "apple_col"))
	})
	private Dot apple;
	@Enumerated(EnumType.STRING)
	private Direction direction = null;
	@Enumerated(EnumType.STRING)
	private GameStatus status = null;
	
	public GameInfo() {	}
	
	public GameInfo(String username, int width, int height, int speed) {
		this.username = username;
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

	
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getUserName() {
		return username;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getRowChange() {
		return rowChange;
	}

	public void setRowChange(int rowChange) {
		this.rowChange = rowChange;
	}

	public int getColChange() {
		return colChange;
	}

	public void setColChange(int colChange) {
		this.colChange = colChange;
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

	public Snake getSnake() {
		return snake;
	}
	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	public Snake createSnake() {
		Dot tempDot = null;
		Snake snake = new Snake();
		
		for(int i = 0 ; i < 3 ; i++) {
			tempDot = new Dot(i * Constants.SIZE, 1 * Constants.SIZE);
			snake.getList().add(tempDot) ;
		}
		snake.setHead(tempDot);
		return snake;
	}
	
	public Dot generateApple() {
		Dot randomAppleDot = null;
		boolean collison = true;
		
		while(collison) {

			int randomRow = ThreadLocalRandom.current().nextInt(0, height / Constants.SIZE );
			int randomCol = ThreadLocalRandom.current().nextInt(0, width / Constants.SIZE );
			randomAppleDot = new Dot(randomRow * Constants.SIZE, randomCol * Constants.SIZE);
			
			collison = false;
			if (hasSnakeCollison(snake, randomAppleDot)) {
				collison = true;
				break;
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
	
	public boolean hasCollison(Dot nextDot) {
		
		if((nextDot.getRow() < 0) || (nextDot.getRow() == height) ||
			(nextDot.getCol() < 0) || (nextDot.getCol() == width)) {
			return true;
		}
		
		if (hasSnakeCollison(snake, nextDot)) {
			return true;
		}
		
		return false;
	}
	
	public boolean update(int player) {
		
		if(getDirection() == Direction.UP && rowChange == 0) {
			rowChange = -Constants.SIZE;
			colChange = 0;
		}
		if(getDirection() == Direction.DOWN && rowChange == 0) {
			rowChange = Constants.SIZE;
			colChange = 0;
		}	
		if(getDirection() == Direction.LEFT && colChange == 0) {
			rowChange = 0;
			colChange = -Constants.SIZE;
		}
		if(getDirection() == Direction.RIGHT && colChange == 0) {
			rowChange = 0;
			colChange = Constants.SIZE;
		}
		
		
		if(rowChange != 0 || colChange != 0) {
			
			Dot nextDot = new Dot(getSnake().getHead().getRow() + rowChange, getSnake().getHead().getCol() + colChange);
			
			if(hasCollison(nextDot)) {
				return false;
			}
				
			if(nextDot.equals(getApple())) {
				getSnake().grow(nextDot);
				setScore(getScore()+1); ;
				setApple(generateApple());
			} else {
				getSnake().move(nextDot);
			}
		}
		
		return true;
	}
}
