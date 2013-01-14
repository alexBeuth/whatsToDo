package com.whatstodo.filter;

import java.io.Serializable;
import java.util.Calendar;

import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.models.Task;

public abstract class Filter implements Serializable {

	private static final long serialVersionUID = 2645671922210489484L;

	public List getTask() {
		ListContainer container = ListContainer.getInstance();

		List result = new List(getFilterName(), true);

		for (List list : container.getLists()) {
			for (Task task : list) {
				if (filter(task)) {
					result.add(task);
				}
			}
		}

		return result;
	}

	protected abstract String getFilterName();

	protected abstract boolean filter(Task task);

	public static int compareDate(Calendar cal1, Calendar cal2) {

		if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) {
			return 1;
		}
		if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) {
			return -1;
		}
		if (cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH)) {
			return 1;
		}
		if (cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH)) {
			return -1;
		}
		if (cal1.get(Calendar.DATE) > cal2.get(Calendar.DATE)) {
			return 1;
		}
		if (cal1.get(Calendar.DATE) < cal2.get(Calendar.DATE)) {
			return -1;
		}
		return 0;

	}

}
