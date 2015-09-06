package com.example.mainactivity;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

public class RetrieveMovieInfoTask extends AsyncTask<Void, MovieObject, ArrayList<MovieObject>>{
	Activity activity;
	ListView listView;
	ArrayList<String> movieIds;
	String movieID = "";
	String url = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
	String apikey = MainActivity.RTKEY;		;
	CustomAdapter adapter;
	ProgressDialog dialog;
	
	public RetrieveMovieInfoTask(Activity a, ListView list, ArrayList<String> movieIds, ProgressDialog pd) {
		// TODO Auto-generated constructor stub
		this.listView = list;
		this.movieIds = movieIds;
		activity = a;
		dialog = pd;
	}

	@Override
	protected ArrayList<MovieObject> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		ArrayList<MovieObject> moviesList = new ArrayList<MovieObject>();
		
		for(int i=0; i<movieIds.size(); i++){
		movieID = movieIds.get(i);
		String requestUrl = url + movieID + ".json?apikey=" + apikey;
		
		StringBuilder sb = new StringBuilder();
			URL url;
			try {
				url = new URL(requestUrl);
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int statusCode = con.getResponseCode();
			if(statusCode == HttpURLConnection.HTTP_OK){
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line = reader.readLine();
				while(line!=null){
					sb.append(line);
					line = reader.readLine();
				}
			moviesList.add(ParserUtil.MovieJSONParser.parseMovie(sb.toString()));
			}
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return moviesList;
	}

	@Override
	protected void onPostExecute(ArrayList<MovieObject> result) {
		// TODO Auto-generated method stub
		
		adapter = new CustomAdapter(activity, result);
		listView.setAdapter(adapter);
		
		dialog.cancel();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(MovieObject... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
