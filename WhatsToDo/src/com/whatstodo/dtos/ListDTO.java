package com.whatstodo.dtos;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ListDTO implements Serializable {

	private static final long serialVersionUID = 2718636838558619165L;
	private int size;
	private TaskDTO[] orderedTasks;
	
	@SerializedName("_id")
	private long id;
	
	private String name;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public TaskDTO[] getTasks() {
		return orderedTasks;
	}

	public void setTasks(TaskDTO[] orderedTasks) {
		this.orderedTasks = orderedTasks;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ListDTO) {
			return id == ((ListDTO) o).id;
		}
		return false;
	}
}
