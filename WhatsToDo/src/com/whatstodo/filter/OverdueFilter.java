package com.whatstodo.filter;

import java.util.Calendar;

import com.whatstodo.models.Task;

public class OverdueFilter extends Filter {

	private static final long serialVersionUID = 7082562771754590924L;

	@Override
	protected boolean filter(Task task) {
		
		if(task.getDate() == null) {
			return false;
		}
		
		Calendar today = Calendar.getInstance();
		
		Calendar date = Calendar.getInstance();
		date.setTime(task.getDate());

		return compareDate(date, today) < 0;
	}

	@Override
	protected String getFilterName() {
		return "Überfällig";
	}
}
