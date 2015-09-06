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

public class CheckFavoriteTask extends AsyncTask<Void, Void, String> {
	SharedPreferences sharedPrefs;
	String postParam;
	ImageButton favButton;
	String movieId;
	String favsUrl;
	Activity activity;
	public CheckFavoriteTask(Activity a, String id, ImageButton ib){
		movieId = id;
		sharedPrefs = a.getSharedPreferences("Usernames", 0);
		postParam = "uid=" + Config.getUid(sharedPrefs.getString("username", "")) + "&mid=" + movieId;
		favsUrl = "http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites/isFavorite.php";
		favButton = ib;
		activity = a;
	}
	@Override
	protected String doInBackground(Void... params) {
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
				return ParserUtil.FavsXMLParser.isFavorite(sb.toString(), activity);
				//Log.d("demo", sb.toString());
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
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equalsIgnoreCase("false")){
			favButton.setImageResource(R.drawable.nofavorite);
			favButton.setTag("favFalse");
		}
		else{
			favButton.setImageResource(R.drawable.fav36);
			favButton.setTag("favTrue");
		}
		super.onPostExecute(result);
	}

}
