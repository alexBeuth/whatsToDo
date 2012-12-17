package com.whatstodo.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

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

		setContentView(R.layout.activity_list);

		Bundle bundle = getIntent().getExtras();
		long listId = bundle.getLong("ListId");
		long taskId = bundle.getLong("TaskId");

		container = ListContainer.getInstance();
		list = container.getList(listId);
		task = list.getTask(taskId);

		setTitle("WhatsToDo");

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);

		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);

		EditText editText = (EditText) findViewById(R.id.task);
		editText.setText(task.getName());

		TextView taskNotice = (TextView) findViewById(R.id.textViewNotice);
		taskNotice.setText(task.getNotice());
		FrameLayout editNotice = (FrameLayout) findViewById(R.id.taskNotice);
		editNotice.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.save) {
			EditText editText = (EditText) findViewById(R.id.task);
			task.setName(editText.toString());
			startListActivity();
		} else if (view.getId() == R.id.cancel) {
			startListActivity();
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
