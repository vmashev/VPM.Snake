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
	private int rowChange;
	@Transient
	private int colChange;
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
			
			Dot nextDot = new Dot(getHead().getRow() + rowChange, getHead().getCol() + colChange);
			
			list.add(nextDot);
			this.head = nextDot;
	
		}	
	}
}
