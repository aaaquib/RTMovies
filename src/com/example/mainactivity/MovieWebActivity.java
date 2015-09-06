package com.example.mainactivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.NavUtils;

public class MovieWebActivity extends Activity {
	WebView webView;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_web);
		// Show the Up button in the action bar.
		setupActionBar();
		
		String movieLink = getIntent().getExtras().getString("link");
		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(movieLink);
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
		getMenuInflater().inflate(R.menu.movie_web, menu);
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

	private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    } 
	
}
