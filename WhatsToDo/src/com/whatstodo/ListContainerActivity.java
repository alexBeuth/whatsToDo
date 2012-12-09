package com.whatstodo;

import java.lang.reflect.Field;

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
					bundle.putString("ListName", viewName); //List Name
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
			//count++;
			Button listButton = new Button(this);
			listButton.setText(list.getName());
			listButton.setOnClickListener(this);

			listList.addView(listButton);
			
		}
	}

	/**
     * Determines the Name of a Resource,
     * by passing the <code>R.xyz.class</code> and
     * the <code>resourceID</code> of the class to it.
     * @param aClass : like <code>R.drawable.class</code>
     * @param resourceID : like <code>R.drawable.icon</code>
     * @throws IllegalArgumentException if field is not found.
     * @throws NullPointerException if <code>aClass</code>-Parameter is null.
     * <br><br>
     * <b>Example-Call:</b><br>
     * <code>String resName = getResourceNameFromClassByID(R.drawable.class, R.drawable.icon);</code><br>
     * Then <code>resName</code> would be '<b>icon</b>'.*/
    public String getResourceNameFromClassByID(Class<?> aClass, int resourceID)
                                            throws IllegalArgumentException{
            /* Get all Fields from the class passed. */
            Field[] drawableFields = aClass.getFields();
           
            /* Loop through all Fields. */
            for(Field f : drawableFields){
                    try {
                            /* All fields within the subclasses of R
                             * are Integers, so we need no type-check here. */
                           
                            /* Compare to the resourceID we are searching. */
                            if (resourceID == f.getInt(null))
                                    return f.getName(); // Return the name.
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
            }
            /* Throw Exception if nothing was found*/
            throw new IllegalArgumentException();
    }
}
