package vpm.ui;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import vpm.client.ServerConnection;
import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.Direction;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.helper.SnakeMoveInfo;
import vpm.model.Dot;
import vpm.model.GameInfo;
import vpm.model.Snake;

public class Board extends JPanel implements Runnable, KeyListener {

	private Graphics2D graphics2D;
	private BufferedImage image;
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	private GameInfo gameInfo;
	private SnakeMoveInfo snakeMove;

	private ClientSetup clientSetup = ClientSetup.createInstance();
	
	private Socket server;
	private ServerConnection serverConnection;
	private ObjectOutputStream objectOutput;
	private Thread serverConnectionThread;
	
	public Board(int width, int height, int speed) throws UnknownHostException, IOException {
		
		this.gameInfo = new GameInfo(clientSetup.getUserName(), width , height , speed);
		this.server = new Socket(Constants.SERVER_IP , Constants.PORT);
		this.objectOutput = new ObjectOutputStream(server.getOutputStream());
		this.snakeMove = new SnakeMoveInfo(clientSetup.getUserName());
		
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
				
	}
	
	public Board(GameInfo gameInfo) throws IOException {
		this.gameInfo = gameInfo;
		this.server = new Socket(Constants.SERVER_IP , Constants.PORT);
		this.objectOutput = new ObjectOutputStream(server.getOutputStream());
		this.snakeMove = new SnakeMoveInfo(clientSetup.getUserName());
		this.snakeMove.setStatus(GameStatus.Ready);
		
		setPreferredSize(new Dimension(gameInfo.getWidth(), gameInfo.getHeight()));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		this.snakeMove.setStatus(gameInfo.getStatus());
	}

	public SnakeMoveInfo getSnakeMove() {
		return snakeMove;
	}

	public void setSnakeMove(SnakeMoveInfo snakeMove) {
		this.snakeMove = snakeMove;
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
			snakeMove.setDirection(Direction.UP);
		}
		if( k == KeyEvent.VK_S) {
			snakeMove.setDirection(Direction.DOWN);
		}
		if( k == KeyEvent.VK_A) {
			snakeMove.setDirection(Direction.LEFT);
		}
		if( k == KeyEvent.VK_D) {
			snakeMove.setDirection(Direction.RIGHT);
		}
		if( k == KeyEvent.VK_ESCAPE) {
			snakeMove.setStatus(GameStatus.SetPause);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void run() {
		long startTime;
		long elapsed;
		long wait;
		Window win = SwingUtilities.getWindowAncestor(this);
		
		if(running) {
			return;
		}
		
		try {
			init();
			
			while(running) {
				startTime = System.nanoTime();
				
				if((snakeMove.getDirection() != null) && (snakeMove.getStatus() != GameStatus.Pause)) {
					
					if(snakeMove.getStatus() == GameStatus.Ready) {
						snakeMove.setStatus(GameStatus.Run);
					}
					
					String message = JsonParser.parseFromSnakeMoveInfo(snakeMove);
					Command sendCommand = new Command(11, message);
					objectOutput.writeObject(sendCommand);
					
				}
				
				if(snakeMove.getStatus() != null) {
					switch (snakeMove.getStatus()) {
					case GameOver:
						JOptionPane.showMessageDialog(this, "GameOver! Max Score: ???");// + snake.getScore());
						win.dispose();
						thread.interrupt();
						break;
						
					case Save:
						win.dispose();
						thread.interrupt();
						break;
					case SetPause:
						pauseGame();
						break;						
					}
				}
				
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
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(	objectOutput != null) {
					objectOutput.close();
				}
				if(server != null) {
					server.close();
				}	
				thread.join();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}

	private void init() throws IOException {
		serverConnection = new ServerConnection(server, this);
		serverConnectionThread = new Thread(serverConnection);
		serverConnectionThread.start();
		
		targetTime = 1000 / gameInfo.getSpeed();
		image = new BufferedImage(gameInfo.getWidth(), gameInfo.getHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics2D = image.createGraphics();
		running = true;
		targetTime = 1000 / gameInfo.getSpeed();

		String message = JsonParser.parseFromGameInfo(gameInfo);
		Command sendCommand;
		if(gameInfo.getId() == null) {
			//New game
			sendCommand = new Command(10, message);
		} else {
			//Resume game
			sendCommand = new Command(12, message);
			requestRender();
		}
		objectOutput.writeObject(sendCommand);
		
	}
	
	public void requestRender() {
		render(graphics2D);
		Graphics graphics = getGraphics();
		graphics.drawImage(image,0,0,null);
		graphics.dispose();
	}
	
	private void render(Graphics2D graphics) {
		graphics2D.clearRect(0, 0, gameInfo.getWidth() , gameInfo.getHeight() );
		graphics2D.setColor(Color.BLUE);
		
		for (Snake snake : gameInfo.getSnakes().values()) {
			for (Dot dot : snake.getList()) {
				dot.render(graphics2D);
			}
		}

		graphics2D.setColor(Color.GREEN);
		gameInfo.getApple().render(graphics2D);
		
		graphics2D.setColor(Color.WHITE);
		
		int i = 0;
		for (Map.Entry<String,Snake> snakeEntry : gameInfo.getSnakes().entrySet()) {
			
			i++;
			
			String playerInfo = snakeEntry.getKey() + " score: " + snakeEntry.getValue().getScore();
			
			switch (i) {
			case 1:
				graphics2D.drawString(playerInfo, 10, 10);
				break;
			case 2:
				graphics2D.drawString(playerInfo, 10 , getHeight() - 10);
				break;
			default:
				break;
			}			
		}
		
		if(gameInfo.getStatus() != null) {
			switch (gameInfo.getStatus()) {
			case Pause:
				graphics2D.drawString("PAUSE", (gameInfo.getWidth() / 2) -10, (gameInfo.getHeight() / 2));
				break;
			case Ready:
				graphics2D.drawString("Ready!", (gameInfo.getWidth() / 2) -10, (gameInfo.getHeight() / 2));
				break;
			case GameOver:
				int score = gameInfo.getSnakes().get(clientSetup.getUserName()).getScore();
				graphics2D.drawString("GameOver! Score: " + score, (gameInfo.getWidth() / 2) -10, (gameInfo.getHeight() / 2));
				break;
			}
		}
		
	}
	
	private void pauseGame() throws IOException {
		PauseMenu pauseMenu = new PauseMenu(this);
		pauseMenu.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
		pauseMenu.setVisible(true);
	}
	
}
