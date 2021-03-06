package com.example.mainactivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.support.v4.app.NavUtils;

public class MovieActivity extends Activity {
	String movieId;
	String url = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
	String apikey = ".json?apikey=";
	ImageButton webButton,backButton;
	ImageButton favButton;
	Intent returnIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);
		// Show the Up button in the action bar.
		setupActionBar();
		
		returnIntent = new Intent();
		returnIntent.putExtra("dataSetChanged", false);
		movieId = getIntent().getExtras().getString("movieId");
		MovieDetailInfoTask getMovieDetails = new MovieDetailInfoTask(this, url + movieId + apikey + MainActivity.RTKEY);
		getMovieDetails.execute();
		
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		webButton = (ImageButton) findViewById(R.id.imageButton2);
		favButton = (ImageButton) findViewById(R.id.imageButton3);
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		webButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				GetMovieLinkTask getLink = new GetMovieLinkTask(url + movieId + apikey + MainActivity.RTKEY, MovieActivity.this);
				getLink.execute();
			}
		});
		
		favButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(favButton.getTag().equals("favTrue")){					
					DeleteFavoriteTask delTask = new DeleteFavoriteTask(MovieActivity.this,movieId,favButton);
					delTask.execute();
					
				}
				else if(favButton.getTag().equals("favFalse")){
					AddToFavoritesTask addTask = new AddToFavoritesTask(MovieActivity.this,movieId,favButton);
					addTask.execute();
				}
				
				 returnIntent.removeExtra("dataSetChanged");
				 returnIntent.putExtra("dataSetChanged", true);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK,returnIntent);
		finish();
		super.onBackPressed();
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
		getMenuInflater().inflate(R.menu.movie, menu);
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
        	returnIntent.removeExtra("dataSetChanged");
			returnIntent.putExtra("dataSetChanged", true);
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
