package com.whatstodo.filter;

import com.whatstodo.models.Task;

public class NoDateFilter extends Filter {

	private static final long serialVersionUID = 7206958589452625246L;

	@Override
	protected boolean filter(Task task) {
		return task.getDate() == null;
	}
	
	@Override
	protected String getFilterName() {
		return "Kein Datum";
	}

}
