package com.example.mainactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

public class GetMovieLinkTask extends AsyncTask<Void, Void, String> {
	String requestUrl;
	Activity activity;
	public GetMovieLinkTask(String url, Activity a) {
		// TODO Auto-generated constructor stub
		requestUrl = url;
		activity = a;
	}

	@Override
	protected String doInBackground(Void... params) {
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
				return ParserUtil.GetMovieLinkJSONParser.parseMovie(sb.toString());
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
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		Intent movieWebActivity = new Intent(activity, MovieWebActivity.class);
		movieWebActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		movieWebActivity.putExtra("link", result);
		activity.startActivity(movieWebActivity);
		
		super.onPostExecute(result);
	}

}
