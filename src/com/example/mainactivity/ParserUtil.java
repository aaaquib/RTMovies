package com.example.mainactivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.widget.Toast;


//import android.util.Xml;

public class ParserUtil {
	
	static public class FavsXMLParser{
		static ArrayList<String> parseFavs(String in) throws XmlPullParserException, IOException{
			
			ArrayList<String> movieIDList = new ArrayList<String>();
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         //factory.setNamespaceAware(true);
	         final XmlPullParser xpp = factory.newPullParser();
	         
	         xpp.setInput(new ByteArrayInputStream(in.getBytes("UTF-8")), "UTF-8");
	         int eventType = xpp.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	             if(eventType == XmlPullParser.START_TAG) {
	            	 if(xpp.getName().equals("favorite")){
	            		 eventType = xpp.next();
	            		 if(xpp.getName().equals("id")){
	            			 movieIDList.add(xpp.nextText().trim());
	            		 }
	            	 }
	            	 
	             }
	            eventType = xpp.next();
	         }
	         
			return movieIDList;

		}
		
		public static String deleteAllFavorites(String in) throws XmlPullParserException, IOException{
			String message = null;
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         //factory.setNamespaceAware(true);
	         XmlPullParser xpp = factory.newPullParser();
	         
	         xpp.setInput(new ByteArrayInputStream(in.getBytes("UTF-8")), "UTF-8");
	         int eventType = xpp.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	             if(eventType == XmlPullParser.START_TAG) {
	            	 
	            	 if(xpp.getName().equals("message")){
	            		 message = xpp.nextText().trim();
	            	 }
	             }
	             eventType = xpp.next();
	         }
			return message;
	}

		
		static String isFavorite(String in, Activity activity) throws XmlPullParserException, IOException{
			String isFav = null;
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         XmlPullParser xpp = factory.newPullParser();
	         
	         xpp.setInput(new ByteArrayInputStream(in.getBytes("UTF-8")), "UTF-8");
	         int eventType = xpp.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	             if(eventType == XmlPullParser.START_TAG) {
	            	 if(xpp.getName().equals("isFavorite")){
	            		 isFav = xpp.nextText().trim();
	            	 }
	             }
	             eventType = xpp.next();
	         }
			return isFav;
	}
		
		static int parseResponse(String in, final Activity activity) throws XmlPullParserException, IOException{
			String responseCode = "1";
		
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         //factory.setNamespaceAware(true);
	         final XmlPullParser xpp = factory.newPullParser();
	         
	         xpp.setInput(new ByteArrayInputStream(in.getBytes("UTF-8")), "UTF-8");
	         int eventType = xpp.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	             if(eventType == XmlPullParser.START_TAG) {
	            	 if(xpp.getName().equals("error")){
	            		 eventType = xpp.next();
	            		 if(xpp.getName().equals("id")){
	            			 responseCode = xpp.nextText().trim();
	            		 }
	            	 }
	            	 else if(xpp.getName().equals("message")){
	            		 //Toast.makeText(activity, xpp.nextText(), Toast.LENGTH_LONG).show();
	            		 activity.runOnUiThread(new Runnable(){

	            	          @Override
	            	          public void run(){
	            	        	  
									try {
										Toast.makeText(activity, xpp.nextText(), Toast.LENGTH_SHORT).show();
									} catch (XmlPullParserException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	            	          }
	            	       });
	            	 }
	             }
	             eventType = xpp.next();
	         }
			return Integer.parseInt(responseCode);
		}

		public static HashMap<String, Integer> parseFavStats(String in, final Activity activity) throws XmlPullParserException, IOException {
			// TODO Auto-generated method stub
			HashMap<String, Integer> favData = new HashMap<String,Integer>();
			String id = ""; Integer count;
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         final XmlPullParser xpp = factory.newPullParser();
	         
	         xpp.setInput(new ByteArrayInputStream(in.getBytes("UTF-8")), "UTF-8");
	         int eventType = xpp.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	             if(eventType == XmlPullParser.START_TAG) {
	            	 if(xpp.getName().equals("favorite")){
	            		 eventType = xpp.next();
	            		 if(xpp.getName().equals("id")){
	            			 id = xpp.nextText().trim();
	            		 }
	            	 }
	            	 else if(xpp.getName().equals("count")){
	            		 count = Integer.parseInt(xpp.nextText().trim());
	            		 favData.put(id, count);
	            	 }
	            	 else if(xpp.getName().equals("message")){
	            		 activity.runOnUiThread(new Runnable(){

	            	          @Override
	            	          public void run(){
	            	        	  
									try {
										Toast.makeText(activity, xpp.nextText(), Toast.LENGTH_LONG).show();
									} catch (XmlPullParserException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	            	          }
	            	       });
	            	 }
	             }
	             eventType = xpp.next();
	         }
			return favData;
		}
		
		
}
	
	static public class MovieJSONParser{
		static MovieObject parseMovie(String in) throws JSONException {
			MovieObject movie = new MovieObject();
			
			JSONObject root = new JSONObject(in);
			
			movie.setId(root.getString("id"));
			movie.setTitle(root.getString("title"));
			movie.setYear(root.getInt("year"));
			movie.setMpaa_rating(root.getString("mpaa_rating"));
			
			JSONObject ratings = root.getJSONObject("ratings");
			if(ratings.has("critics_rating"))
				movie.setCritics_rating(ratings.getString("critics_rating"));
			if(ratings.has("audience_rating"))
				movie.setAudience_rating(ratings.getString("audience_rating"));
			
			JSONObject poster = root.getJSONObject("posters");
			movie.setThumbnail_url(poster.getString("thumbnail"));
			
			return movie;
		}

		public static String parseMovieTitles(String in) throws JSONException {
			// TODO Auto-generated method stub
			String title;
			JSONObject root = new JSONObject(in);
			title = root.getString("title");
			
			return title;
		}
	}
	
	static public class DetailMovieJSONParser{
		static MovieObject parseMovie(String in) throws JSONException {
			MovieObject movie = new MovieObject();
			
			JSONObject root = new JSONObject(in);
			
			movie.setId(root.getString("id"));
			movie.setTitle(root.getString("title"));
			movie.setYear(root.getInt("year"));
			movie.setRuntime(root.getInt("runtime"));
			JSONArray genreArray = root.getJSONArray("genres");
			ArrayList<String> genres = new ArrayList<String>();
			for(int i=0; i<genreArray.length(); i++){
				genres.add((String) genreArray.get(i));
			}
			movie.setGenres(genres);
			movie.setMpaa_rating(root.getString("mpaa_rating"));
			
			JSONObject ratings = root.getJSONObject("ratings");
			if(ratings.has("critics_rating"))
				movie.setCritics_rating(ratings.getString("critics_rating"));
			if(ratings.has("audience_rating"))
				movie.setAudience_rating(ratings.getString("audience_rating"));
			if(ratings.has("critics_score"))
				movie.setCritics_score(ratings.getInt("critics_score"));
			if(ratings.has("audience_score"))
				movie.setAudience_score(ratings.getInt("audience_score"));
			
			JSONObject poster = root.getJSONObject("posters");
			movie.setDetailed_url(poster.getString("detailed"));
			
			return movie;
		}
	}
	
	static public class JSONMovieListParser{
		static ArrayList<MovieObject> parseMovieList(String in) throws JSONException{
			ArrayList<MovieObject> moviesList = new ArrayList<MovieObject>();
			
			JSONObject root = new JSONObject(in);
			
			JSONArray moviesArray = root.getJSONArray("movies");
			
			for(int i=0; i<moviesArray.length(); i++){
				JSONObject movieObject = moviesArray.getJSONObject(i);
				MovieObject movie = new MovieObject();
				
				movie.setId(movieObject.getString("id"));
				movie.setTitle(movieObject.getString("title"));
				movie.setYear(movieObject.getInt("year"));
				movie.setMpaa_rating(movieObject.getString("mpaa_rating"));
				
				JSONObject ratings = movieObject.getJSONObject("ratings");
				if(ratings.has("critics_rating"))
					movie.setCritics_rating(ratings.getString("critics_rating"));
				if(ratings.has("audience_rating"))
					movie.setAudience_rating(ratings.getString("audience_rating"));
				
				JSONObject poster = movieObject.getJSONObject("posters");
				if(poster.has("thumbnail"))
					movie.setThumbnail_url(poster.getString("thumbnail"));
				else 
					movie.setThumbnail_url(null);
				moviesList.add(movie);
			}
			
			return moviesList;
		}
	}
	
	static public class GetMovieLinkJSONParser{
		static String parseMovie(String in) throws JSONException {
			
			JSONObject root = new JSONObject(in);
			
			JSONObject links = root.getJSONObject("links");

			String altLink = links.getString("alternate");
			
			return altLink;
		}
	}
}
