package com.whatstodo.filter;

import java.util.Calendar;

import com.whatstodo.models.Task;

public class TomorrowFilter extends Filter {

	private static final long serialVersionUID = -6094397041253624962L;

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
	
	@Override
	protected String getFilterName() {
		return "Morgen";
	}

}
