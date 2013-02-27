package com.whatstodo.models;

public enum Priority {
	LOW, NORMAL, HIGH;
	
	public static Priority getNextPriority(Priority priority) {
		switch (priority) {
		case HIGH:
			return Priority.LOW;
		case NORMAL:
			return Priority.HIGH;
		case LOW:
			return Priority.NORMAL;
		}
		return Priority.NORMAL;
	}
}
