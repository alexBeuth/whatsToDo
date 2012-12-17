package com.whatstodo.task;

import java.io.Serializable;
import java.util.Date;

import com.whatstodo.ListContainer;
import com.whatstodo.utility.Priority;

public class Task implements Serializable {

	private long id;
	private String name;
	private Priority priority;
	private Date date;
	private String notice;
	
	public Task(String name) {
		this.setName(name);
		this.id = ListContainer.getNextTaskId();
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

	public CharSequence getNotice() {
		// TODO Auto-generated method stub
		return notice;
	}
	

}
