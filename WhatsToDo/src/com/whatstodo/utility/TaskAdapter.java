package com.whatstodo.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whatstodo.R;
import com.whatstodo.task.Task;

public class TaskAdapter extends ArrayAdapter<Task> {

	private Context context;
	private java.util.List<Task> tasks;

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

		TextView taskName = (TextView) element.findViewById(R.id.taskName);
		TextView taskPriority = (TextView) element.findViewById(R.id.taskPriority);
		TextView taskDone = (TextView) element.findViewById(R.id.taskDone);

		taskName.setText(task.getName());
		//TODO Pictures!
		taskPriority.setText(task.getPriority().toString());
		taskDone.setText("false");
		
		

		return element;
	}
}
