package com.whatstodo.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatstodo.ListContainer;
import com.whatstodo.R;
import com.whatstodo.list.List;
import com.whatstodo.list.ListActivity;

public class TaskActivity extends Activity implements OnClickListener {

	private ListContainer container;
	private List list;
	private Task task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_task);

		Bundle bundle = getIntent().getExtras();
		long listId = bundle.getLong("ListId");
		long taskId = bundle.getLong("TaskId");

		container = ListContainer.getInstance();
		list = container.getList(listId);
		task = list.getTask(taskId);

		setTitle("WhatsToDo");

		Button save = (Button) findViewById(R.id.taskSave);
		save.setOnClickListener(this);

		Button cancel = (Button) findViewById(R.id.taskCancel);
		cancel.setOnClickListener(this);

		EditText editText = (EditText) findViewById(R.id.taskName);
		editText.setText(task.getName());

		TextView taskNotice = (TextView) findViewById(R.id.textViewNotice);
		if (task.getNotice() != null) {
			taskNotice.setText(task.getNotice());
		} else {
			taskNotice.setText("Keine Notiz");
		}
		FrameLayout editNotice = (FrameLayout) findViewById(R.id.taskNotice);
		editNotice.setOnClickListener(this);

		TextView taskDate = (TextView) findViewById(R.id.textViewDate);
		if (task.getDate() != null) {
			taskDate.setText(task.getDate().toString());
		} else {
			taskDate.setText("Ohne Datum");
		}
		FrameLayout editDate = (FrameLayout) findViewById(R.id.taskDate);
		editDate.setOnClickListener(this);

		TextView taskReminder = (TextView) findViewById(R.id.textViewReminder);
		if (task.getReminder() != null) {
			taskReminder.setText(task.getReminder().toString());
		} else {
			taskReminder.setText("Keine Erinnerung");
		}
		FrameLayout editReminder = (FrameLayout) findViewById(R.id.taskReminder);
		editReminder.setOnClickListener(this);

		TextView taskList = (TextView) findViewById(R.id.textViewList);
		taskList.setText(list.getName());
		FrameLayout editList = (FrameLayout) findViewById(R.id.taskList);
		editList.setOnClickListener(this);

		TextView taskPriority = (TextView) findViewById(R.id.textViewPriority);
		taskPriority.setText(task.getPriority().toString());
		FrameLayout editPriority = (FrameLayout) findViewById(R.id.taskPriority);
		editPriority.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.taskSave) {
			EditText editText = (EditText) findViewById(R.id.task);
			task.setName(editText.toString());
			startListActivity();
		} else if (view.getId() == R.id.taskCancel) {
			startListActivity();
		} else if (view.getId() == R.id.taskNotice) {

			Toast.makeText(getBaseContext(), task.getNotice(),
					Toast.LENGTH_SHORT).show();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notiz");
            builder.setMessage(task.getNotice());
            
			final EditText input = new EditText(this);
			builder.setView(input);
			
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							task.setNotice(input.getText().toString());
							TextView taskNotice = (TextView) findViewById(R.id.textViewNotice);
							if (task.getNotice() != null) {
								taskNotice.setText(task.getNotice());
							} else {
								taskNotice.setText("Keine Notiz");
							}
						}
					});

			builder.setNegativeButton("Abbrechen",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			
			AlertDialog alert = builder.create();

		    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		    params.copyFrom(alert.getWindow().getAttributes());
		    params.width = WindowManager.LayoutParams.FILL_PARENT;
		    params.height = WindowManager.LayoutParams.FILL_PARENT;
		    params.width = 250;
		    params.height = 500;
		    alert.show();
		    alert.getWindow().setAttributes(params);
			//alert.show();		
			
			
		}
	}
	private void startListActivity() {
		Intent intent = new Intent(this, ListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong("ListId", list.getId()); // List ID
		intent.putExtras(bundle); // Put your id to your next Intent
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_task, menu);
		return true;
	}

}
