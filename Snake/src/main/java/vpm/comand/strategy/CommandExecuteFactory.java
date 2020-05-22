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
		case 10: // New Game
			return new NewGameCommand(gameInfo);
		case 11: // Move Game
			return new MoveCommand(gameInfo);
		}
		
		return null;
	}
}
