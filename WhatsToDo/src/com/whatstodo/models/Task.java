package com.whatstodo.models;

import java.io.Serializable;
import java.util.Date;

import com.whatstodo.utils.Priority;

public class Task implements Serializable, Comparable<Task> {

	private static final long serialVersionUID = 0;
	private long id;
	private String name;
	private String notice;
	private Date date;
	private Date reminder;
	private Priority priority;
	private boolean done;
	
	public Task(String name) {
		this.id = ListContainer.getNextTaskId();
		this.name = name;
		priority = Priority.NORMAL;
		done = false;
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

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNotice() {
		return notice;
	}
	
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
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
