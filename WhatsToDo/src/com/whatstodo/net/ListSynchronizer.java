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

		@SuppressWarnings("unused")
		String receivedJsonString = HttpClient.SendHttpPost(
				"http://10.0.2.2:8080/rest/message/listtestput", jsonString);
		@SuppressWarnings("unused")
		String get = HttpClient
				.SendHttpGet("http://10.0.2.2:8080/rest/message/listtest");
		

		List synchronizedList = gson.fromJson(jsonString, List.class);

		return synchronizedList;
	}
	
}
