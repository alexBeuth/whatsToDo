package com.whatstodo.filter;

import com.whatstodo.models.Task;

public class AllFilter extends Filter {

	@Override
	protected boolean filter(Task task) {
		return true;
	}

	@Override
	protected String getFilterName() {
		return "Alle";
	}

}
