package com.whatstodo.task;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatstodo.ListContainer;
import com.whatstodo.R;
import com.whatstodo.list.List;
import com.whatstodo.list.ListActivity;
import com.whatstodo.utility.Priority;

public class TaskActivity extends Activity implements OnClickListener {

	private ListContainer container;
	private List list;
	private Task task;
	private Priority userPriority;
	private String userNotice;
	private Date userDate;
	private static final int DATE_DIALOG_ID = 0;

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
		userPriority = task.getPriority();
		userNotice = (String) task.getNotice();

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

		showDate(task.getDate());

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
			EditText editText = (EditText) findViewById(R.id.taskName);
			task.setName(editText.getText().toString());
			task.setNotice(userNotice);
			task.setPriority(userPriority);
			task.setDate(userDate);

			startListActivity();

		} else if (view.getId() == R.id.taskCancel) {
			startListActivity();

		} else if (view.getId() == R.id.taskNotice) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Notiz");
			builder.setMessage(userNotice);

			final EditText input = new EditText(this);
			builder.setView(input);

			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							userNotice = input.getText().toString();
							TextView taskNotice = (TextView) findViewById(R.id.textViewNotice);
							if (userNotice != null) {
								taskNotice.setText(userNotice);
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
			alert.show();

		} else if (view.getId() == R.id.taskPriority) {
			TextView taskPriority = (TextView) findViewById(R.id.textViewPriority);
			switch (userPriority) {
			case LOW:
				userPriority = Priority.NORMAL;
				break;
			case NORMAL:
				userPriority = Priority.HIGH;
				break;
			case HIGH:
				userPriority = Priority.LOW;
				break;
			default:
				Toast.makeText(getBaseContext(), "Priority not found",
						Toast.LENGTH_LONG).show();
			}
			taskPriority.setText(userPriority.toString());

		} else if (view.getId() == R.id.taskDate) {
			showDialog(DATE_DIALOG_ID);
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dateDialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							Calendar cal = Calendar.getInstance();
							cal.set(year, monthOfYear, dayOfMonth);
							userDate = cal.getTime();
							showDate(userDate);
						}
					}, 2011, 0, 1);

			dateDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ohne Datum",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEUTRAL) {
								userDate = null;
								showDate(userDate);
							}
						}
					});
			dateDialog.setMessage("Datum der Aufgabe");
			return dateDialog;
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dateDlg = (DatePickerDialog) dialog;
			int Day,
			Month,
			Year;

			if (task.getDate() != null) {
				Day = task.getDate().getDay();
				Month = task.getDate().getMonth();
				Year = task.getDate().getYear();
			} else {
				Calendar calendar = Calendar.getInstance();
				Day = calendar.get(Calendar.DAY_OF_MONTH);
				Month = calendar.get(Calendar.MONTH);
				Year = calendar.get(Calendar.YEAR);
			}
			dateDlg.updateDate(Year, Month, Day);
			break;
		}
	}

	private void showDate(Date date) {

		TextView taskDate = (TextView) findViewById(R.id.textViewDate);
		if (date != null) {
			CharSequence stringDate = DateFormat.format("dd.MM.yyyy", date);
			taskDate.setText(stringDate);
		} else {
			taskDate.setText("Ohne Datum");
		}
	}
}
