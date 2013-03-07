package com.whatstodo.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.whatstodo.R;
import com.whatstodo.activities.TaskActivity;
import com.whatstodo.manager.TaskManager;
import com.whatstodo.manager.TodoListManager;
import com.whatstodo.models.Task;

public class AlarmService extends Service {

	private Task task;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Bundle bundle = intent.getExtras();
		
		TodoListManager.getInstance().load(bundle.getLong("ListId"));
		task = TaskManager.getInstance().load(bundle.getLong("TaskId"));


		if (task.getReminder() != null) {

			Intent nIntent = new Intent(this, TaskActivity.class);
			nIntent.putExtras(bundle);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					nIntent, 0);

			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notif = new Notification(R.drawable.ic_launcher,
					"WhatsToDo", System.currentTimeMillis());
			notif.setLatestEventInfo(this, "WhatsToDo",
					"Erinnerung: " + task.getName(), pendingIntent);
			notif.vibrate = new long[] { 100, 250, 100, 500 };
			nm.notify((int) task.getId(), notif);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
