package com.whatstodo.filter;

import com.whatstodo.models.Task;
import com.whatstodo.utils.Priority;

public class PriorityLowFilter extends Filter {

	@Override
	protected boolean filter(Task task) {
		return task.getPriority().equals(Priority.LOW);
	}

}
