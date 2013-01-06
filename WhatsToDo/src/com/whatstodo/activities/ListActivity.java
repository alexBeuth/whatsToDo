package com.whatstodo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatstodo.R;
import com.whatstodo.filter.Filter;
import com.whatstodo.filter.PriorityHighFilter;
import com.whatstodo.filter.TodayFilter;
import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.models.Task;
import com.whatstodo.persistence.ChangeListener;

public class ListActivity extends Activity implements OnClickListener {

	private List list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list);

		Bundle bundle = getIntent().getExtras();

		if (bundle.getBoolean("isFilter")) {
			Filter filter = (Filter) bundle.getSerializable("filter");
			list = filter.getTask();
		} else {
			long listId = bundle.getLong("ListId");
			list = ListContainer.getInstance().getList(listId);
		}

		setTitle(list.getName());

		showTasks();

		Button createTask = (Button) findViewById(R.id.newTask);
		createTask.setOnClickListener(this);

		Button todayFilter = (Button) findViewById(R.id.backToLists);
		todayFilter.setOnClickListener(this);

		Button tomorrowFilter = (Button) findViewById(R.id.today);
		tomorrowFilter.setOnClickListener(this);

		Button priorityFilter = (Button) findViewById(R.id.priority);
		priorityFilter.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.newTask) {
			EditText editText = (EditText) findViewById(R.id.task);
			list.addTask(editText.getText().toString());
			ChangeListener.onListChange(list);
			showTasks();
		} else if (view.getId() == R.id.backToLists) {
			Intent intent = new Intent(view.getContext(), ListContainerActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.today) {
			startFilteredActivity(view, new TodayFilter());
		} else if (view.getId() == R.id.priority) {
			startFilteredActivity(view, new PriorityHighFilter());
		}
	}

	private void startFilteredActivity(View view, Filter filter) {
		Intent intent = new Intent(view.getContext(), ListActivity.class);
		Bundle bundle = new Bundle();

		bundle.putBoolean("isFilter", true);
		bundle.putSerializable("filter", filter);

		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void showTasks() {

		ListView listList = (ListView) findViewById(R.id.taskList);

		TaskAdapter adapter = new TaskAdapter(this, R.layout.taskitem, list);

		listList.setAdapter(adapter);
		listList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = ((TextView) (((RelativeLayout) view)
						.getChildAt(2))).getText().toString();

				for (Task task : list) {

					if (item == task.getName()) {
						Intent intent = new Intent(view.getContext(),
								TaskActivity.class);
						Bundle bundle = new Bundle();
						bundle.putLong("TaskId", task.getId());
						bundle.putLong("ListId", list.getId());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG)
						.show();
			}
		});

		registerForContextMenu(listList);

	}
}
