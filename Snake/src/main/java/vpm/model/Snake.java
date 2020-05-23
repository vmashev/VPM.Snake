package vpm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;

import vpm.helper.Constants;
import vpm.helper.Direction;

@Embeddable
public class Snake implements Serializable{

	@AttributeOverrides({
		@AttributeOverride(name = "row" , column = @Column(name = "snake_head_row")),
		@AttributeOverride(name = "col" , column = @Column(name = "snake_head_col"))
	})
	private Dot head;
	private int score;
	@Transient
	private Integer rowChange;
	@Transient
	private Integer colChange;
	//@Enumerated(EnumType.STRING)
	@Transient
	private Direction direction = null;
	
	@ElementCollection(fetch = FetchType.EAGER )
	@CollectionTable(name = "SnakeBody")
	@OrderColumn
	@Column(name = "Position")
	private List<Dot> list = new ArrayList<Dot>();
	
	public Snake() {}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Integer getRowChange() {
		return rowChange == null ? 0:rowChange;
	}
	public void setRowChange(Integer rowChange) {
		this.rowChange = rowChange;
	}
	public Integer getColChange() {
		return colChange == null ? 0:colChange;
	}
	public void setColChange(Integer colChange) {
		this.colChange = colChange;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public void setList(List<Dot> list) {
		this.list = list;
	}
	public List<Dot> getList() {
		return list;
	}
	public Dot getHead() {
		return head;
	}
	public void setHead(Dot head) {
		this.head = head;
	}
	
	public void move() {
		
		if(getDirection() == Direction.UP && rowChange == null) {
			rowChange = -Constants.SIZE;
			colChange = null;
		}
		if(getDirection() == Direction.DOWN && rowChange == null) {
			rowChange = Constants.SIZE;
			colChange = null;
		}	
		if(getDirection() == Direction.LEFT && colChange == null) {
			rowChange = null;
			colChange = -Constants.SIZE;
		}
		if(getDirection() == Direction.RIGHT && colChange == null) {
			rowChange = null;
			colChange = Constants.SIZE;
		}
		
		
		if(rowChange != null || colChange != null) {
			
			Dot nextDot = new Dot(getHead().getRow() + getRowChange(), getHead().getCol() + getColChange());
			
			list.add(nextDot);
			this.head = nextDot;
	
		}	
	}
	
	public static Snake createSnake(int playerNo, int boardWidth) {
		Dot tempDot = null;
		Snake snake = new Snake();
			
		switch (playerNo) {
		case 1:
			for(int j = 0 ; j < 3 ; j++) {
				tempDot = new Dot(j * Constants.SIZE, 1 * Constants.SIZE);
				snake.getList().add(tempDot) ;
			}
			break;

		case 2:
			for(int j = 0 ; j < 3 ; j++) {
				tempDot = new Dot(j * Constants.SIZE, boardWidth - (2 * Constants.SIZE));
				snake.getList().add(tempDot) ;
			}
			break;
		}
		
		snake.setHead(tempDot);
		snake.setDirection(Direction.DOWN);
		
		return snake;
	}
}
