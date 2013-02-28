package com.whatstodo.net;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.whatstodo.models.List;

public class ListTypeAdapter implements JsonDeserializer<List>, JsonSerializer<List>{
	
	//TODO Add Comperator
	
	@Override
	public List deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		Gson gson = new Gson();
		
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement tasks = jsonObject.get("tasks");
		
		List list = gson.fromJson(tasks, List.class);
		
		
		if(!jsonObject.get("name").isJsonNull()) {
			list.setName(jsonObject.get("name").getAsString());
		}
		if(!jsonObject.get("id").isJsonNull()) {
			list.setId(jsonObject.get("id").getAsLong());
		}
		if(!jsonObject.get("isPersistent").isJsonNull()) {
			list.setPersistent(jsonObject.get("isPersistent").getAsBoolean());
		}
		
		return list;
	}

	@Override
	public JsonElement serialize(List src, Type typeOfSrc,
			JsonSerializationContext context) {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(
				Modifier.STATIC).create();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", src.getName());
		jsonObject.addProperty("id", src.getId());
		jsonObject.addProperty("isPersistent", src.isPersistent());
		JsonElement tasks = gson.toJsonTree(src);
		jsonObject.add("tasks", tasks);

		return jsonObject;
	}

}
