package com.whatstodo.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.whatstodo.R;
import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.models.Task;

public class MyAlarmService extends Service {

	private ListContainer container;
	private List list;
	private Task task;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Bundle bundle = intent.getExtras();
		container = ListContainer.getInstance();
		list = container.getList(bundle.getLong("ListId"));
		task = list.getTask(bundle.getLong("ListId"));
		
		if(task.getReminder()!=null){
			Intent nIntent = new Intent(this, TaskActivity.class);
			nIntent.putExtras(bundle);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					nIntent, 0);
			
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notif = new Notification(R.drawable.ic_launcher, "WhatsToDo", System.currentTimeMillis());
			notif.setLatestEventInfo(this, "WhatsToDo", "Erinnerung: "+task.getName(), pendingIntent);
//			notif.vibrate = new long[] {100, 250, 100, 500};
			nm.notify((int) task.getId(), notif);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}