package com.whatstodo.filter;

import java.util.Calendar;

import com.whatstodo.models.Task;

public class TodayFilter extends Filter {

	private static final long serialVersionUID = -7531658110133328556L;

	@Override
	protected boolean filter(Task task) {
		
		if(task.getDate() == null) {
			return false;
		}

		Calendar today = Calendar.getInstance();
		
		Calendar date = Calendar.getInstance();
		date.setTime(task.getDate());

		return compareDate(date, today) == 0;
	}
	
	@Override
	protected String getFilterName() {
		return "Heute";
	}
	
}
