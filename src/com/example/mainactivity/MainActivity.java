package com.example.mainactivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.TextView;

public class MainActivity extends Activity {
	Intent moviesActivity;
	String username,uid;
	String favsURL; 
	SharedPreferences sharedPrefs;
	//Intent usernameActivity;
	String[] main_menu = { "My Favorite Movies", "Box Office Movies", "In Theaters Movies", "Opening Movies",
							"Upcoming Movies", "Favorite Statistics" };
	public static final String RTKEY = "45geu43zspcjppc67s4jmvtt";
	String RTUrl = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=45geu43zspcjppc67s4jmvtt&q=harry+potter&page_limit=5";
			 
	private ListView main_ListView;
			 
	private ArrayAdapter<String> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sharedPrefs = getSharedPreferences("Usernames", 0);
		if(!sharedPrefs.contains("username")){
			Intent usernameActivity = new Intent(this,UsernameActivity.class);
			startActivityForResult(usernameActivity,0);
		}
		
		uid = Config.getUid(sharedPrefs.getString("username", ""));
		Log.d("demo", uid + "  " + sharedPrefs.getString("username", ""));
		main_ListView = (ListView) findViewById(R.id.listView_main);
		
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, main_menu);
		
		main_ListView.setAdapter(arrayAdapter);
		
		main_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				
				switch((int)id){

					case 0:
						moviesActivity = new Intent(getApplicationContext(),MoviesActivity.class);
						moviesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						moviesActivity.putExtra("item", 0);
						startActivity(moviesActivity);
						break;
					case 1:
						moviesActivity = new Intent(getApplicationContext(),MoviesActivity.class);
						moviesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						moviesActivity.putExtra("item", 1);
						startActivity(moviesActivity);
						break;
					case 2:
						moviesActivity = new Intent(getApplicationContext(),MoviesActivity.class);
						moviesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						moviesActivity.putExtra("item", 2);
						startActivity(moviesActivity);
						break;
					case 3:
						moviesActivity = new Intent(getApplicationContext(),MoviesActivity.class);
						moviesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						moviesActivity.putExtra("item", 3);
						startActivity(moviesActivity);
						break;
					case 4:
						moviesActivity = new Intent(getApplicationContext(),MoviesActivity.class);
						moviesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						moviesActivity.putExtra("item", 4);
						startActivity(moviesActivity);
						break;
					case 5:
						Intent statActivity = new Intent(getApplicationContext(), StatisticsActivity.class);
						statActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(statActivity);
						break;
				}
			}
		});
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
        case 0:
            	if(!sharedPrefs.contains("username")){
        			Intent usernameActivity = new Intent(this,UsernameActivity.class);
        			startActivityForResult(usernameActivity,0);
        		}
            break;
        default:
            break;
    }
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
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
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

