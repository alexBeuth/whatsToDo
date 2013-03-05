package com.whatstodo.models;

import com.whatstodo.dtos.PriorityDTO;

public enum Priority {
	LOW(0), NORMAL(1), HIGH(2);
	
	private int id;
	
	private Priority(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

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
	
	public static Priority fromId(int id) {
		switch (id) {
		case 0:
			return LOW;
		case 1:
			return NORMAL;
		case 2:
			return HIGH;
		default:
			throw new IllegalArgumentException("No Priority enum for ID: " + id);
		}
	}
	
	public static PriorityDTO toDTO(Priority priority) {
		PriorityDTO dto = new PriorityDTO();
		dto.setId(priority.id);
		return dto;
	}
	
	public static Priority fromDTO(PriorityDTO priorityDTO) {
		return Priority.fromId(priorityDTO.getId());
	}
}
