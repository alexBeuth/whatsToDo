package com.whatstodo.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.whatstodo.R;

public class ListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list);

		Bundle bundle = getIntent().getExtras();
		String listName = bundle.getString("ListName");
		
		setTitle(listName);

		startLayoutConfiguration(listName);
	}

	private void startLayoutConfiguration(String listName) {
		//TODO 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}

}
