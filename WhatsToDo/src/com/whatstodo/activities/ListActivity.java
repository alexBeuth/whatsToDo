package com.whatstodo.activities;

import java.util.ArrayList;

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
import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.models.Task;

public class ListActivity extends Activity implements OnClickListener {

	private ListContainer container;
	private List list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list);

		Bundle bundle = getIntent().getExtras();
		long listId = bundle.getLong("ListId");

		container = ListContainer.getInstance();
		list = container.getList(listId);

		setTitle(list.getName());
		
		showTasks();

		Button createTask = (Button) findViewById(R.id.newTask);
		createTask.setOnClickListener(this);
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
			showTasks();
		} else {
			// TODO Button nicht gefunden			
		}
	}

	private void showTasks() {

		ListView listList = (ListView) findViewById(R.id.taskList);

		// TODO We need to transform our list to java list this is really bad!
		// -> our list needs to implement java list..
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(Task task : list) {
			tasks.add(task);


		TaskAdapter adapter = new TaskAdapter(this, R.layout.taskitem, tasks);

		listList.setAdapter(adapter);
		listList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = ((TextView) (((RelativeLayout) view).getChildAt(2)))
						.getText().toString();

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
}
