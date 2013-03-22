package com.whatstodo.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.whatstodo.R;

public class SyncSettingsActivity extends Activity implements OnClickListener {

	public static final String PREFERENCES_NAME = "UserSettings";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_settings);
		
		SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);

		EditText editUsername = (EditText) findViewById(R.id.editUsername);
		editUsername.setText(settings.getString("Username", ""));

		EditText editIP = (EditText) findViewById(R.id.editIP);
		editIP.setText(settings.getString("IP", ""));

		EditText editPort = (EditText) findViewById(R.id.editPort);
		editPort.setText(settings.getString("Port", ""));

		Button save = (Button) findViewById(R.id.settingSave);
		save.setOnClickListener(this);

		Button cancel = (Button) findViewById(R.id.settingCancel);
		cancel.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sync_settings, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.settingSave:
			
			SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_NAME, 0).edit();
			
			EditText editUsername = (EditText) findViewById(R.id.editUsername);
			editor.putString("Username", editUsername.getText().toString());
			EditText editIP = (EditText) findViewById(R.id.editIP);
			editor.putString("IP", editIP.getText().toString());
			EditText editPort = (EditText) findViewById(R.id.editPort);
			editor.putString("Port", editPort.getText().toString());
			
			editor.commit();
			setResult(Activity.RESULT_OK);
			finish();

			break;

		case R.id.settingCancel:
			setResult(Activity.RESULT_CANCELED);
			finish();
			break;

		}
	}
}
