package com.whatstodo.filter;

import com.whatstodo.models.Priority;
import com.whatstodo.models.Task;

public class PriorityNormalFilter extends Filter {

	private static final long serialVersionUID = 3174403705528008099L;

	@Override
	protected boolean filter(Task task) {
		return task.getPriority().equals(Priority.NORMAL);
	}

	@Override
	protected String getFilterName() {
		return "Normale Priorität";
	}
}
