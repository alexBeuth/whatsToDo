package com.whatstodo.activities;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.whatstodo.R;
import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.models.Task;
import com.whatstodo.utils.Priority;

public class TaskActivity extends Activity implements OnClickListener {

	private ListContainer container;
	private List list;
	private Task task;
	private Priority userPriority;
	private String userNotice;
	private Date userDate;
	private Date userReminder;
	private static final int DATE_DIALOG_ID = 0;
	private static final int REMINDER_DATE_DIALOG_ID = 1;
	private static final int REMINDER_TIME_DIALOG_ID = 2;

	private PendingIntent pendingIntent;

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
		userDate = task.getDate();
		userReminder = task.getReminder();

		setTitle("WhatsToDo");

		Button save = (Button) findViewById(R.id.taskSave);
		save.setOnClickListener(this);

		Button cancel = (Button) findViewById(R.id.taskCancel);
		cancel.setOnClickListener(this);

		EditText editText = (EditText) findViewById(R.id.taskName);
		editText.setText(task.getName());

		showNotice(task.getNotice());
		FrameLayout editNotice = (FrameLayout) findViewById(R.id.taskNotice);
		editNotice.setOnClickListener(this);

		showDate(task.getDate());
		FrameLayout editDate = (FrameLayout) findViewById(R.id.taskDate);
		editDate.setOnClickListener(this);

		showReminder(task.getReminder());
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

		switch (view.getId()) {

		case R.id.taskSave:
			EditText editText = (EditText) findViewById(R.id.taskName);
			task.setName(editText.getText().toString());
			task.setNotice(userNotice);
			task.setPriority(userPriority);
			task.setDate(userDate);
			task.setReminder(userReminder);

			startListActivity();
			break;

		case R.id.taskCancel:
			startListActivity();
			break;

		case R.id.taskNotice:
			changeNotice();
			break;

		case R.id.taskPriority:
			changePriority();
			break;

		case R.id.taskDate:
			changeDate();
			break;

		case R.id.taskReminder:
			changeReminder();
			break;

		case R.id.taskList:
			changeList();
			break;
		}
	}

	private void changeNotice() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Notiz");
		builder.setMessage(userNotice);

		final EditText input = new EditText(this);
		builder.setView(input);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

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
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void changePriority() {
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
	}

	private void changeDate() {

		 showDialog(DATE_DIALOG_ID);
	}

	private void changeReminder() {

		 showDialog(REMINDER_DATE_DIALOG_ID);
	}

	// private void showDateDialog() {
	//
	// final Dialog dateDialog = new Dialog(this,
	// android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	// dateDialog.setContentView(R.layout.datelayout);
	//
	// DatePicker datePicker = (DatePicker) dateDialog
	// .findViewById(R.id.dateReminderDate);
	// int day, month, year;
	//
	// if (task.getReminder() != null) {
	// day = task.getReminder().getDay();
	// month = task.getReminder().getMonth();
	// year = task.getReminder().getYear();
	// } else {
	// Calendar calendar = Calendar.getInstance();
	// day = calendar.get(Calendar.DAY_OF_MONTH);
	// month = calendar.get(Calendar.MONTH);
	// year = calendar.get(Calendar.YEAR);
	// }
	// datePicker.updateDate(year, month, day);
	//
	// Button buttonTime = (Button) dateDialog.findViewById(R.id.reminderTime);
	// buttonTime.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// showTimeDialog();
	// dateDialog.dismiss();
	// }
	// });
	//
	// dateDialog.show();
	//
	// }

	// private void showTimeDialog() {
	//
	// final Dialog timeDialog = new Dialog(this,
	// android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	// timeDialog.setContentView(R.layout.timelayout);
	//
	// TimePicker timePicker = (TimePicker) timeDialog
	// .findViewById(R.id.timePickerTime);
	// int hour, minute;
	//
	// if (task.getReminder() != null) {
	// hour = task.getReminder().getHours();
	// minute = task.getReminder().getMinutes();
	// } else {
	// Calendar calendar = Calendar.getInstance();
	// hour = calendar.get(Calendar.HOUR);
	// minute = calendar.get(Calendar.MINUTE);
	// }
	// timePicker.setIs24HourView(true);
	// timePicker.setCurrentHour(hour);
	// timePicker.setCurrentMinute(minute);
	//
	// Button buttonTime = (Button) timeDialog.findViewById(R.id.reminderDate);
	// buttonTime.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// showDateDialog();
	// timeDialog.dismiss();
	//
	// }
	// });
	//
	// Button buttonSetDate = (Button) timeDialog
	// .findViewById(R.id.timeReminderSave);
	// buttonSetDate.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// }
	// });
	// timeDialog.show();
	// }

	private void changeList() {
		// TODO Auto-generated method stub
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

		case REMINDER_DATE_DIALOG_ID:
			DatePickerDialog remDateDialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							Calendar cal = Calendar.getInstance();
							cal.set(year, monthOfYear, dayOfMonth);
							userReminder = cal.getTime();
							showDialog(REMINDER_TIME_DIALOG_ID);

						}
					}, 2011, 0, 1);

			remDateDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
					"Ohne Erinnerung", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEUTRAL) {

								userReminder = null;
								showReminder(userReminder);
							}
						}
					});

			remDateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Zeit",
					remDateDialog);
			remDateDialog.setMessage("Datum der Erinnerung:");
			return remDateDialog;

		case REMINDER_TIME_DIALOG_ID:
			TimePickerDialog remTimeDialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {

							userReminder.setHours(hourOfDay);
							userReminder.setMinutes(minute);
							userReminder.setSeconds(0);
							showReminder(userReminder);
							setReminderAlarm(userReminder);
						}
					}, 12, 0, true);

			remTimeDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
					"Ohne Erinnerung", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEUTRAL) {

								userReminder = null;
								showReminder(userReminder);
							}
						}
					});

			remTimeDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"Einstellen", remTimeDialog);
			remTimeDialog.setMessage("Zeit der Erinnerung: ");
			return remTimeDialog;
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		super.onPrepareDialog(id, dialog);
		Calendar calendar = Calendar.getInstance();

		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dateDialog = (DatePickerDialog) dialog;
			int day,
			month,
			year;

			if (task.getDate() != null) {
				day = task.getDate().getDay();
				month = task.getDate().getMonth();
				year = task.getDate().getYear();
			} else {
				day = calendar.get(Calendar.DAY_OF_MONTH);
				month = calendar.get(Calendar.MONTH);
				year = calendar.get(Calendar.YEAR);
			}
			dateDialog.updateDate(year, month, day);
			break;

		case REMINDER_DATE_DIALOG_ID:
			DatePickerDialog reminderDateDialog = (DatePickerDialog) dialog;
			int rDay,
			rMonth,
			rYear;

			if (task.getReminder() != null) {
				rDay = task.getReminder().getDay();
				rMonth = task.getReminder().getMonth();
				rYear = task.getReminder().getYear();
			} else {
				rDay = calendar.get(Calendar.DAY_OF_MONTH);
				rMonth = calendar.get(Calendar.MONTH);
				rYear = calendar.get(Calendar.YEAR);
			}
			reminderDateDialog.updateDate(rYear, rMonth, rDay);
			break;

		case REMINDER_TIME_DIALOG_ID:
			TimePickerDialog reminderTimeDialog = (TimePickerDialog) dialog;
			int rHour,
			rMinute;
			if (task.getReminder() != null) {
				rHour = task.getReminder().getHours();
				rMinute = task.getReminder().getMinutes();
			} else {
				rHour = calendar.get(Calendar.HOUR_OF_DAY);
				rMinute = calendar.get(Calendar.MINUTE);
			}
			reminderTimeDialog.updateTime(rHour, rMinute);
			break;
		}
	}

	private void showNotice(String string) {

		TextView taskNotice = (TextView) findViewById(R.id.textViewNotice);
		if (string != null) {
			taskNotice.setText(string);
		} else {
			taskNotice.setText("Keine Notiz");
		}
	}

	private void showDate(Date date) {

		TextView taskDate = (TextView) findViewById(R.id.textViewDate);
		if (date != null) {
			String stringDate = DateFormat.format("dd.MM.yyyy", date)
					.toString();
			taskDate.setText(stringDate);
		} else {
			taskDate.setText("Ohne Datum");
		}
	}

	private void showReminder(Date date) {

		TextView taskReminder = (TextView) findViewById(R.id.textViewReminder);
		if (date != null) {
			String stringDate = DateFormat.format("kk:mm dd.MM.yyyy", date)
					.toString();
			taskReminder.setText(stringDate);
		} else {
			taskReminder.setText("Keine Erinnerung");
		}
	}

	private void setReminderAlarm(Date date) {

		Intent intent = new Intent(this, AlarmActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("Task", task.getName()); // List ID
		intent.putExtras(bundle);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(),
				pendingIntent);
		
	}
}
