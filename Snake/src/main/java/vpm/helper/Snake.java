package vpm.helper;

import java.util.LinkedList;
import java.util.List;

public class Snake {

	private Dot head;
	private List<Dot> list = new LinkedList<Dot>();
	
	public Snake() {}
	
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
