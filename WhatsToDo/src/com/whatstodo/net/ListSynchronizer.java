package com.whatstodo.net;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.whatstodo.dtos.ListDTO;
import com.whatstodo.manager.HistoryEventManager;
import com.whatstodo.manager.TodoListManager;
import com.whatstodo.models.HistoryEvent;
import com.whatstodo.models.List;
import com.whatstodo.net.request.SyncAllRequest;
import com.whatstodo.net.request.SyncTodoRequest;

public class ListSynchronizer {

	// TODO Implement preferences
	private static final String BaseURL = "http://10.0.2.2:8080/rest/list";
	private static final String user = "Testuser";

	private Gson gson = new GsonBuilder()
			.excludeFieldsWithModifiers(Modifier.STATIC).serializeNulls()
			.create();

	public boolean serverIsAvailble() {

		String URL = BaseURL + "/" + "serverinfo";

		try {
			HttpClient.sendHttpGet(URL);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public List getList(long listId) throws SynchronizationException {

		try {

			String URL = BaseURL + "/" + user + "/" + listId;

			JsonElement receivedJson = HttpClient.sendHttpGet(URL);

			List list = null;

			if (!receivedJson.isJsonNull()) {
				ListDTO dto = gson.fromJson(receivedJson, ListDTO.class);
				list = List.fromDTO(dto);
			}

			return list;

		} catch (Exception e) {
			throw new SynchronizationException(e);
		}

	}

	public void saveList(List list) throws SynchronizationException {

		try {
			String jsonString = gson.toJson(list);

			String URL = BaseURL + "/" + user + "/" + list.getId();

			HttpClient.sendHttpPut(URL, jsonString);

		} catch (SynchronizationException e) {
			throw new SynchronizationException(e);
		}
	}

	public void deleteList(List list) throws SynchronizationException {

		try {

			String URL = BaseURL + "/" + user + "/" + list.getId();

			HttpClient.sendHttpDelete(URL);

		} catch (Exception e) {
			throw new SynchronizationException(e);
		}
	}

	public List synchronizeList(List list) throws SynchronizationException {

		try {
			java.util.List<HistoryEvent> history = HistoryEventManager
					.getInstance().find(null, null, null, list.getId(), false);
	
			SyncTodoRequest todoRequest = new SyncTodoRequest();
			todoRequest.setTodo(List.toDTO(list));
			todoRequest.setHistory(history);
	
			String json = gson.toJson(todoRequest);
	
			String URL = BaseURL + "/" + user + "/" + list.getId();
	
			JsonElement receivedJson = HttpClient.sendHttpPost(URL, json);
	
			List synchronizedList = null;
	
			if (!receivedJson.isJsonNull()) {
				ListDTO dto = gson.fromJson(receivedJson, ListDTO.class);
				synchronizedList = List.fromDTO(dto);
			}
			
			updateHistory(history);
	
			return synchronizedList;
		} catch (Exception e) {
			throw new SynchronizationException(e);
		}

	}

	public java.util.List<List> synchronizeAll() throws SynchronizationException {

		try {
			java.util.List<HistoryEvent> history = HistoryEventManager
					.getInstance().find(null, null, null, null, false);
	
			java.util.List<List> all = TodoListManager.getInstance().findAll();
			java.util.List<ListDTO> todos = new ArrayList<ListDTO>();
	
			for (List list : all) {
				todos.add(List.toDTO(list));
			}
	
			SyncAllRequest request = new SyncAllRequest();
			request.setHistory(history);
			request.setTodos(todos);
	
			String json = gson.toJson(request);

			String URL = BaseURL + "/" + user;

			JsonElement receivedJson = HttpClient.sendHttpPost(URL, json);

			java.util.List<List> synchronizedTodos = new ArrayList<List>();

			if (!receivedJson.isJsonNull()) {

				Type typeOfT = new TypeToken<java.util.List<ListDTO>>() {
				}.getType();

				java.util.List<ListDTO> dtos = gson.fromJson(receivedJson,
						typeOfT);
				
				for(ListDTO dto : dtos) {
					synchronizedTodos.add(List.fromDTO(dto));
				}
			}
			
			updateHistory(history);

			return synchronizedTodos;

		} catch (Exception e) {
			throw new SynchronizationException(e);
		}
	}
	
	private void updateHistory(java.util.List<HistoryEvent> history) {
		for (HistoryEvent event : history) {
			event.setSynchronized(true);
			HistoryEventManager.getInstance().save(event);
		}
	}

}
