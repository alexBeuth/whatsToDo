package com.whatstodo.activities;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.whatstodo.R;
import com.whatstodo.manager.TaskManager;
import com.whatstodo.manager.TodoListManager;
import com.whatstodo.models.List;
import com.whatstodo.models.Priority;
import com.whatstodo.models.Task;
import com.whatstodo.utils.AlarmService;

public class TaskActivity extends FragmentActivity implements OnClickListener,
		AddressDialogFragment.NoticeDialogListener {

//	private ListContainer container;
	private List list;
	private Task task;
//	private List userList;
//	private Priority userPriority;
//	private String userNotice;
//	private Date userDate;
//	private Date userReminder;
//	private String userAddress;

	private static final int DATE_DIALOG_ID = 0;
	private static final int REMINDER_DATE_DIALOG_ID = 1;
	private static final int REMINDER_TIME_DIALOG_ID = 2;
	private static final int LIST_DIALOG_ID = 3;
	private static final int CALENDAR_DIALOG_ID = 4;
	protected static final int SYNC_ACTIVITY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Bundle bundle = getIntent().getExtras();

		long taskId = bundle.getLong("TaskId");
		nm.cancel((int) taskId);

		task = TaskManager.getInstance().load(taskId);
		list = TodoListManager.getInstance().load(task.getListId());
//		userPriority = task.getPriority();
//		userNotice = (String) task.getNotice();
//		userDate = task.getDate();
//		userReminder = task.getReminder();
//		userList = list;
//		userAddress = task.getAddress();

		setTitle("WhatsToDo");

		Button save = (Button) findViewById(R.id.taskSave);
		save.setOnClickListener(this);

		Button cancel = (Button) findViewById(R.id.taskCancel);
		cancel.setOnClickListener(this);

		EditText editText = (EditText) findViewById(R.id.taskName);
		editText.setText(task.getName());

		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setChecked(task.isDone());

		initFrameLayout(R.id.taskNotice);
		initFrameLayout(R.id.taskDate);
		initFrameLayout(R.id.taskCalendar);
		initFrameLayout(R.id.taskReminder);
		initFrameLayout(R.id.taskList);
		initFrameLayout(R.id.taskPriority);
		initFrameLayout(R.id.taskAddress);

		showTextView(R.id.textViewNotice,
				task.getNotice() == null ? "Keine Notiz" : task.getNotice());
		showDate(task.getDate());
		showCalendar(task.isCalendarCreated());
		showReminder(task.getReminder());
		showTextView(R.id.textViewList, list.getName());
		showPriority(task.getPriority());
		showTextView(R.id.textViewAdress,
				task.getAddress() == null ? "Keine Adresse" : task.getAddress());
	}

	private void initFrameLayout(int id) {
		FrameLayout layout = (FrameLayout) findViewById(id);
		layout.setOnClickListener(this);
	}

	private void showTextView(int id, String text) {
		TextView taskNotice = (TextView) findViewById(id);
		taskNotice.setText(text);
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

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.taskSave:
			EditText editText = (EditText) findViewById(R.id.taskName);
			task.setName(editText.getText().toString());
			task.setDone(getCheckBox());
			
			TaskManager.getInstance().save(task);

			setResult(Activity.RESULT_OK);
			finish();

			break;

		case R.id.taskCancel:
			setResult(Activity.RESULT_CANCELED);
			finish();
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

		case R.id.taskCalendar:
			changeCalendarEntry();
			break;

		case R.id.taskReminder:
			changeReminder();
			break;

		case R.id.taskList:
			changeList();
			break;
		case R.id.taskAddress:
			changeAddress();
			break;
		}
	}

	private void changeCalendarEntry() {
		if (task.isCalendarCreated()) {
			showDialog(CALENDAR_DIALOG_ID);
		} else {
			createCalendarEntry();
		}
	}

	private void createCalendarEntry() {

		Calendar cal = Calendar.getInstance();
		if (task.getDate() != null) {
			cal.setTime(task.getDate());
		}

		EditText editText = (EditText) findViewById(R.id.taskName);

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("endTime", cal.getTimeInMillis() + 15 * 60 * 1000);
		intent.putExtra("title", editText.getText().toString());
		intent.putExtra("description", task.getNotice());
		intent.putExtra("eventLocation", task.getAddress());
		startActivity(intent);

		task.setCalendarCreated(true);
		showCalendar(true);
	}

	private boolean getCheckBox() {
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		if (checkBox.isChecked()) {
			return true;
		}
		return false;
	}

	private void changeNotice() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Notiz");
		builder.setMessage(task.getNotice());

		final EditText input = new EditText(this);
		builder.setView(input);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String notice = input.getText().toString();
				TextView taskNotice = (TextView) findViewById(R.id.textViewNotice);
				if (notice != null) {
					taskNotice.setText(notice);
					task.setNotice(notice);
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
		
		task.setPriority(Priority.getNextPriority(task.getPriority()));
		showPriority(task.getPriority());
	}

	private void changeDate() {
		showDialog(DATE_DIALOG_ID);
	}

	private void changeReminder() {
		showDialog(REMINDER_DATE_DIALOG_ID);
	}

	private void changeList() {
		showDialog(LIST_DIALOG_ID);
	}

	private void changeAddress() {
		AddressDialogFragment dialogFragment = new AddressDialogFragment();
		dialogFragment.show(getSupportFragmentManager(), "??");
		dialogFragment.setAddress(task.getAddress());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_task, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivityForResult((new Intent(getApplicationContext(), SyncSettingsActivity.class)), SYNC_ACTIVITY);
			return true;
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			final DatePickerDialog dateDialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							Calendar cal = Calendar.getInstance();
							cal.set(year, monthOfYear, dayOfMonth);
							task.setDate(cal.getTime());
							showDate(task.getDate());
						}
					}, 2011, 0, 1);

			dateDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ohne Datum",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEUTRAL) {
								task.setDate(null);
								showDate(task.getDate());
								dialog.dismiss();
							}
						}
					});

			dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Einstellen",
					dateDialog);
			dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Abbrechen",
					dateDialog);
			dateDialog.setMessage("Datum der Aufgabe");

			dateDialog.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog) {

					float textSize = 14.0f;

					Button positive = dateDialog
							.getButton(AlertDialog.BUTTON_POSITIVE);
					positive.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

					Button neutral = dateDialog
							.getButton(AlertDialog.BUTTON_NEUTRAL);
					neutral.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

					Button negative = dateDialog
							.getButton(AlertDialog.BUTTON_NEGATIVE);
					negative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
				}
			});

			return dateDialog;

		case REMINDER_DATE_DIALOG_ID:
			final DatePickerDialog remDateDialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							Calendar calendar = Calendar.getInstance();
							calendar.set(year, monthOfYear, dayOfMonth);
							task.setReminder(calendar.getTime());
							showDialog(REMINDER_TIME_DIALOG_ID);
						}
					}, 2011, 0, 1);

			remDateDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
					"Ohne Erinnerung", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEUTRAL) {
								cancelReminderAlarm((int) task.getId());
								task.setReminder(null);
								showReminder(task.getReminder());
								dialog.dismiss();
							}
						}
					});

			remDateDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Abbrechen", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEGATIVE) {
								dialog.dismiss();
							}
						}
					});

			remDateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Zeit",
					remDateDialog);
			remDateDialog.setMessage("Datum der Erinnerung:");

			remDateDialog
					.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface dialog) {

							float textSize = 14.0f;

							Button positive = remDateDialog
									.getButton(AlertDialog.BUTTON_POSITIVE);
							positive.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
									textSize);

							Button neutral = remDateDialog
									.getButton(AlertDialog.BUTTON_NEUTRAL);
							neutral.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
									textSize);

							Button negative = remDateDialog
									.getButton(AlertDialog.BUTTON_NEGATIVE);
							negative.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
									textSize);
						}
					});

			return remDateDialog;

		case REMINDER_TIME_DIALOG_ID:
			final TimePickerDialog remTimeDialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {

							task.getReminder().setHours(hourOfDay);
							task.getReminder().setMinutes(minute);
							task.getReminder().setSeconds(0);
							showReminder(task.getReminder());
							Calendar calendar = Calendar.getInstance();
							if (calendar.getTimeInMillis() < task.getReminder()
									.getTime()) {
								setReminderAlarm(task.getReminder());
							}
						}
					}, 12, 0, true);

			remTimeDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
					"Ohne Erinnerung", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEUTRAL) {
								cancelReminderAlarm((int) task.getId());
								task.setReminder(null);
								showReminder(task.getReminder());
								dialog.dismiss();
							}
						}
					});

			remTimeDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Abbrechen", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEGATIVE) {
								dialog.dismiss();
							}
						}
					});

			remTimeDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"Einstellen", remTimeDialog);
			remTimeDialog.setMessage("Zeit der Erinnerung: ");

			remTimeDialog
					.setOnShowListener(new DialogInterface.OnShowListener() {

						@Override
						public void onShow(DialogInterface dialog) {

							float textSize = 14.0f;

							Button positive = remTimeDialog
									.getButton(AlertDialog.BUTTON_POSITIVE);
							positive.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
									textSize);

							Button neutral = remTimeDialog
									.getButton(AlertDialog.BUTTON_NEUTRAL);
							neutral.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
									textSize);

							Button negative = remTimeDialog
									.getButton(AlertDialog.BUTTON_NEGATIVE);
							negative.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
									textSize);
						}
					});
			return remTimeDialog;

		case LIST_DIALOG_ID:
			final java.util.List<List> allLists = TodoListManager.getInstance().findAll();
			final String[] listNames = new String[allLists.size()];
			int listPos = 0,
			i = 0;
			for (List lists : allLists) {
				if (lists.getId() == list.getId()) {
					listPos = i;
				}
				listNames[i++] = lists.getName();
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Wähle Liste");
			builder.setSingleChoiceItems(listNames, listPos,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});

			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_POSITIVE) {
								int checkedPosition = ((AlertDialog) dialog)
										.getListView().getCheckedItemPosition();
								
								List checkedList = allLists.get(checkedPosition);
								
								task.setListId(checkedList.getId());
								showTextView(R.id.textViewList,
										checkedList.getName());
								dialog.dismiss();
							}
						}
					});

			builder.setNegativeButton("Abbrechen",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_POSITIVE) {
								dialog.dismiss();
							}
						}
					});
			return builder.create();

		case CALENDAR_DIALOG_ID:
			AlertDialog.Builder builderCalendar = new AlertDialog.Builder(this);
			builderCalendar.setTitle("Kalendereintrag");
			builderCalendar
					.setMessage("Der Kalendereintrag für diese Aufgabe ist bereits vorhanden. Soll ein Neuer erstellt werden?");
			builderCalendar.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_POSITIVE) {
								createCalendarEntry();
								dialog.dismiss();
							}
						}
					});

			builderCalendar.setNegativeButton("Abbrechen",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_POSITIVE) {
								dialog.dismiss();
							}
						}
					});
			return builderCalendar.create();
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
				day = task.getDate().getDate();
				month = task.getDate().getMonth();
				year = task.getDate().getYear() + 1900;
			} else {
				day = calendar.get(Calendar.DAY_OF_MONTH);
				month = calendar.get(Calendar.MONTH);
				year = calendar.get(Calendar.YEAR);
			}
			dateDialog.updateDate(year, month, day);
			break;

		case REMINDER_DATE_DIALOG_ID:
			DatePickerDialog reminderDateDialog = (DatePickerDialog) dialog;
			int remDay,
			remMonth,
			remYear;

			if (task.getReminder() != null) {
				remDay = task.getReminder().getDate();
				remMonth = task.getReminder().getMonth();
				remYear = task.getReminder().getYear() + 1900;
			} else {
				remDay = calendar.get(Calendar.DAY_OF_MONTH);
				remMonth = calendar.get(Calendar.MONTH);
				remYear = calendar.get(Calendar.YEAR);
			}
			reminderDateDialog.updateDate(remYear, remMonth, remDay);
			break;

		case REMINDER_TIME_DIALOG_ID:
			TimePickerDialog reminderTimeDialog = (TimePickerDialog) dialog;
			int remHour,
			remMinute;
			if (task.getReminder() != null) {
				remHour = task.getReminder().getHours();
				remMinute = task.getReminder().getMinutes() + 1;
			} else {
				remHour = calendar.get(Calendar.HOUR_OF_DAY);
				remMinute = calendar.get(Calendar.MINUTE);
			}
			reminderTimeDialog.updateTime(remHour, remMinute);
			break;
		}
	}

	private void showCalendar(boolean calendarCreated) {
		TextView taskCalendar = (TextView) findViewById(R.id.textViewCalendar);
		taskCalendar.setText("");
		taskCalendar
				.setBackgroundResource(calendarCreated ? R.drawable.thumbsup
						: R.drawable.thumbsdown);
	}

	private void showPriority(Priority priority) {
		TextView taskPriority = (TextView) findViewById(R.id.textViewPriority);
		taskPriority.setText("");
		switch (priority) {
		case HIGH:
			taskPriority.setBackgroundResource(R.drawable.rating_important);
			break;
		case NORMAL:
			taskPriority
					.setBackgroundResource(R.drawable.rating_half_important);
			break;
		case LOW:
			taskPriority.setBackgroundResource(R.drawable.rating_not_important);
			break;
		}
	}

	private void setReminderAlarm(Date date) {

		Intent intent = new Intent(this, AlarmService.class);
		intent.putExtras(getBundleForAlarmSerive());
		PendingIntent pendingIntent = PendingIntent.getService(this,
				(int) task.getId(), intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

	}

	private void cancelReminderAlarm(int id) {

		Intent intent = new Intent(this, AlarmService.class);
		PendingIntent pendingIntent = PendingIntent.getService(
				getBaseContext(), id, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	private Bundle getBundleForAlarmSerive() {
		Bundle bundle = new Bundle();
		bundle.putLong("TaskId", task.getId());
		return bundle;
	}

	@Override
	public void onAddressDialogPositiveClick(AddressDialogFragment dialog) {
		task.setAddress(dialog.getAddress());
		showTextView(R.id.textViewAdress, task.getAddress());
	}

	@Override
	public void onAddressDialogNegativeClick(AddressDialogFragment dialog) {
		// Do nothing on cancel
	}
}
