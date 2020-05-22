package vpm.helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Hibernate;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import vpm.model.GameInfo;
import vpm.model.UserEntity;

public class JsonParser {

	public static UserEntity parseToUserEntity(String jsonString) {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		UserEntity userEntity = gson.fromJson(jsonString, UserEntity.class);

		return userEntity;
	}
	
	public static String parseFromUserEntity(UserEntity userEntity) {
		
		if(userEntity == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(userEntity);
		
		return jsonString;
	}
	
	public static GameInfo parseToGameInfo(String jsonString) {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		GameInfo gameInfo = gson.fromJson(jsonString, GameInfo.class);

		return gameInfo;
	}
	
	public static List<GameInfo> parseToGameInfoList(String jsonString) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<GameInfo>>(){}.getType();
	    
		List<GameInfo> games = gson.fromJson(jsonString, type);
		
		return games;
	}
	
	public static String parseFromGameInfo(GameInfo gameInfo) {
		
		if(gameInfo == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(gameInfo);
		
		return jsonString;
	}
	
	public static String parseFromGameInfoList(List<GameInfo> games) {
		
		if(games == null) {
			return null;
		}
		
		Hibernate.initialize(games);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(games);
		
		return jsonString;
	}
	
	public static SnakeMoveInfo parseToSnakeMoveInfo(String jsonString) {
		if(jsonString == null) {
			return null;
		}
		
		Gson gson = new Gson();
		SnakeMoveInfo snakeMove = gson.fromJson(jsonString, SnakeMoveInfo.class);

		return snakeMove;
	}
	
	public static String parseFromSnakeMoveInfo(SnakeMoveInfo snakeMove) {
		
		if(snakeMove == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(snakeMove);
		
		return jsonString;
	}

}
