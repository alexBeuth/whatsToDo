package com.whatstodo.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.whatstodo.ListContainer;
import com.whatstodo.R;
import com.whatstodo.task.Task;

public class ListActivity extends Activity implements OnClickListener{

	private ListContainer container;
	private List list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list);

		Bundle bundle = getIntent().getExtras();
		long listId = bundle.getLong("ListId");
		
		container = ListContainer.getInstance();
		list = container.getList(listId);
		
		setTitle(list.getName());

		Button createTask = (Button) findViewById(R.id.newTask);
		createTask.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		
		if(view.getId() == R.id.newTask) {
			EditText editText = (EditText) findViewById(R.id.task);
			list.addTask(editText.getText().toString());
			showTasks();
		} else {
			
			
		}
		// TODO Auto-generated method stub
		
	}

	private void showTasks() {
		
		LinearLayout taskList = (LinearLayout) findViewById(R.id.taskLayout);
		taskList.removeAllViewsInLayout();
		for(Task task : list) {
			Button taskButton = new Button(this);
			taskButton.setText(task.getName());
			taskButton.setOnClickListener(this);

			taskList.addView(taskButton);
			
		}
		
	}
	
	

}
