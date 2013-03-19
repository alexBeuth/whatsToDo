package com.whatstodo.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.whatstodo.R;
import com.whatstodo.filter.Filter;
import com.whatstodo.filter.PriorityHighFilter;
import com.whatstodo.filter.TomorrowFilter;
import com.whatstodo.models.List;

public class FilteredTodoActivity extends ListActivity {
	
	private Filter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final EditText createTask = (EditText) findViewById(R.id.createTask);
		
		createTask.setText("Filteransicht");
		createTask.setKeyListener(null);
		
		initTextView(R.id.filterButton5, "Listen");
		initTextView(R.id.filterButton6, "Morgen");
		initTextView(R.id.filterButton7, "Priorität");
		initTextView(R.id.filterButton8, "Mehr");

		if (filter instanceof TomorrowFilter) {
			initTextView(R.id.filterButton6, "Heute");
			initTextView(R.id.filterButton7, "Priorität");
		} else if (filter instanceof PriorityHighFilter) {
			initTextView(R.id.filterButton6, "Heute");
			initTextView(R.id.filterButton7, "Morgen");
		}
	}

	@Override
	protected List getTodoData() {
		Bundle bundle = getIntent().getExtras();
		filter = (Filter) bundle.getSerializable("filter");
		return filter.getTask();
	}

	@Override
	protected void startSynchronisation() {
		Toast toast = Toast.makeText(getApplicationContext(),
				"Cannot sync filtered list.", Toast.LENGTH_SHORT);
		toast.show();
		
	}
	
	

}
