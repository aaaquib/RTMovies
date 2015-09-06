package com.example.mainactivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.webkit.WebView;

public class RetrieveFavStatsTask extends AsyncTask<Void, Void, HashMap<String,Integer>>{
	WebView webView;
	SharedPreferences sharedPrefs;
	String postParam;
	String favsUrl;
	Activity activity;
	ProgressDialog progressDialog;
	public RetrieveFavStatsTask(Activity a, WebView view, ProgressDialog dialog){
		activity = a;
		webView = view;
		sharedPrefs = a.getSharedPreferences("Usernames", 0);
		postParam = "uid=" + Config.getUid(sharedPrefs.getString("username", ""));
		favsUrl = "http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites/getFavoriteStats.php";
		progressDialog = dialog;
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Loading Movies");
	}
	@Override
	protected HashMap<String, Integer> doInBackground(Void... params) {
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
				return ParserUtil.FavsXMLParser.parseFavStats(sb.toString(),activity);
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
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progressDialog.show();
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(HashMap<String, Integer> result) {
		// TODO Auto-generated method stub
		
		RetrieveFavMovieTitlesTask getTitles = new RetrieveFavMovieTitlesTask(result,webView, progressDialog);
		getTitles.execute();
		
		
		super.onPostExecute(result);
	}

}
