package com.whatstodo.filter;

import java.util.Calendar;

import com.whatstodo.models.Task;

public class Next7DaysFilter extends Filter {

	private static final long serialVersionUID = 3438268894808041791L;

	@Override
	protected boolean filter(Task task) {
		
		if(task.getDate() == null) {
			return false;
		}

		Calendar today = Calendar.getInstance();

		Calendar future = Calendar.getInstance();
		future.add(Calendar.DATE, 7);

		Calendar date = Calendar.getInstance();
		date.setTime(task.getDate());

		return (compareDate(date, future) <= 0)
				&& (compareDate(date, today) >= 0);
	}
	
	@Override
	protected String getFilterName() {
		return "Nächste 7 Tage";
	}

}
