package com.whatstodo.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.whatstodo.R;
import com.whatstodo.manager.TodoListManager;
import com.whatstodo.models.List;
import com.whatstodo.net.ListSynchronizer;
import com.whatstodo.net.SynchronizationException;

public class TodoActivity extends ListActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initTextView(R.id.filterButton5, "Listen");
		initTextView(R.id.filterButton6, "Heute");
		initTextView(R.id.filterButton7, "Priorität");
		initTextView(R.id.filterButton8, "Mehr");
		
		final EditText createTask = (EditText) findViewById(R.id.createTask);
		
		createTask.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter"
				// button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					
					String taskName = createTask.getText().toString();
					list.addTask(taskName);
					TodoListManager.getInstance().save(list, true);
					refreshListView();
					
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							createTask.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	protected List getTodoData() {
		Bundle bundle = getIntent().getExtras();
		long listId = bundle.getLong("ListId");
		return TodoListManager.getInstance().load(listId, true);
	}

	@Override
	protected void startSynchronisation() {
		try {
			ListSynchronizer synchronizer = new ListSynchronizer();
			if (synchronizer.serverIsAvailble()) {
				List syncedTodo = synchronizer.synchronizeList(list);
				list = TodoListManager.getInstance().replace(syncedTodo);
				refreshListView();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Server is not available", Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (SynchronizationException e) {
			Log.e(ListActivity.class.toString(),
					"An error error occured during synchronization", e);
			
			Toast toast = Toast.makeText(getApplicationContext(),
					"An error error occured during synchronization",
					Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}
	
}
