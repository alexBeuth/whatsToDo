package com.whatstodo;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class ListActivity extends Activity implements OnClickListener {
	
	java.util.List<List> lists = new LinkedList<List>();
	int count = 0;
	int listHeight = 60;
	int listWidth = 300;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		Button buttonCreateList = (Button) findViewById(R.id.buNewList);
		buttonCreateList.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}
	
	// @Override
	public void onClick(View view) {
		EditText editText = (EditText) findViewById(R.id.etNewList);
		lists.add(new List(editText.getText().toString()));
		showLists();
	}

	private void showLists() {
		FrameLayout listlist = (FrameLayout) findViewById(R.id.flList);

		int showListWidth = listWidth;
		int showListHeight = listHeight;

		int count = 0;
		for (List list : lists) {
			count++;
			Button buList = new Button(this);
			buList.setText(list.name);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					showListWidth, showListHeight);
			params.topMargin = count * showListHeight;
			params.gravity = Gravity.TOP + Gravity.LEFT;
			listlist.addView(buList, params);
		}
	}

}
