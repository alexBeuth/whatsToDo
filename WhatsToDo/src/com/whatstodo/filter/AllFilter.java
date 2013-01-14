package com.whatstodo.filter;

import com.whatstodo.models.Task;

public class AllFilter extends Filter {

	private static final long serialVersionUID = 5003616170286675528L;

	@Override
	protected boolean filter(Task task) {
		return true;
	}

	@Override
	protected String getFilterName() {
		return "Alle";
	}

}
