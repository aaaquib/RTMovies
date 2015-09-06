package com.example.mainactivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ImageButton;

public class DeleteFavoriteTask extends AsyncTask<Void, Void, Integer>{
	String favsUrl = "http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites/deleteFavorite.php";
	String postParam; 
	Activity activity;
	SharedPreferences sharedPrefs;
	String movieId;
	ImageButton favButton;
	
	public DeleteFavoriteTask(Activity a, String id, ImageButton ib) {
		// TODO Auto-generated constructor stub
		activity = a;
		movieId = id;
		sharedPrefs = activity.getSharedPreferences("Usernames", 0);
		postParam = "uid=" + Config.getUid(sharedPrefs.getString("username", "")) + "&mid=" + movieId;
		favButton = ib;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(favsUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", Integer.toString(postParam.getBytes().length));
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	        wr.writeBytes(postParam);
	        wr.flush();
	        wr.close();
			con.connect();
			int statusCode = con.getResponseCode();
			if(statusCode == HttpURLConnection.HTTP_OK){
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line = reader.readLine();
				while(line!=null){
					sb.append(line);
					line = reader.readLine();
				}
				return ParserUtil.FavsXMLParser.parseResponse(sb.toString(), activity);
			}
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		if(result == 0 && favButton!=null){
			favButton.setImageResource(R.drawable.nofavorite);
			favButton.setTag("favFalse");
		}
		super.onPostExecute(result);
	}

}
