package com.example.mainactivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class UsernameActivity extends Activity {
	SharedPreferences prefs;
	EditText nameText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_username);
		// Show the Up button in the action bar.
		setupActionBar();
		
		nameText = (EditText) findViewById(R.id.editText1);
		Button button = (Button) findViewById(R.id.button1);
		nameText.setError("Enter valid Username");
		
		prefs = getSharedPreferences("Usernames", 0);
		if(prefs.contains("username")){
			nameText.setText(prefs.getString("username", ""));
		}
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(nameText.getText().toString().matches("")){
					nameText.setError("Enter valid Username");
				    return;
				}
				else{
					String name = nameText.getText().toString();
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("username", name);
					editor.commit();
					setResult(RESULT_OK);
					
					finish();
				}
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.username, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.clearFav :
        	ClearAllFavoritesTask clearFavs = new ClearAllFavoritesTask(this,null);
        	clearFavs.execute();            	
            return true;
        case R.id.enterName:
        	Intent usernameActivity = new Intent(this,UsernameActivity.class);
			startActivity(usernameActivity);
            return true;
        case R.id.exit:
        	finish();
        	moveTaskToBack(true);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}

}
