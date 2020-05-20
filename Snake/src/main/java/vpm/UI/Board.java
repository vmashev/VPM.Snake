package vpm.UI;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import vpm.helper.Constants;
import vpm.helper.Direction;
import vpm.helper.Dot;
import vpm.helper.Snake;
import vpm.model.GameInProgress;

public class Board extends JPanel implements Runnable, KeyListener {

	private final int WIDTH ;
	private final int HEIGHT ;
	
	private Graphics2D graphics2D;
	private BufferedImage image;
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	private List<Snake> snakes = new ArrayList<Snake>();
	private int score;
	private int speed;
	private int numberOfPlayers;
	private boolean gameover;
	private Dot apple;
	
	private int newRow;
	private int newCol;
	
	private Direction direction = null;
	private boolean start;
	private boolean escape;
	
	public Board(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public Board(GameInProgress gameInProgress) {
		this.WIDTH = gameInProgress.getWidth();
		this.HEIGHT = gameInProgress.getHeight();
		this.apple = gameInProgress.getApple();
		this.direction = gameInProgress.getDirection();
		this.newCol = gameInProgress.getNewCol();
		this.newRow = gameInProgress.getNewRow();
		this.numberOfPlayers = gameInProgress.getNumberOfPlayers();
		this.score = gameInProgress.getScore();
		this.snakes = gameInProgress.getSnakes();
		this.speed = gameInProgress.getSpeed();
		
		setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	public void setSpeed(int speed) {
		this.speed = speed;
		this.targetTime = 1000 / speed;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this); 
		thread.start();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if( k == KeyEvent.VK_W) {
			direction = Direction.UP;
		}
		if( k == KeyEvent.VK_S) {
			direction = Direction.DOWN;
		}
		if( k == KeyEvent.VK_A) {
			direction = Direction.LEFT;
		}
		if( k == KeyEvent.VK_D) {
			direction = Direction.RIGHT;
		}
		if( k == KeyEvent.VK_ENTER) {
			start = true;
		}
		if( k == KeyEvent.VK_ESCAPE) {
			escape = true;
			pauseGame();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();

		if( k == KeyEvent.VK_ENTER) {
			start = false;
			escape = false;
		}
	}

	@Override
	public void run() {
		long startTime;
		long elapsed;
		long wait;
		
		if(running) {
			return;
		}
		
		init();
		while(running) {
			startTime = System.nanoTime();
			
			update(0);
			requestRender();
			
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(wait > 0) {
				try {
					Thread.sleep(wait);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void init() {
		image= new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		graphics2D = image.createGraphics();
		running = true;
		
		if(!escape) {
			setUpGame();
		}

	}
	
	private void setUpGame() {
		
		snakes.clear();
		direction = null;
		apple = generateApple();
		score = 0;
		newRow = 0;
		newCol = 0;
		gameover = false;
		
		for (int i = 1; i <= numberOfPlayers; i++) {
			Dot tempDot = null;
			Snake snake = new Snake();
			
			switch (i) {
			case 1: 
				
				for(int j = 0 ; j < 3 ; j++) {
					tempDot = new Dot(j * Constants.SIZE, 1 * Constants.SIZE);
					snake.getList().add(tempDot) ;
				}
				snake.setHead(tempDot);
				break;
			case 2: 
				
				for(int j = 0 ; j < 3 ; j++) {
					tempDot = new Dot((WIDTH - 2) * Constants.SIZE , 1 * Constants.SIZE);
					snake.getList().add(tempDot) ;
				}
				snake.setHead(tempDot);
				break;			
			}
			
			snakes.add(snake);

		}
		
		
	}
	
	private Dot generateApple() {
		Dot randomAppleDot = null;
		boolean collison = true;
		
		while(collison) {

			int randomRow = ThreadLocalRandom.current().nextInt(0, HEIGHT / Constants.SIZE );
			int randomCol = ThreadLocalRandom.current().nextInt(0, WIDTH / Constants.SIZE );
			randomAppleDot = new Dot(randomRow * Constants.SIZE, randomCol * Constants.SIZE);
			
			collison = false;
			for (Snake snake : snakes) {
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
	
	private boolean hasCollison(Dot nextDot) {
		
		if((nextDot.getRow() < 0) || (nextDot.getRow() == HEIGHT) ||
			(nextDot.getCol() < 0) || (nextDot.getCol() == WIDTH)) {
			return true;
		}
		
		for (Snake snake : snakes) {
			if (hasSnakeCollison(snake, nextDot)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void requestRender() {
		render(graphics2D);
		Graphics graphics = getGraphics();
		graphics.drawImage(image,0,0,null);
		graphics.dispose();
	}

	private void update(int player) {
		if(gameover) {
			if(start) {
				setUpGame();
			}
			return;
		} 
		
		if(escape) {
			return;
		}
		
		
		if(direction == Direction.UP && newRow == 0) {
			newRow = -Constants.SIZE;
			newCol = 0;
		}
		if(direction == Direction.DOWN && newRow == 0) {
			newRow = Constants.SIZE;
			newCol = 0;
		}	
		if(direction == Direction.LEFT && newCol == 0) {
			newRow = 0;
			newCol = -Constants.SIZE;
		}
		if(direction == Direction.RIGHT && newCol == 0) {
			newRow = 0;
			newCol = Constants.SIZE;
		}
		
		
		Snake snake = snakes.get(player);
		
		if(newRow != 0 || newCol != 0) {
			
			Dot nextDot = new Dot(snake.getHead().getRow() + newRow, snake.getHead().getCol() + newCol);
			
			if(hasCollison(nextDot)) {
				gameover = true;
				return;
			}
			
			
			if(nextDot.equals(apple)) {
				snake.grow(nextDot);
				score++;
				apple = generateApple();
				
			} else {
				snake.move(nextDot);
			}
		}
		
	}

	private void render(Graphics2D graphics) {
		graphics2D.clearRect(0, 0, WIDTH , HEIGHT );
		graphics2D.setColor(Color.BLUE);
		for (Snake snake : snakes) {
			for (Dot dot : snake.getList()) {
				dot.render(graphics2D);
			}
		}
		
		graphics2D.setColor(Color.RED);
		apple.render(graphics2D);
		
		if(gameover) {
			graphics2D.drawString("GameOver! Max Score: " + score , (WIDTH / 2) -30,  (HEIGHT / 2));
		}
		
		if(escape) {
			graphics2D.drawString("PAUSE", (WIDTH / 2) -10, (HEIGHT / 2));
		}
		
		graphics2D.setColor(Color.WHITE);
		graphics2D.drawString("Score: " + score + " Speed: " + speed, 10, 10);
		
		if(newRow == 0 && newCol == 0) {
			graphics2D.drawString("Ready!", 150, 200);
		}
	}
	
	private void pauseGame() {
		Pause pause = new Pause(this);
		pause.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
		pause.setVisible(true);
		start = true;
		escape = false;
		
	}
	
}
