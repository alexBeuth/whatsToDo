package com.whatstodo.activities.adapter;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whatstodo.R;
import com.whatstodo.filter.Filter;
import com.whatstodo.manager.TaskManager;
import com.whatstodo.models.Priority;
import com.whatstodo.models.Task;

public class TaskAdapter extends ArrayAdapter<Task> implements OnClickListener {

	public interface TaskAdapterListener {
		public void onTaskChange();

	}

	TaskAdapterListener listener;

	private Context context;
	private List<Task> tasks;

	public TaskAdapter(Context ctx, int textViewResourceId,
			java.util.List<Task> tasks) {
		super(ctx, textViewResourceId, tasks);
		context = ctx;
		this.tasks = tasks;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View element = convertView;
		if (element == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			element = inflater.inflate(R.layout.taskitem, null);
		}

		Task task = tasks.get(position);
		
		//set on click listener to overlaying layouts
		FrameLayout parentLayout = (FrameLayout) element.findViewById(R.id.clickTaskLayout);
		parentLayout.setTag(task.getId());
		FrameLayout taskClickDone = (FrameLayout) element.findViewById(R.id.clickTaskDone);
		taskClickDone.setOnClickListener(this);
		FrameLayout clickTaskPriority = (FrameLayout) element.findViewById(R.id.clickTaskPriority);
		clickTaskPriority.setOnClickListener(this);
		
		TextView taskName = (TextView) element.findViewById(R.id.taskName);
		TextView taskDate = (TextView) element.findViewById(R.id.taskDate);
		TextView taskPriority = (TextView) element
				.findViewById(R.id.taskPriority);
		
		
		TextView taskDone = (TextView) element.findViewById(R.id.taskDone);

		FrameLayout button = (FrameLayout) element
				.findViewById(R.id.buttonLayout);

		taskName.setText(task.getName());
		taskName.getInputExtras(true).putLong("id", task.getId());

		if (task.getDate() != null) {
			taskDate.setText(DateFormat.format("dd.MM.yyyy", task.getDate()));

			Calendar taskCal = Calendar.getInstance();
			taskCal.setTime(task.getDate());
			if (Filter.compareDate(taskCal, Calendar.getInstance()) < 0
					&& !task.isDone()) {
				taskDate.setTextColor(context.getResources().getColor(
						R.color.Red));
			}
		} else {
			taskDate.setText("");
		}

		switch (task.getPriority()) {
		case HIGH:
			taskPriority.setBackgroundResource(R.drawable.rating_important);
			break;
		case NORMAL:
			taskPriority
					.setBackgroundResource(R.drawable.rating_half_important);
			break;
		case LOW:
			taskPriority.setBackgroundResource(R.drawable.rating_not_important);
		}

		if (task.isDone()) {
			taskDone.setBackgroundResource(R.drawable.check);
			button.setBackgroundResource(R.drawable.buttontransparent);
			taskName.setTextColor(context.getResources().getColor(
					R.color.DarkGray));
			taskDate.setTextColor(context.getResources().getColor(
					R.color.DarkGray));
		} else {
			taskDone.setBackgroundResource(R.drawable.checkbox3);
			button.setBackgroundResource(R.drawable.button3);
			taskName.setTextColor(context.getResources()
					.getColor(R.color.Black));
		}

		return element;
	}

	@Override
	public void onClick(View view) {

		long taskId = (Long) ((FrameLayout) view.getParent()).getTag();
		Task task = TaskManager.getInstance().load(taskId);
		
		switch (view.getId()) {
		case R.id.clickTaskDone:
			task.setDone(!task.isDone());
			TaskManager.getInstance().save(task);
			listener.onTaskChange();
			break;
		case R.id.clickTaskPriority:
			task.setPriority(Priority.getNextPriority(task.getPriority()));
			TaskManager.getInstance().save(task);
			listener.onTaskChange();
			break;

		}
	}

	public void registerListener(Activity activity) {
		try {
			listener = (TaskAdapterListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}
}
