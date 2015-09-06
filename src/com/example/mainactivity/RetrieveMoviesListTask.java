package com.example.mainactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

public class RetrieveMoviesListTask extends AsyncTask<Void, Void, ArrayList<MovieObject>> {
	Activity activity;
	ListView listView;
	String requestUrl;
	public CustomAdapter adapter;
	ProgressDialog dialog;
	
	public RetrieveMoviesListTask(Activity a, ListView list, String url){
		this.activity = a;
		this.listView = list;
		this.requestUrl = url;
		dialog = new ProgressDialog(activity);
		dialog.setCancelable(false);
		dialog.setMessage("Loading Movies");
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog.show();
		super.onPreExecute();
	}
	@Override
	protected ArrayList<MovieObject> doInBackground(Void... params) {
		// TODO Auto-generated method stub
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
				return ParserUtil.JSONMovieListParser.parseMovieList(sb.toString());
					
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		return null;
	}
	@Override
	protected void onPostExecute(ArrayList<MovieObject> result) {
		// TODO Auto-generated method stub
		if(result != null){
			adapter = new CustomAdapter(activity, result);
			listView.setAdapter(adapter);
		}
		else
			Toast.makeText(activity, "result is null", Toast.LENGTH_SHORT).show();
		dialog.cancel();
		super.onPostExecute(result);
	}

}
