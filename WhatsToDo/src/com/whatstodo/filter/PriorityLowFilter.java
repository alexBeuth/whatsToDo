package com.whatstodo.filter;

import com.whatstodo.models.Priority;
import com.whatstodo.models.Task;

public class PriorityLowFilter extends Filter {

	private static final long serialVersionUID = -3658863246909470452L;

	@Override
	protected boolean filter(Task task) {
		return task.getPriority().equals(Priority.LOW);
	}
	
	@Override
	protected String getFilterName() {
		return "Niedrige Priorität";
	}

}
