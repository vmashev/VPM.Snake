package vpm.ui;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import vpm.client.ServerConnection;
import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.Direction;
import vpm.helper.Dot;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.UserEntity;

public class Board extends JPanel implements Runnable, KeyListener {

	private Graphics2D graphics2D;
	private BufferedImage image;
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	private GameInfo gameInfo;
	private ClientSetup clientSetup = ClientSetup.createInstance();
	
	private Socket server;
	private ServerConnection serverConnection;
	private ObjectOutputStream objectOutput;
	
	public Board(int width, int height, int speed) throws UnknownHostException, IOException {
		
		this.gameInfo = new GameInfo(clientSetup.getUserName(), width , height , speed);
		
		this.server = new Socket(Constants.SERVER_IP , Constants.PORT);
		this.objectOutput = new ObjectOutputStream(server.getOutputStream());
		
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
				
	}
	
	public Board(GameInfo gameInfo) throws IOException {
		this.gameInfo = gameInfo;
		
		this.server = new Socket(Constants.SERVER_IP , Constants.PORT);
		this.objectOutput = new ObjectOutputStream(server.getOutputStream());
		
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
			gameInfo.setDirection(Direction.UP);
		}
		if( k == KeyEvent.VK_S) {
			gameInfo.setDirection(Direction.DOWN);
		}
		if( k == KeyEvent.VK_A) {
			gameInfo.setDirection(Direction.LEFT);
		}
		if( k == KeyEvent.VK_D) {
			gameInfo.setDirection(Direction.RIGHT);
		}
		if( k == KeyEvent.VK_ENTER) {
			gameInfo.setStatus(GameStatus.Run);
		}
		if( k == KeyEvent.VK_ESCAPE) {
			gameInfo.setStatus(GameStatus.SetPause);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();

		if( k == KeyEvent.VK_ENTER) {
			//start = false;
			//pause = false;
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
		
		try {
			init();
			
			while(running) {
				startTime = System.nanoTime();
				
				if((gameInfo.getDirection() != null) && (gameInfo.getStatus() != GameStatus.Pause)) {
					
					if(gameInfo.getStatus() == null) {
						gameInfo.setStatus(GameStatus.Run);
					}

					String message = JsonParser.parseFromGameInfo(gameInfo);
					Command sendCommand = new Command(11, message);
					objectOutput.writeObject(sendCommand);
					
					if(gameInfo.getStatus() == GameStatus.SetPause) {
						pauseGame();
					}
				}
				
				if(gameInfo.getStatus() == GameStatus.GameOver) {
					break;
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
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		} finally {
			try {
				objectOutput.close();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}	
		
		
	}

	private void init() throws IOException {
		serverConnection = new ServerConnection(server, this);
		new Thread(serverConnection).start();
		
		String message = JsonParser.parseFromGameInfo(gameInfo);
		Command sendCommand = new Command(10, message);
		objectOutput.writeObject(sendCommand);
		
		targetTime = 1000 / gameInfo.getSpeed();
		image= new BufferedImage(gameInfo.getWidth(), gameInfo.getHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics2D = image.createGraphics();
		running = true;
		targetTime = 1000 / gameInfo.getSpeed();
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
		for (Dot dot : gameInfo.getSnake().getList()) {
			dot.render(graphics2D);
		}
		
		graphics2D.setColor(Color.GREEN);
		gameInfo.getApple().render(graphics2D);
		
		if(gameInfo.getStatus() == GameStatus.GameOver) {
			graphics2D.drawString("GameOver! Max Score: " + gameInfo.getScore() , (gameInfo.getWidth() / 2) -30,  (gameInfo.getHeight() / 2));
		}
		
		if(gameInfo.getStatus() == GameStatus.Pause) {
			graphics2D.drawString("PAUSE", (gameInfo.getWidth() / 2) -10, (gameInfo.getHeight() / 2));
		}
		
		graphics2D.setColor(Color.WHITE);
		String playerInfo = "Score: " + gameInfo.getScore();
		if(gameInfo.getUserName() != null) {
			playerInfo = "User: " + gameInfo.getUserName() + " " + playerInfo;
		}
		graphics2D.drawString(playerInfo, 10, 10);
		
		if(gameInfo.getRowChange() == 0 && gameInfo.getColChange() == 0) {
			graphics2D.drawString("Ready!", (gameInfo.getWidth() / 2) -10, (gameInfo.getHeight() / 2));
		}
	}
	
	private void pauseGame() {
		PauseMenu pauseMenu = new PauseMenu(this);
		pauseMenu.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
		pauseMenu.setVisible(true);
	}
	
}
