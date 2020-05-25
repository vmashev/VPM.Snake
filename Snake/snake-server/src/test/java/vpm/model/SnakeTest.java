package vpm.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import vpm.helper.Direction;

class SnakeTest {

	@Test
	void testMove() {
		Snake snake = new Snake();
		Dot head = new Dot(1 * Dot.RENDER_SIZE, 1* Dot.RENDER_SIZE);
		snake.setHead(head);
		
		snake.move();
		assertEquals(head, snake.getHead());
		
		head = new Dot(2* Dot.RENDER_SIZE, 1* Dot.RENDER_SIZE);
		snake.setDirection(Direction.DOWN);
		snake.move();
		assertEquals(head, snake.getHead());
		
		head = new Dot(2* Dot.RENDER_SIZE, 0);
		snake.setDirection(Direction.LEFT);
		snake.move();
		assertEquals(head, snake.getHead());
		
		head = new Dot(1* Dot.RENDER_SIZE, 0);
		snake.setDirection(Direction.UP);
		snake.move();
		assertEquals(head, snake.getHead());
		
		head = new Dot(1* Dot.RENDER_SIZE, 1* Dot.RENDER_SIZE);
		snake.setDirection(Direction.RIGHT);
		snake.move();
		assertEquals(head, snake.getHead());
		
	}

	@Test
	void testCreateSnake() {
		Snake snake = Snake.createSnake(1, 300);
		Snake snake2 = Snake.createSnake(2, 300);
		Dot head = new Dot( 2 * Dot.RENDER_SIZE , 1* Dot.RENDER_SIZE);
		Dot head2 = new Dot( 2 * Dot.RENDER_SIZE , 300 - (2 * Dot.RENDER_SIZE));
		
		assertEquals(head, snake.getHead());
		assertEquals(3, snake.getList().size());
		assertEquals(Direction.DOWN, snake.getDirection());
		assertEquals(0, snake.getRowChange() );
		assertEquals(0, snake.getColChange() );
		
		assertEquals(head2, snake2.getHead());
		
	}

}
