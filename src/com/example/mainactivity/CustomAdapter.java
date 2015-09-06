package com.example.mainactivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	
	private Activity activity;
	ArrayList<MovieObject> moviesList;
	private static LayoutInflater inflater=null;
	DiskLruCache imageCache;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; 
	
	public CustomAdapter(Activity a, ArrayList<MovieObject> data) {
        activity = a;
        moviesList = data;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageCache = DiskLruCache.openCache(a.getApplicationContext(), a.getCacheDir(), DISK_CACHE_SIZE);

    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return moviesList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return moviesList.get(position);
	}
	
	public void removeItem(int position){
		moviesList.remove(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi=convertView;
		if(convertView==null){
            vi = inflater.inflate(R.layout.list_row, null);
		}
		
		TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView year = (TextView)vi.findViewById(R.id.year); // year
        TextView mpaa_rating = (TextView)vi.findViewById(R.id.mpaa_rating); // mpaa rating
        
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        ImageView critics_image=(ImageView)vi.findViewById(R.id.critics_rating);
        ImageView audience_image=(ImageView)vi.findViewById(R.id.audience_rating);
        ImageLoaderTask imgLoader;
        
        title.setText(moviesList.get(position).getTitle());
        year.setText(moviesList.get(position).getYear().toString());
        mpaa_rating.setText(moviesList.get(position).getMpaa_rating());
        
        String critics_rating = moviesList.get(position).getCritics_rating();
        String aud_rating = moviesList.get(position).getAudience_rating();
        
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
       
        if(imageCache.containsKey(moviesList.get(position).getId())){
        	Bitmap bmp = imageCache.get(moviesList.get(position).getId());
        	thumb_image.setImageBitmap(bmp);
        }
        else{
	        String thumb_url = moviesList.get(position).getThumbnail_url();
	        if(thumb_url != null){
	        	imgLoader = new ImageLoaderTask(thumb_image, thumb_url, imageCache, moviesList.get(position).getId());
	        	imgLoader.execute();
	        }
	        else{
	        	thumb_image.setImageResource(R.drawable.poster_not_found);
	        }
        }
		return vi;
	}
	public void removeAllItems() {
		// TODO Auto-generated method stub
		moviesList.clear();
	}

}
