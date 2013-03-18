package com.whatstodo.net.request;

import java.util.List;

import com.whatstodo.dtos.ListDTO;
import com.whatstodo.models.HistoryEvent;

public class SyncAllRequest {
	
	private List<ListDTO> todos;
	private List<HistoryEvent> history;
	
	public List<ListDTO> getTodos() {
		return todos;
	}
	public void setTodos(List<ListDTO> todos) {
		this.todos = todos;
	}
	public List<HistoryEvent> getHistory() {
		return history;
	}
	public void setHistory(List<HistoryEvent> history) {
		this.history = history;
	}

}
