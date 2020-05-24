package vpm.command;

import vpm.model.GameInfo;
import vpm.server.ServerConectionManager;

public class CommandFactory {

	public static Command createCommand(int commandNumber, GameInfo gameInfo, ServerConectionManager gameManager) {
		
		switch (commandNumber) {
		case 1: // Find User by Nickname
			return new FindUserByNameCommand();
		case 2: // Insert User
			return new InserUserCommand();
		case 3: // Update User
			return new UpdateUserCommand();
		case 4: // Get saved games to list
			return new FindSavedGamesCommand();
		case 5: // Get selected game to resume
			return new ResumerSavedGameCommand(gameManager);	
		case 6: // Find Game History
			return new FindGameHistoryCommand();	
		case 10: // New Singleplayer game
			return new SingleplayerNewGameCommand(gameManager);
		case 11: // In game commands
			return new MoveCommand(gameInfo);
		case 13: // Create lobby
			return new CreateLobbyCommand(gameManager);
		case 14: // Join lobby (start new multiplayer game)
			return new JoinLobbyCommand(gameManager);
		case 15: // Get Lobbies 
			return new GetLobbyCommand(gameManager);
		}
		
		return null;
	}
}
