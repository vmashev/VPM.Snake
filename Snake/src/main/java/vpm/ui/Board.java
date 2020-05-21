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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JDialog;
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
import vpm.model.Dot;
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
	private Thread serverConnectionThread;
	
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
		if( k == KeyEvent.VK_ESCAPE) {
			gameInfo.setStatus(GameStatus.SetPause);
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
				
				if((gameInfo.getDirection() != null) && (gameInfo.getStatus() != GameStatus.Pause)) {
					
					String message = JsonParser.parseFromGameInfo(gameInfo);
					Command sendCommand = new Command(11, message);
					objectOutput.writeObject(sendCommand);
					
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
				
				switch (gameInfo.getStatus()) {
				case SetPause:
					pauseGame();
					break;

				case GameOver:
					JOptionPane.showMessageDialog(this, "GameOver! Max Score: " + gameInfo.getScore());
					win.dispose();
					thread.interrupt();
					break;
					
				case Save:
					win.dispose();
					thread.interrupt();
					break;
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
		image= new BufferedImage(gameInfo.getWidth(), gameInfo.getHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics2D = image.createGraphics();
		running = true;
		targetTime = 1000 / gameInfo.getSpeed();

		if(gameInfo.getId() == null) {
			String message = JsonParser.parseFromGameInfo(gameInfo);
			Command sendCommand = new Command(10, message);
			objectOutput.writeObject(sendCommand);
		} else {
			requestRender();
		}
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
	
	private void pauseGame() throws IOException {
		PauseMenu pauseMenu = new PauseMenu(this);
		pauseMenu.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
		pauseMenu.setVisible(true);
	}
	
}
