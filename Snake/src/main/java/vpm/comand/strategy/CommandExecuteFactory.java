package vpm.comand.strategy;

import vpm.model.GameInfo;

public class CommandExecuteFactory {

	public static CommandExecuteStrategy createCommand(int commandNumber, GameInfo gameInfo) {
		
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
			return new FindGameByDateTimeCommand();	
		case 6: // Find Game History
			return new FindGameHistoryCommand();	
		case 10: // New Singleplayer game
			return new SingleplayerNewGameCommand(gameInfo);
		case 11: // In game commands
			return new MoveCommand(gameInfo);
		case 12: // Resume paused game
			return new ResumeGameCommand(gameInfo);
		case 13: // New Multiplayer game
			return new CreateLobbyCommand(gameInfo);
		case 14: // Join lobby (start new multiplayer game)
			return new JoinLobbyCommand();
		case 15: // Create lobby
			return new GetLobbyCommand();
		case 16: // Create lobby
			return new WaitingForOpponentCommand();

		}
		
		return null;
	}
}
