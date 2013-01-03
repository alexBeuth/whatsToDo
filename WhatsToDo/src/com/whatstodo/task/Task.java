package com.whatstodo.task;

import java.io.Serializable;
import java.util.Date;

import com.whatstodo.ListContainer;
import com.whatstodo.utility.Priority;

public class Task implements Serializable, Comparable<Task> {

	private static final long serialVersionUID = 0;
	private long id;
	private String name;
	private Priority priority;
	private Date date;
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(Task another) {
		
		if(date == null && another.date == null)
			return 0;
		
		if(date != null) {
			if(another.date != null)
				return date.compareTo(another.date);
			else
				return 1;
		}
		return -1;
	}

}
