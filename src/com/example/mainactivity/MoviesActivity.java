package com.example.mainactivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class MoviesActivity extends Activity {
	
	ProgressDialog pdialog;
	public ListView list;
	int selectedItem;
	String limit = "&limit=50";
	String page_limit = "&page_limit=50";
	Boolean favList = false;
	int pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movies);
		// Show the Up button in the action bar.
		setupActionBar();
		pdialog = new ProgressDialog(this);
		pdialog.setCancelable(false);
		pdialog.setMessage("Loading Movies");
		list = (ListView) findViewById(R.id.listView_movies);
		selectedItem = getIntent().getIntExtra("item", 0);
		switch(selectedItem){
		case 0: 
			RetrieveFavsDataTask retrieveFavs = new RetrieveFavsDataTask(this, list, "http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites/getAllFavorites.php", pdialog);
			retrieveFavs.execute();
			favList=true;
			break;
		case 1:
			RetrieveMoviesListTask retrieveBoxOfficeMovies = new RetrieveMoviesListTask(this, list, "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=" + MainActivity.RTKEY + limit);
			retrieveBoxOfficeMovies.execute();
			favList = false;
			break;
		case 2:
			RetrieveMoviesListTask retrieveInTheaterMovies = new RetrieveMoviesListTask(this, list, "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=" + MainActivity.RTKEY + page_limit);
			retrieveInTheaterMovies.execute();
			favList = false;
			break;
		case 3:
			RetrieveMoviesListTask retrieveOpeningMovies = new RetrieveMoviesListTask(this, list, "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/opening.json?apikey=" + MainActivity.RTKEY + limit);
			retrieveOpeningMovies.execute();
			favList = false;
			break;
		case 4:
			RetrieveMoviesListTask retrieveUpcomingMovies = new RetrieveMoviesListTask(this, list, "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?apikey=" + MainActivity.RTKEY + page_limit);
			retrieveUpcomingMovies.execute();
			favList = false;
			break;
		default:
			break;
		}
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				pos = position;
				MovieObject movie = (MovieObject) list.getItemAtPosition(position);
				Intent movieActivity = new Intent(MoviesActivity.this,MovieActivity.class);
				movieActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				movieActivity.putExtra("movieId", movie.getId());
				startActivityForResult(movieActivity, 0);
			}
		});
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(favList == true){
					MovieObject movie = (MovieObject) list.getItemAtPosition(position);
					DeleteFavoriteTask delTask = new DeleteFavoriteTask(MoviesActivity.this,movie.getId(),null);
					delTask.execute();
					((CustomAdapter)list.getAdapter()).removeItem(position);
		        	((CustomAdapter)list.getAdapter()).notifyDataSetChanged();
				}
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {

		     if(resultCode == RESULT_OK){
		         Boolean result=data.getBooleanExtra("dataSetChanged", false);
				if(result == true && favList == true){
					((CustomAdapter)list.getAdapter()).removeItem(pos);
		        	((CustomAdapter)list.getAdapter()).notifyDataSetChanged();
				}
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		super.onActivityResult(requestCode, resultCode, data);
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
		getMenuInflater().inflate(R.menu.movies, menu);
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
        	ClearAllFavoritesTask clearFavs = new ClearAllFavoritesTask(this,list);
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
