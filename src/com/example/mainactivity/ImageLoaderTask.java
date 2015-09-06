package com.example.mainactivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
	ImageView imageView;
	String thumb_url;
	DiskLruCache imageCache;
	String key;
    
	public ImageLoaderTask(ImageView iv, String url,DiskLruCache imageCache, String key){
		this.imageView = iv;
		this.thumb_url = url;
		this.imageCache = imageCache;
		this.key = key;
	}
	
	public ImageLoaderTask(ImageView iv, String url){
		this.imageView = iv;
		this.thumb_url = url;
		this.imageCache = null;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Bitmap bmp = null;
		try {
			URL url = new URL(thumb_url);
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return bmp;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		if(result==null){
			imageView.setImageResource(R.drawable.poster_not_found);
		}
		else{
			imageView.setImageBitmap(result);
			if(imageCache!=null){
				imageCache.put(key, result);
			}
		}
		super.onPostExecute(result);
	}
}
