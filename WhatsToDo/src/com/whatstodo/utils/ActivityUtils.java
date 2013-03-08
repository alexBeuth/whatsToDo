package com.whatstodo.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whatstodo.activities.FilteredTodoActivity;
import com.whatstodo.filter.Filter;

public class ActivityUtils {

	
	public static void startFilteredActivity(Activity activity, View view, Filter filter) {
		Intent intent = new Intent(view.getContext(), FilteredTodoActivity.class);
		Bundle bundle = new Bundle();

		bundle.putSerializable("filter", filter);

		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();
	}
}
