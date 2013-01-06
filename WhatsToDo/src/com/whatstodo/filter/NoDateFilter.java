package com.whatstodo.filter;

import com.whatstodo.models.Task;

public class NoDateFilter extends Filter {

	@Override
	protected boolean filter(Task task) {
		return task.getDate() == null;
	}

}
