package com.whatstodo.task;

import java.io.Serializable;

import com.whatstodo.ListContainer;
import com.whatstodo.utility.Priority;

public class Task implements Serializable {

	private static final long serialVersionUID = 0;
	private long id;
	private String name;
	private Priority priority;
	
	public Task(String name) {
		this.setName(name);
		this.id = ListContainer.getNextTaskId();
		priority = Priority.NORMAL;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

}
