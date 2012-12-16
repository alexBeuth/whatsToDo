package com.whatstodo;

import java.util.ArrayList;
import com.whatstodo.list.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ListAdapter extends ArrayAdapter<List>{
	
	public ListAdapter(Context ctx, int textViewResourceId,
			ListContainer container) {
		super(ctx, textViewResourceId);
	}
}
