package com.whatstodo;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.whatstodo.list.List;

public class ListAdapter extends ArrayAdapter<List>{
	
	public ListAdapter(Context ctx, int textViewResourceId,
			ListContainer container) {
		super(ctx, textViewResourceId);
	}
}
