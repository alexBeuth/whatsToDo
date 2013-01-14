package com.whatstodo.utils;

import java.util.NoSuchElementException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whatstodo.activities.ListActivity;
import com.whatstodo.filter.Filter;
import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.models.Task;

public class ActivityUtils {

	
	public static void startFilteredActivity(Activity activity, View view, Filter filter) {
		Intent intent = new Intent(view.getContext(), ListActivity.class);
		Bundle bundle = new Bundle();

		bundle.putBoolean("isFilter", true);
		bundle.putSerializable("filter", filter);

		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();
	}
	
	public static Task getTaskForId(Long taskId) {

		for (List list : ListContainer.getInstance().getLists()) {
			Task task = null;
			try {
				task = list.getTask(taskId);
			} catch (NoSuchElementException e) {
				continue;
			}
			return task;
		}
		throw new NoSuchElementException("Cannot find task with ID: " + taskId);
	}
	
}
