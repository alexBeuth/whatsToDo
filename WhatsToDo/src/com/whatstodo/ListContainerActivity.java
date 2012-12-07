package com.whatstodo;

import com.whatstodo.list.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ListContainerActivity extends Activity implements OnClickListener {
	
	ListContainer container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		container = ListContainer.getInstance();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		Button createList = (Button) findViewById(R.id.newList);
		createList.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}
	
	// @Override
	public void onClick(View view) {
		EditText editText = (EditText) findViewById(R.id.listName);
		container.addList(editText.getText().toString());
	}

	private void showLists() {
		
		for(List list : container.getLists()) {
			//TODO
		}


	}

}
