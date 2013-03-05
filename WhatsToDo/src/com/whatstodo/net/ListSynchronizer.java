package com.whatstodo.net;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.whatstodo.dtos.ListDTO;
import com.whatstodo.models.List;

public class ListSynchronizer {

	// TODO Implement preferences
	private static final String BaseURL = "http://10.0.2.2:8080/rest/list";
	private static final String user = "Testuser";

	private Gson gson = new GsonBuilder()
			.excludeFieldsWithModifiers(Modifier.STATIC).serializeNulls()
			.create();

	public List getList(long listId) {

		try {

			String URL = BaseURL + "/" + user + "/" + listId;

			JsonElement receivedJson = HttpClient.sendHttpGet(URL);

			List synchronizedList = null;
			
			if (!receivedJson.isJsonNull()) {
				ListDTO dto = gson.fromJson(receivedJson, ListDTO.class);
				List.fromDTO(dto);
			}

			return synchronizedList;

		} catch (SynchronizationException e) {
			// TODO
			throw new RuntimeException(e);
		}

	}

	public void saveList(List list) {

		String jsonString = gson.toJson(list);

		try {

			String URL = BaseURL + "/" + user + "/" + list.getId();

			HttpClient.sendHttpPut(URL, jsonString);

		} catch (SynchronizationException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	public void deleteList(List list) {

		try {

			String URL = BaseURL + "/" + user + "/" + list.getId();

			HttpClient.sendHttpDelete(URL);

		} catch (SynchronizationException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	public List synchronizeList(List list) {

		String jsonString = gson.toJson(list);

		try {

			String URL = BaseURL + "/" + user + "/" + list.getId();

			JsonElement receivedJson = HttpClient.sendHttpPost(URL, jsonString);

			List synchronizedList = null;
			
			
			if (!receivedJson.isJsonNull()) {
				ListDTO dto = gson.fromJson(receivedJson, ListDTO.class);
				List.fromDTO(dto);
			}

			return synchronizedList;

		} catch (SynchronizationException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

}
