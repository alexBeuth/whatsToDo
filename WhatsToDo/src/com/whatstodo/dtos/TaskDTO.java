package com.whatstodo.dtos;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;


public class TaskDTO implements Serializable{

	private static final long serialVersionUID = 4153668504200731290L;
	@SerializedName("_id")
	private long id;
	private String name;
	private String notice;
	private Date date;
	private Date reminder;
	private PriorityDTO priority;
	private boolean done;
	private String address;
	private boolean calendarCreated;
	private long listId;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;		
	}

	public PriorityDTO getPriority() {
		return priority;
	}

	public void setPriority(PriorityDTO priorityDTO) {
		this.priority = priorityDTO;
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

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCalendarCreated(boolean calendarCreated) {
		this.calendarCreated = calendarCreated;
	}
	
	public boolean isCalendarCreated() {
		return calendarCreated;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TaskDTO) {
			return id == ((TaskDTO) o).id;
		}
		return false;
	}
}