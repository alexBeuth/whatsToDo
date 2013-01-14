package com.whatstodo.filter;

import com.whatstodo.models.Task;
import com.whatstodo.utils.Priority;

public class PriorityHighFilter extends Filter {

	private static final long serialVersionUID = -2027545264875207689L;

	@Override
	protected boolean filter(Task task) {
		return task.getPriority().equals(Priority.HIGH);
	}
	
	@Override
	protected String getFilterName() {
		return "Hohe Priorität";
	}

}
