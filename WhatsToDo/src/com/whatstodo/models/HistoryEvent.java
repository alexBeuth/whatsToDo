package com.whatstodo.models;

import java.util.Date;

public class HistoryEvent implements Comparable<HistoryEvent>{

	public enum Type {
		Task, Todo
	}

	public enum Action {
		Created, Read, Updated, Deleted
	}

	// The unique identifier for the object that was changed
	private long entityUid;
	private Type type;
	private Action action;
	private Date timeOfChange;
	private boolean isSynchronized;

	public long getEntityUid() {
		return entityUid;
	}

	public void setEntityUid(long objectUid) {
		this.entityUid = objectUid;
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

	public Date getTimeOfChange() {
		return timeOfChange;
	}

	public void setTimeOfChange(Date timeOfChange) {
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
