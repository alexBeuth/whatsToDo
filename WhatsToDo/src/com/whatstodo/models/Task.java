package com.whatstodo.models;

import java.io.Serializable;
import java.util.Date;

import com.whatstodo.dtos.TaskDTO;

public class Task implements Serializable, Comparable<Task> {

	private static final long serialVersionUID = -8335115914160527642L;
	private long id;
	private String name;
	private String notice;
	private Date date;
	private Date reminder;
	private Priority priority;
	private boolean done;
	private String address;
	private boolean calendarCreated;
	private long listId;
	
	public Task(){
		
		priority = Priority.NORMAL;
		done = false;
	}

	public Task(String name) {
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
	
	public void setId(long id) {
		this.id = id;
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

	@Override
	public int compareTo(Task another) {

		if (!isDone() && another.isDone()) {
			return -1;
		}
		if (isDone() && !another.isDone()) {
			return 1;
		}

		if (date == null && another.date == null)
			return 0;

		if (date != null) {
			if (another.date != null)
				return date.compareTo(another.date);
			else
				return -1;
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Task) {
			return id == ((Task) o).id;
		}
		return false;
	}

	public void setCalendarCreated(boolean calendarCreated) {
		this.calendarCreated = calendarCreated;
	}

	public boolean isCalendarCreated() {
		return calendarCreated;
	}
	
	public static TaskDTO toDTO(Task task){
		
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(task.id);
		taskDTO.setName(task.name);
		taskDTO.setNotice(task.notice);
		taskDTO.setDate(task.date);
		taskDTO.setReminder(task.reminder);
		taskDTO.setPriority(Priority.toDTO(task.priority));
		taskDTO.setDone(task.done);
		taskDTO.setAddress(task.address);
		taskDTO.setCalendarCreated(task.calendarCreated);
		taskDTO.setListId(task.listId);
		return taskDTO;	
	}
	
	public static Task fromDTO(TaskDTO taskDTO){
		Task task = new Task();
		task.id = taskDTO.getId();
		task.name = taskDTO.getName();
		task.notice = taskDTO.getNotice();
		task.date = taskDTO.getDate();
		task.reminder = taskDTO.getReminder();
		task.priority = Priority.fromDTO(taskDTO.getPriority());
		task.done = taskDTO.isDone();
		task.address = taskDTO.getAddress();
		task.calendarCreated = taskDTO.isCalendarCreated();
		task.listId = taskDTO.getListId();
		return task;
	}
}