package vpm.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import vpm.helper.ClientSetup;
import vpm.helper.Command;
import vpm.helper.Constants;
import vpm.helper.GameStatus;
import vpm.helper.JsonParser;
import vpm.model.GameInfo;
import vpm.model.Snake;
import vpm.model.UserEntity;

public class SingleplayerMenu extends JDialog implements ActionListener{

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	public SingleplayerMenu() throws UnknownHostException, IOException {
		socket = new Socket(Constants.SERVER_IP , Constants.PORT);
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 570, 230);
		setTitle("Singleplayer Menu");
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton joinBtn = new JButton("Resume");
		joinBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		joinBtn.setBounds(366, 165, 89, 23);
		joinBtn.addActionListener(this);
		contentPane.add(joinBtn);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnClose.setBounds(465, 165, 89, 23);
		btnClose.addActionListener(this);
		contentPane.add(btnClose);
		
		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		newGameBtn.setBounds(267, 165, 89, 23);
		newGameBtn.addActionListener(this);
		contentPane.add(newGameBtn);
		
		model = new DefaultTableModel( new Object[][] {}, new String[] { "Line No.", "Username", "DateTime", "Board Height", "Board Width", "Game Speed"} );
		
		model.setColumnIdentifiers(new String[] { "Line No.", "Username", "DateTime", "Board Height", "Board Width", "Game Speed"});
		table = new JTable(model);
		table.setBounds(10, 11, 544, 143);
		table.setAutoResizeMode(5);

		contentPane.add(table);

		getSavedGames();
		
	}
	
	private void getSavedGames() {
		ClientSetup clientSetup = ClientSetup.createInstance();
		
		try {
			UserEntity user = new UserEntity(clientSetup.getUserName());
			String message = JsonParser.parseFromUserEntity(user);
			
			Command sendCommand = new Command(4, message);
			outputStream.writeObject(sendCommand);
			
			Command receiveCommand = (Command)inputStream.readObject();
			List<GameInfo> games = JsonParser.parseToGameInfoList(receiveCommand.getMessage());

			for (int i = 0; i < games.size(); i++) {
				model.addRow(new Object[]{ i+1 , 
											games.get(i).getHostUsername(), 
											games.get(i).getDateTime(),
											games.get(i).getHeight(), 
											games.get(i).getWidth(), 
											games.get(i).getSpeed()});
			}		
			

		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		} 	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "New Game":
			Singleplayer singleplayer = new Singleplayer();
			singleplayer.setVisible(true);
			break;
		case "Resume":
			resumeGame();
			break;
		case "Close":
			dispose();
			break;		
		}
		
	}
	
	private void resumeGame() {
		if (table.getSelectedRowCount() > 0) {
            int selectedRow = table.getSelectedRow();
            
            LocalDateTime dateTime = LocalDateTime.parse(table.getValueAt(selectedRow, 2).toString());
            
            try {
    			GameInfo gameInfo = new GameInfo();
    			gameInfo.setDateTime(dateTime);
    			
    			String message = JsonParser.parseFromGameInfo(gameInfo);
    			Command sendCommand = new Command(5, message);
    			outputStream.writeObject(sendCommand);
    			
    			Command receiveCommand = (Command)inputStream.readObject();
    			gameInfo = JsonParser.parseToGameInfo(receiveCommand.getMessage());
    			gameInfo.setStatus(GameStatus.Ready);
    			gameInfo.getSnakes().put(gameInfo.getHostUsername(), gameInfo.getHostSnake());
    			
    			Board board = new Board(gameInfo);
        		
        		JFrame frame = new JFrame("Singleplayer");
        		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        		frame.setContentPane(board);
        		frame.setResizable(false);
        		frame.pack();
        		frame.setPreferredSize(new Dimension(gameInfo.getWidth(), gameInfo.getHeight()));
        		frame.setLocationRelativeTo(null);
        		frame.setVisible(true);	
    			
    		} catch (IOException | ClassNotFoundException e) {
    			JOptionPane.showMessageDialog(this, e.getMessage());
    			
    		}
        
        }
	}

}
