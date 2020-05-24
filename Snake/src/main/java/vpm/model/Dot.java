package vpm.model;

import java.awt.Graphics2D;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Dot {
	
	private int row;
	private int col;
	@Transient
	public static final int RENDER_SIZE = 10;
	
	public Dot() {}
	
	public Dot(int row, int col) {
		this.row = row ;
		this.col = col ;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	@Override
	public String toString() {
		return "row: " + row + ", col: " + col;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Dot)) {
			return false;
		}
		
		Dot other = (Dot)obj;
		
		return ((this.row == other.row) && (this.col == other.col));
	}
	
	public void render(Graphics2D graphics2d) {
		graphics2d.fillRect(col + 1, row + 1, RENDER_SIZE - 2, RENDER_SIZE - 2);
	}
}
