package com.whatstodo.activities.adapter;

import com.whatstodo.models.List;

import com.whatstodo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<List> {

	private Context context;
	private java.util.List<List> lists;

	public ListAdapter(Context ctx, int textViewResourceId,
			java.util.List<List> lists) {
		super(ctx, textViewResourceId, lists);
		context = ctx;
		this.lists = lists;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View element = convertView;
		if (element == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			element = inflater.inflate(R.layout.listitem, null);
		}
		List list = lists.get(position);

		TextView listName = (TextView) element.findViewById(R.id.listName);
		TextView numberOfTasks = (TextView) element
				.findViewById(R.id.numberOfTasks);

		listName.getInputExtras(true).putLong("id", list.getId());
		listName.setText(list.getName());
		numberOfTasks.setText(Integer.toString(list.getDisplayedSize()));

		return element;
	}
}
