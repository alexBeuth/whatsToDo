package com.whatstodo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whatstodo.R;
import com.whatstodo.filter.PriorityHighFilter;
import com.whatstodo.filter.TodayFilter;
import com.whatstodo.filter.TomorrowFilter;
import com.whatstodo.models.List;
import com.whatstodo.models.ListContainer;
import com.whatstodo.utils.ActivityUtils;

public class ListContainerActivity extends Activity implements OnClickListener {

	protected static final int LIST_ACTIVITY = 0;
	protected static final int FILTER_ACTIVITY = 1;
	ListContainer container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontainer);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Button createList = (Button) findViewById(R.id.newList);
		createList.setOnClickListener(this);
		
		TextView todayFilter = (TextView) findViewById(R.id.filterButton1);
		todayFilter.setText("Heute");
		todayFilter.setOnClickListener(this);

		TextView tomorrowFilter = (TextView) findViewById(R.id.filterButton2);
		tomorrowFilter.setText("Morgen");
		tomorrowFilter.setOnClickListener(this);

		TextView priorityFilter = (TextView) findViewById(R.id.filterButton3);
		priorityFilter.setText("Priorität");
		priorityFilter.setOnClickListener(this);

		TextView more = (TextView) findViewById(R.id.filterButton4);
		more.setText("Mehr");
		more.setOnClickListener(this);

//		Button todayFilter = (Button) findViewById(R.id.today);
//		todayFilter.setOnClickListener(this);
//
//		Button tomorrowFilter = (Button) findViewById(R.id.tomorrow);
//		tomorrowFilter.setOnClickListener(this);
//
//		Button priorityFilter = (Button) findViewById(R.id.priority);
//		priorityFilter.setOnClickListener(this);
//
//		Button more = (Button) findViewById(R.id.more);
//		more.setOnClickListener(this);

		final EditText editText = (EditText) findViewById(R.id.list);
		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					container.addList(editText.getText().toString());
					showLists();
					InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

					return true;
				}
				return false;
			}
		});

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

		switch (view.getId()) {
		// case R.id.newList:
		// EditText editText = (EditText) findViewById(R.id.list);
		// container.addList(editText.getText().toString());
		// showLists();
		// break;
		case R.id.filterButton1:
			ActivityUtils.startFilteredActivity(this, view, new TodayFilter());
			break;
		case R.id.filterButton2:
			ActivityUtils.startFilteredActivity(this, view,
					new TomorrowFilter());
			break;
		case R.id.filterButton3:
			ActivityUtils.startFilteredActivity(this, view,
					new PriorityHighFilter());
			break;
		case R.id.filterButton4:
			Intent intent = new Intent(view.getContext(), MoreActivity.class);
			startActivity(intent);
			finish();
			break;
		}

	}

	/**
	 * Shows all existing lists in a linear layout. The method gets the lists
	 * from the class List Container.
	 */
	private void showLists() {

		ListView listList = (ListView) findViewById(R.id.list1);

		ListAdapter adapter = new ListAdapter(this, R.layout.listitem,
				container.getLists());

		listList.setAdapter(adapter);
		listList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				long listId = ((TextView) ((FrameLayout) ((FrameLayout) ((FrameLayout) view)
						.getChildAt(0)).getChildAt(0)).getChildAt(0))
						.getInputExtras(false).getLong("id");

				Intent intent = new Intent(view.getContext(),
						ListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("ListId", listId); // List
				intent.putExtras(bundle); // Put your id to your next Intent
				startActivityForResult(intent, LIST_ACTIVITY);

			}
		});

		registerForContextMenu(listList);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		if (view.getId() == R.id.list1) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			List list = container.getLists().get(info.position);
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
		final List list = container.getLists().get(info.position);

		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		if (menuItemName.equals(menuItems[0])) { // Edit
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Name");
			builder.setMessage(list.getName());

			final EditText input = new EditText(this);
			builder.setView(input);

			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							list.setName(input.getText().toString());
							showLists();
						}
					});

			builder.setNegativeButton("Abbrechen",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

		} else if (menuItemName.equals(menuItems[1])) { // Delete
			container.deleteList(list.getId());
			showLists();

		} else if (menuItemName.equals(menuItems[2])) { // Copy
			clipboard.setText(list.getName());
			showLists();
		} else if (menuItemName.equals(menuItems[3])) { // Paste
			list.setName(clipboard.getText().toString());
			showLists();
		}

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {
			showLists();
		}
	}
}
