package vpm.helper;

import com.google.gson.Gson;

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
	
	public static String parseFromGameInfo(GameInfo gameInfo) {
		
		if(gameInfo == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(gameInfo);
		
		return jsonString;
	}
}
