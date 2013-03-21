package com.whatstodo.models;

import java.util.Random;

import com.google.gson.annotations.SerializedName;

public class HistoryEvent implements Comparable<HistoryEvent> {

	public enum Type {
		Task, Todo
	}

	public enum Action {
		Created, Read, Updated, Deleted
	}

	@SerializedName("_id")
	private long id;
	// The unique identifier for the object that was changed
	private long entityUid;
	private long parentEntityUid;
	private Type type;
	private Action action;
	private Long timeOfChange;
	private boolean isSynchronized;
	
	public HistoryEvent() {
		id = Math.abs(new Random().nextLong());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEntityUid() {
		return entityUid;
	}

	public void setEntityUid(long objectUid) {
		this.entityUid = objectUid;
	}

	public long getParentEntityUid() {
		return parentEntityUid;
	}

	public void setParentEntityUid(long parentEntityUid) {
		this.parentEntityUid = parentEntityUid;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Long getTimeOfChange() {
		return timeOfChange;
	}

	public void setTimeOfChange(Long timeOfChange) {
		this.timeOfChange = timeOfChange;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}

	@Override
	public int compareTo(HistoryEvent another) {
		if (!isSynchronized && another.isSynchronized) {
			return -1;
		}
		if (isSynchronized && !another.isSynchronized) {
			return 1;
		}

		if (timeOfChange == null && another.timeOfChange == null)
			return 0;

		if (timeOfChange != null) {
			if (another.timeOfChange != null)
				return timeOfChange.compareTo(another.timeOfChange);
			else
				return -1;
		}
		return 0;
	}
}
