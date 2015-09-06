package com.example.mainactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.webkit.WebView;

public class RetrieveFavMovieTitlesTask extends AsyncTask<Void, Void, HashMap<String, Integer>>{
	
	WebView webView;
	Set<String> movieIds;
	HashMap<String, Integer> favData;
	String movieID = "";
	String url = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
	String apikey = MainActivity.RTKEY;
	ProgressDialog dialog;
	
	public RetrieveFavMovieTitlesTask(HashMap<String, Integer> favData, WebView view, ProgressDialog dialog){
		webView = view;
		this.favData = favData;
		this.movieIds = favData.keySet();
		this.dialog = dialog;
	}
	
	@Override
	protected HashMap<String, Integer> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		HashMap<String,Integer> favStats = new HashMap<String,Integer>();
		Iterator<String> iter = movieIds.iterator();
		while (iter.hasNext()) {
		movieID = (String) iter.next();
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
				favStats.put(ParserUtil.MovieJSONParser.parseMovieTitles(sb.toString()), favData.get(movieID));
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
		
		return favStats;
	}

	@Override
	protected void onPostExecute(HashMap<String, Integer> favStats) {
		// TODO Auto-generated method stub
		
		Set<String> titles= favStats.keySet();
		Iterator<String> i = titles.iterator();
		String data = "['Title', 'Count']";
		while(i.hasNext()){
			String title = i.next();
			data = data + ",['" + title + "'," + favStats.get(title).toString() + "]";
		}
		
		String customHtml = "<html>" +
  "<head>" + 
    "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>" + 
    "<script type=\"text/javascript\">" +
      "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});" +
      "google.setOnLoadCallback(drawChart);" +
      "function drawChart() {" +
        "var data = google.visualization.arrayToDataTable([" +
          data +
        "]);" +
        "var options = {" +
          "title: 'Favorite Statistics'," +
          "hAxis: {title: 'Favorites Count', maxValue: 10,  titleTextStyle: {color: 'red'}}" +
        "};" +
        "var chart = new google.visualization.BarChart(document.getElementById('chart_div'));" +
        "chart.draw(data, options);" +
      "}" +
    "</script>" +
  "</head>" +
  "<body>" +
    "<div id=\"chart_div\" style=\"width: 600px; height: 500px;\"></div>" +
  "</body>" +
"</html>";
		webView.loadData(customHtml, "text/html", "UTF-8");
		
		dialog.cancel();
		super.onPostExecute(favStats);
	}

}
