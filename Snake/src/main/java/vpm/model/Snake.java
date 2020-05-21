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
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;

@Embeddable
public class Snake implements Serializable{

	@AttributeOverrides({
		@AttributeOverride(name = "row" , column = @Column(name = "snake_head_row")),
		@AttributeOverride(name = "col" , column = @Column(name = "snake_head_col"))
	})
	private Dot head;
	
	@ElementCollection(fetch = FetchType.EAGER )
	@CollectionTable(name = "Snake")
	@OrderColumn
	@Column(name = "Position")
	private List<Dot> list = new ArrayList<Dot>();;
	
	public Snake() {}

	public void setList(List<Dot> list) {
		this.list = list;
	}

	public List<Dot> getList() {
		return list;
	}

	public void move(Dot dot) {
		list.remove(0);
		list.add(dot);
		this.head = dot;
	}
	
	public void grow(Dot dot) {
		list.add(dot);
		this.head = dot;
	}

	public Dot getHead() {
		return head;
	}

	public void setHead(Dot head) {
		this.head = head;
	}


}
