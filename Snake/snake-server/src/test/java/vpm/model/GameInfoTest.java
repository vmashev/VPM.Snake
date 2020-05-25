package vpm.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import vpm.helper.Direction;

class GameInfoTest {

	@Test
	void testGenerateApple() {
		UserEntity userEntity = new UserEntity("username");
		GameInfo gameInfo = new GameInfo(userEntity, 300, 300, 5);
		Dot apple = gameInfo.generateApple();
		
		assertTrue(apple.getRow() > 0);
		assertTrue(apple.getRow() < 300);
		assertTrue(apple.getCol() > 0);
		assertTrue(apple.getCol() < 300);
		
	}

	@Test
	void testHasCollison() {
		UserEntity userEntity = new UserEntity("username");
		GameInfo gameInfo = new GameInfo(userEntity, 300, 300, 5);
		gameInfo.getSnakes().put("username", Snake.createSnake(1, 300));
		
		Dot dot = new Dot(-1, 0);
		assertTrue(gameInfo.hasCollison(dot));
		
		dot = new Dot(0,-1);
		assertTrue(gameInfo.hasCollison(dot));
		
		dot = new Dot(300,0);
		assertTrue(gameInfo.hasCollison(dot));
		
		dot = new Dot(0,300);
		assertTrue(gameInfo.hasCollison(dot));
		
		dot = new Dot(0 , 1 * Dot.RENDER_SIZE); // Snake tail
		assertTrue(gameInfo.hasCollison(dot));
	}
	
	@Test
	void testUpdateSnake() {
		boolean updateSnake;
		
		Dot apple = new Dot( 3 * Dot.RENDER_SIZE , 1* Dot.RENDER_SIZE);
		UserEntity userEntity = new UserEntity("username");
		GameInfo gameInfo = new GameInfo(userEntity, 300, 300, 5);
		gameInfo.setApple(apple);
		gameInfo.getSnakes().put("username", Snake.createSnake(1, 300));

		Dot head = new Dot( 3 * Dot.RENDER_SIZE , 1* Dot.RENDER_SIZE);
		updateSnake = gameInfo.updateSnake("username", Direction.DOWN);
		assertTrue(updateSnake);
		assertEquals(head, gameInfo.getSnakes().get("username").getHead());
		assertEquals(4, gameInfo.getSnakes().get("username").getList().size());
		
		assertFalse(apple.equals(gameInfo.getApple()));
		
		head = new Dot(3* Dot.RENDER_SIZE, 0);
		updateSnake = gameInfo.updateSnake("username", Direction.LEFT);
		assertTrue(updateSnake);
		assertEquals(head, gameInfo.getSnakes().get("username").getHead());
	
		head = new Dot(2* Dot.RENDER_SIZE, 0);
		updateSnake = gameInfo.updateSnake("username", Direction.LEFT);
		assertFalse(updateSnake);

	}

}
