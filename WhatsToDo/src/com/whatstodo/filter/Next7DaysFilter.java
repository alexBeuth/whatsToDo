package com.whatstodo.filter;

import java.util.Calendar;

import com.whatstodo.models.Task;

public class Next7DaysFilter extends Filter {

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

}
