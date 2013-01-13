package com.whatstodo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.whatstodo.R;
import com.whatstodo.filter.AllFilter;
import com.whatstodo.filter.Next7DaysFilter;
import com.whatstodo.filter.NoDateFilter;
import com.whatstodo.filter.OverdueFilter;
import com.whatstodo.filter.PriorityHighFilter;
import com.whatstodo.filter.PriorityLowFilter;
import com.whatstodo.filter.PriorityNormalFilter;
import com.whatstodo.filter.TodayFilter;
import com.whatstodo.filter.TomorrowFilter;
import com.whatstodo.utils.ActivityUtils;

public class MoreActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setTitle("Mehr");

		FrameLayout allTasks = (FrameLayout) findViewById(R.id.allTasks);
		allTasks.setOnClickListener(this);

		FrameLayout next7Days = (FrameLayout) findViewById(R.id.next7Days);
		next7Days.setOnClickListener(this);

		FrameLayout noDate = (FrameLayout) findViewById(R.id.noDate);
		noDate.setOnClickListener(this);

		FrameLayout overdue = (FrameLayout) findViewById(R.id.overdue);
		overdue.setOnClickListener(this);

		FrameLayout prioHigh = (FrameLayout) findViewById(R.id.prioHigh);
		prioHigh.setOnClickListener(this);

		FrameLayout prioNormal = (FrameLayout) findViewById(R.id.prioNormal);
		prioNormal.setOnClickListener(this);

		FrameLayout prioLow = (FrameLayout) findViewById(R.id.prioLow);
		prioLow.setOnClickListener(this);

		FrameLayout today = (FrameLayout) findViewById(R.id.today);
		today.setOnClickListener(this);

		FrameLayout tomorrow = (FrameLayout) findViewById(R.id.tomorrow);
		tomorrow.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.allTasks:
			ActivityUtils.startFilteredActivity(this, view, new AllFilter());
			break;
		case R.id.next7Days:
			ActivityUtils.startFilteredActivity(this, view,
					new Next7DaysFilter());
			break;
		case R.id.noDate:
			ActivityUtils.startFilteredActivity(this, view, new NoDateFilter());
			break;
		case R.id.overdue:
			ActivityUtils
					.startFilteredActivity(this, view, new OverdueFilter());
			break;
		case R.id.prioHigh:
			ActivityUtils.startFilteredActivity(this, view,
					new PriorityHighFilter());
			break;
		case R.id.prioNormal:
			ActivityUtils.startFilteredActivity(this, view,
					new PriorityNormalFilter());
			break;
		case R.id.prioLow:
			ActivityUtils.startFilteredActivity(this, view,
					new PriorityLowFilter());
			break;
		case R.id.today:
			ActivityUtils.startFilteredActivity(this, view, new TodayFilter());
			break;
		case R.id.tomorrow:
			ActivityUtils.startFilteredActivity(this, view,
					new TomorrowFilter());
			break;
		}

	}

}
