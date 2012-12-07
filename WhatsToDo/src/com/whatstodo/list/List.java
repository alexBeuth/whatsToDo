package com.whatstodo.list;

import com.whatstodo.task.Task;

public class List {
	
	private class Node {
		
		Node next;
		Task task;
	}
	
	private String name;
	
	public List(String name) {
		this.name = name;
	}
	
	public void addTask(String name) {
		//TODO
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}