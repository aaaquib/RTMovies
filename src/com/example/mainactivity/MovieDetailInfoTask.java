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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailInfoTask extends AsyncTask<Void, Void, MovieObject> {
	Activity activity;
	String movieId;
	String requestUrl;
	ProgressDialog pdialog;
	public MovieDetailInfoTask(Activity a, String url)
	{
		activity = a;
		requestUrl = url;
		pdialog = new ProgressDialog(activity);
		pdialog.setCancelable(false);
		pdialog.setMessage("Loading Movie Info");
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pdialog.show();
		super.onPreExecute();
	}
	@Override
	protected MovieObject doInBackground(Void... params) {
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
				//return ParserUtil.JSONParser.parsePhotos(sb.toString());
			return ParserUtil.DetailMovieJSONParser.parseMovie(sb.toString());
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
	protected void onPostExecute(MovieObject movie) {
		// TODO Auto-generated method stub
		
		TextView titleText = (TextView)activity.findViewById(R.id.textView3);
		titleText.setText(movie.getTitle());
		ImageView detailedImage = (ImageView) activity.findViewById(R.id.movie_imageView1);
		ImageLoaderTask loader = new ImageLoaderTask(detailedImage, movie.getDetailed_url());
		loader.execute();
		
		TextView yearText = (TextView)activity.findViewById(R.id.movie_year);
		yearText.setText(movie.getYear().toString());
		TextView mpaaText = (TextView)activity.findViewById(R.id.movie_mpaa);
		mpaaText.setText(movie.getMpaa_rating());
		TextView lengthText = (TextView)activity.findViewById(R.id.movie_length);
		lengthText.setText(movie.getRuntime().toString() + "min.");
		TextView genreText = (TextView)activity.findViewById(R.id.movie_genres);
		ArrayList<String> genres = movie.getGenres();
		String genreString = "";
		int i;
		for(i=0;i<genres.size()-1;i++)
			genreString = genreString + genres.get(i) + ",";
		genreString = genreString + genres.get(i);
		genreText.setText(genreString);
		
		ImageView critics_image=(ImageView)activity.findViewById(R.id.movie_imageView3);
        ImageView audience_image=(ImageView)activity.findViewById(R.id.movie_imageView2);
        TextView critics_score = (TextView)activity.findViewById(R.id.movie_critScore);
        TextView aud_score = (TextView)activity.findViewById(R.id.movie_audScore);
        
        String critics_rating = movie.getCritics_rating();
        String aud_rating = movie.getAudience_rating();
        
        
        if(critics_rating==null)
        	critics_image.setImageResource(R.drawable.notranked);
        else if(critics_rating.equalsIgnoreCase("Certified Fresh"))
        	critics_image.setImageResource(R.drawable.certified_fresh);
        else if(critics_rating.equalsIgnoreCase("Fresh"))
        	critics_image.setImageResource(R.drawable.fresh);
        else if(critics_rating.equalsIgnoreCase("Rotten"))
        	critics_image.setImageResource(R.drawable.rotten);
        if(aud_rating == null)
        	audience_image.setImageResource(R.drawable.notranked);
        else if(aud_rating.equalsIgnoreCase("Upright"))
        	audience_image.setImageResource(R.drawable.upright);
        else if(aud_rating.equalsIgnoreCase("Spilled"))
        	audience_image.setImageResource(R.drawable.spilled);
        else if(aud_rating.equalsIgnoreCase("Not Ranked"))
        	audience_image.setImageResource(R.drawable.notranked);
        
        
         critics_score.setText(movie.getCritics_score().toString());
         aud_score.setText(movie.getAudience_score().toString());
         
         ImageButton favButton = (ImageButton) activity.findViewById(R.id.imageButton3);
         movieId = movie.getId();
         
         CheckFavoriteTask cfTask = new CheckFavoriteTask(activity, movieId, favButton);
         cfTask.execute();
         
		pdialog.cancel();
		super.onPostExecute(movie);
	}
}
