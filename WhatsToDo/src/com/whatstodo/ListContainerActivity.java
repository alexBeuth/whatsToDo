package com.whatstodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.whatstodo.list.List;
import com.whatstodo.list.ListActivity;

public class ListContainerActivity extends Activity implements OnClickListener{
	
	ListContainer container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontainer);
		
		Button createList = (Button) findViewById(R.id.newList);
		createList.setOnClickListener(this);
		
		container = ListContainer.getInstance();
		showLists();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}
	
	// @Override
	public void onClick(View view) {
		
		if(view.getId() == R.id.newList) {
			EditText editText = (EditText) findViewById(R.id.list);
			container.addList(editText.getText().toString());
			showLists();
		}
		else if (view instanceof Button){
			
		    String viewName = ((Button)view).getText().toString();
		    			
			for(List list : container.getLists()){
				
				if(viewName == list.getName()){
					Intent intent = new Intent(this, ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putLong("ListId", list.getId()); //List ID
					intent.putExtras(bundle); //Put your id to your next Intent
					startActivity(intent);
					//finish();					
				}
			}
		}
	}
	
	private void showLists() {
		
		LinearLayout listList = (LinearLayout) findViewById(R.id.listLayout);
		listList.removeAllViewsInLayout();
		for(List list : container.getLists()) {
			Button listButton = new Button(this);
			listButton.setText(list.getName());
			listButton.setOnClickListener(this);

			listList.addView(listButton);
			
		}
	}
}
