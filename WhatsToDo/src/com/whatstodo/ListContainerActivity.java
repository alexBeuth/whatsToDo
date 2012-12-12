package com.whatstodo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.whatstodo.list.List;
import com.whatstodo.list.ListActivity;

public class ListContainerActivity extends Activity implements OnClickListener {

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

		if (view.getId() == R.id.newList) {
			EditText editText = (EditText) findViewById(R.id.list);
			container.addList(editText.getText().toString());
			showLists();
		} else if (view instanceof Button) {

			String viewName = ((Button) view).getText().toString();

			for (List list : container.getLists()) {

				if (viewName == list.getName()) {
					Intent intent = new Intent(this, ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putLong("ListId", list.getId()); // List ID
					intent.putExtras(bundle); // Put your id to your next Intent
					startActivity(intent);
					// finish();
				}
			}
		}
	}

	/**
	 * Shows all existing lists in a linear layout. The method gets the lists
	 * from the class List Container.
	 */

	private void showLists() {

		ArrayList<String> listNames = new ArrayList<String>();

		for (List list : container.getLists()) {
			listNames.add(list.getName());
		}
		ListView listList1 = (ListView) findViewById(R.id.list1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.listitem, listNames);
		listList1.setAdapter(adapter);
		registerForContextMenu(listList1);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list1) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			List list = container.getList(info.id);
			menu.setHeaderTitle(list.getName());
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		String[] menuItems = getResources().getStringArray(R.array.menu);
		String menuItemName = menuItems[item.getItemId()];
		List list = container.getList(info.id);
		String listItemName = list.getName();

		TextView text = (TextView) findViewById(R.id.footer);
		text.setText(String.format("Selected %s for item %s", menuItemName,
				listItemName));
		return true;
	}
}
