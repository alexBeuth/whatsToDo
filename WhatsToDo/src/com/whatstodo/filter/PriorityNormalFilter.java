package com.whatstodo.filter;

import com.whatstodo.models.Task;
import com.whatstodo.utils.Priority;

public class PriorityNormalFilter extends Filter {

	@Override
	protected boolean filter(Task task) {
		return task.getPriority().equals(Priority.NORMAL);
	}

	@Override
	protected String getFilterName() {
		return "Normale Priorität";
	}
}
