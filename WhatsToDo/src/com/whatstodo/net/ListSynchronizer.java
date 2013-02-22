package com.whatstodo.net;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whatstodo.models.List;

public class ListSynchronizer {

	public List synchronizeList(List list) {

		Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.STATIC).serializeNulls()
				.registerTypeAdapter(List.class, new ListTypeAdapter())
				.create();

		String jsonString = gson.toJson(list);

//		String receivedJsonString = HttpClient.SendHttpPost("localhost",
//				jsonString);

		List synchronizedList = gson.fromJson(jsonString, List.class);

		return synchronizedList;
	}
}
