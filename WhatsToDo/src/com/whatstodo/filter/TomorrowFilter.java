package com.whatstodo.filter;

import java.util.Calendar;

import com.whatstodo.models.Task;

public class TomorrowFilter extends Filter {

	@Override
	protected boolean filter(Task task) {
		
		if(task.getDate() == null) {
			return false;
		}
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, 1);
		
		Calendar date = Calendar.getInstance();
		date.setTime(task.getDate());

		return compareDate(date, tomorrow) == 0;
	}

}
