package com.whatstodo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.whatstodo.activities.adapter.TaskAdapter;
import com.whatstodo.filter.PriorityHighFilter;
import com.whatstodo.filter.TodayFilter;
import com.whatstodo.filter.TomorrowFilter;
import com.whatstodo.manager.TaskManager;
import com.whatstodo.models.List;
import com.whatstodo.models.Task;
import com.whatstodo.utils.ActivityUtils;

public abstract class ListActivity extends Activity implements OnClickListener,
		TaskAdapter.TaskAdapterListener {

	protected List list;
	protected static final int TASK_ACTIVITY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		list = getTodoData();
		setTitle(list.getName());

		Button syncButton = (Button) findViewById(R.id.synchronisationList);
		syncButton.setOnClickListener(this);

		initListView();
	}

	protected abstract List getTodoData();

	protected TextView initTextView(int id, String text) {
		TextView textView = (TextView) findViewById(id);
		if (text != null)
			textView.setText(text);
		textView.setOnClickListener(this);

		return textView;
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

		case R.id.synchronisationList:
			startSynchronisation();
			break;
		case R.id.filterButton5:
			Intent intent = new Intent(view.getContext(),
					ListContainerActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.filterButton6:
			startFilter(((TextView) view).getText().toString(), view);
			break;
		case R.id.filterButton7:
			startFilter(((TextView) view).getText().toString(), view);
			break;
		case R.id.filterButton8:
			Intent moreIntent = new Intent(view.getContext(),
					MoreActivity.class);
			startActivity(moreIntent);
			finish();
			break;
		}

	}

	private void startFilter(String filter, View view) {
		if (filter.equals("Heute")) {
			ActivityUtils.startFilteredActivity(this, view, new TodayFilter());
		} else if (filter.equals("Morgen")) {
			ActivityUtils.startFilteredActivity(this, view,
					new TomorrowFilter());
		} else if (filter.equals("Priorität")) {
			ActivityUtils.startFilteredActivity(this, view,
					new PriorityHighFilter());
		}
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		if (view.getId() == R.id.taskList) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			long taskId = ((TextView) ((FrameLayout) ((FrameLayout) ((RelativeLayout) info.targetView)
					.getChildAt(0)).getChildAt(0)).getChildAt(0))
					.getInputExtras(false).getLong("id");

			Task task = TaskManager.getInstance().load(taskId);
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
							TaskManager.getInstance().save(task);
							refreshListView();
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
			TaskManager.getInstance().delete(task);
			refreshListView();

		} else if (menuItemName.equals(menuItems[2])) { // Copy
			clipboard.setText(task.getName());
			refreshListView();
		} else if (menuItemName.equals(menuItems[3])) { // Paste
			task.setName(clipboard.getText().toString());
			refreshListView();
		}

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			refreshListView();
		}
	}

	@Override
	public void onTaskChange() {
		refreshListView();
	}
	
	private void initListView() {

		ListView taskListView = (ListView) findViewById(R.id.taskList);

		list = getTodoData();
		TaskAdapter adapter = new TaskAdapter(this, R.layout.taskitem, list);
		adapter.registerListener(this);

		taskListView.setAdapter(adapter);
		taskListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				long taskId = ((TextView) ((FrameLayout) ((FrameLayout) ((RelativeLayout) view)
						.getChildAt(0)).getChildAt(0)).getChildAt(0))
						.getInputExtras(false).getLong("id");

				Intent intent = new Intent(view.getContext(),
						TaskActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("TaskId", taskId);
				bundle.putLong("ListId", list.getId());
				intent.putExtras(bundle);
				startActivityForResult(intent, TASK_ACTIVITY);
			}
		});

		registerForContextMenu(taskListView);
	}
	
	protected void refreshListView() {
		
		ListView taskListView = (ListView) findViewById(R.id.taskList);
		list = getTodoData();
		TaskAdapter adapter = new TaskAdapter(this, R.layout.taskitem, list);
		adapter.registerListener(this);

		taskListView.setAdapter(adapter);
	}

	private void startSynchronisation() {
		// TODO Auto-generated method stub

	}
}
