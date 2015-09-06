package com.example.mainactivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

//import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;

public class RetrieveFavsDataTask extends AsyncTask<Void, Void, ArrayList<String>> {
	ListView list;
	String favsUrl;
	String postParam; 
	Activity activity;
	SharedPreferences sharedPrefs;
	ProgressDialog dialog;
	public RetrieveFavsDataTask(Activity a, ListView lv, String url, ProgressDialog dialog) {
		// TODO Auto-generated constructor stub
		sharedPrefs = a.getSharedPreferences("Usernames", 0);
		postParam = "uid=" + Config.getUid(sharedPrefs.getString("username", ""));
		list = lv;
		favsUrl = url;
		activity = a;
		this.dialog = dialog;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog.show();
		super.onPreExecute();
	}

	@Override
	protected ArrayList<String> doInBackground(Void... params) {
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
				return ParserUtil.FavsXMLParser.parseFavs(sb.toString());
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
	protected void onPostExecute(ArrayList<String> movieIds) {
		// TODO Auto-generated method stub
		RetrieveMovieInfoTask retrieveMovies = new RetrieveMovieInfoTask(activity, list,movieIds,dialog);
		retrieveMovies.execute();
		super.onPostExecute(movieIds);
	}



	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
