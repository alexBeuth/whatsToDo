package com.whatstodo.net.request;

import java.util.List;

import com.whatstodo.dtos.ListDTO;
import com.whatstodo.models.HistoryEvent;

public class SyncTodoRequest {
	
	private ListDTO todo;
	private List<HistoryEvent> history;
	
	public ListDTO getTodo() {
		return todo;
	}
	public void setTodo(ListDTO todo) {
		this.todo = todo;
	}
	public List<HistoryEvent> getHistory() {
		return history;
	}
	public void setHistory(List<HistoryEvent> history) {
		this.history = history;
	}

}
