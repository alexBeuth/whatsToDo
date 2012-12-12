package com.whatstodo.task;

import java.io.Serializable;

import com.whatstodo.ListContainer;

public class Task implements Serializable {

	private long id;
	private String name;
	
	public Task(String name) {
		this.name = name;
		this.id = ListContainer.getNextTaskId();
	}

	public String getName() {
		return this.name;
	}
}
