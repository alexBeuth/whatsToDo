package com.whatstodo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.whatstodo.utils.ActivityUtils;

public class ListActivity extends Activity implements OnClickListener, TaskAdapter.TaskAdapterListener {

	private List list;
	protected static final int TASK_ACTIVITY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
		
		TextView todayFilter = (TextView) findViewById(R.id.filterButton5);
		todayFilter.setOnClickListener(this);

		TextView tomorrowFilter = (TextView) findViewById(R.id.filterButton6);
		tomorrowFilter.setOnClickListener(this);

		TextView priorityFilter = (TextView) findViewById(R.id.filterButton7);
		priorityFilter.setOnClickListener(this);

		TextView more = (TextView) findViewById(R.id.filterButton8);
		more.setOnClickListener(this);
		
		todayFilter.setText("Listen");
		tomorrowFilter.setText("Morgen");
		priorityFilter.setText("Priorität");
		more.setText("Mehr");

//		Button todayFilter = (Button) findViewById(R.id.backToLists);
//		todayFilter.setOnClickListener(this);
//
//		Button tomorrowFilter = (Button) findViewById(R.id.today);
//		tomorrowFilter.setOnClickListener(this);
//
//		Button priorityFilter = (Button) findViewById(R.id.priority);
//		priorityFilter.setOnClickListener(this);
//
//		Button more = (Button) findViewById(R.id.more);
//		more.setOnClickListener(this);

		final EditText editText = (EditText) findViewById(R.id.task);
		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					list.addTask(editText.getText().toString());
					showTasks();
					InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		// case R.id.newTask:
		// EditText editText = (EditText) findViewById(R.id.task);
		// list.addTask(editText.getText().toString());
		// showTasks();
		// break;
		case R.id.filterButton5:
			Intent intent = new Intent(view.getContext(),
					ListContainerActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.filterButton6:
			ActivityUtils.startFilteredActivity(this, view, new TodayFilter());
			break;
		case R.id.filterButton7:
			ActivityUtils.startFilteredActivity(this, view,
					new PriorityHighFilter());
			break;
		case R.id.filterButton8:
			Intent moreIntent = new Intent(view.getContext(),
					MoreActivity.class);
			startActivity(moreIntent);
			finish();
			break;
		}

	}

	private void showTasks() {

		ListView listList = (ListView) findViewById(R.id.taskList);

		TaskAdapter adapter = new TaskAdapter(this, R.layout.taskitem, list);
		adapter.registerListener(this);

		listList.setAdapter(adapter);
		listList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				long taskId = ((TextView) ((FrameLayout) ((FrameLayout) ((RelativeLayout) view)
						.getChildAt(0)).getChildAt(0)).getChildAt(0))
						.getInputExtras(false).getLong("id");

				Task task = list.getTask(taskId);

				Intent intent = new Intent(view.getContext(),
						TaskActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("TaskId", task.getId());
				bundle.putLong("ListId", list.getId());
				intent.putExtras(bundle);
				startActivityForResult(intent, TASK_ACTIVITY);

			}
		});

		registerForContextMenu(listList);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		if (view.getId() == R.id.taskList) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			long taskId = ((TextView) ((FrameLayout) ((FrameLayout) ((RelativeLayout) info.targetView)
					.getChildAt(0)).getChildAt(0)).getChildAt(0))
					.getInputExtras(false).getLong("id");

			Task task = list.getTask(taskId);
			menu.setHeaderTitle(task.getName());
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
			Toast.makeText(getApplicationContext(), task.getName().toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		
		long taskId = ((TextView) ((FrameLayout) ((FrameLayout) ((RelativeLayout) info.targetView)
				.getChildAt(0)).getChildAt(0)).getChildAt(0)).getInputExtras(
				false).getLong("id");
		
		final Task task = list.getTask(taskId);
		String[] menuItems = getResources().getStringArray(R.array.menu);

		String menuItemName = menuItems[item.getItemId()];

		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		if (menuItemName.equals(menuItems[0])) { // Edit
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Name");
			builder.setMessage(task.getName());

			final EditText input = new EditText(this);
			builder.setView(input);

			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							task.setName(input.getText().toString());
							showTasks();
						}
					});

			builder.setNegativeButton("Abbrechen",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

		} else if (menuItemName.equals(menuItems[1])) { // Delete
			list.remove(task);
			showTasks();

		} else if (menuItemName.equals(menuItems[2])) { // Copy
			clipboard.setText(task.getName());
			showTasks();
		} else if (menuItemName.equals(menuItems[3])) { // Paste
			task.setName(clipboard.getText().toString());
			showTasks();
		}

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			showTasks();
		}
	}

	@Override
	public void onTaskChange() {
		showTasks();
	}
}
