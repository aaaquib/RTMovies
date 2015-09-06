package com.example.mainactivity;

import java.util.ArrayList;

//import android.graphics.Bitmap;

public class MovieObject {
	String thumbnail_url, detailed_url;
	String title,mpaa_rating;
	ArrayList<String> genres;
	Integer year, runtime, critics_score = 0, audience_score = 0;
	String id;
	String audience_rating, critics_rating;
	
	public String getDetailed_url() {
		return detailed_url;
	}
	public void setDetailed_url(String detailed_url) {
		this.detailed_url = detailed_url;
	}
	public ArrayList<String> getGenres() {
		return genres;
	}
	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}
	public Integer getRuntime() {
		return runtime;
	}
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}
	public Integer getCritics_score() {
		return critics_score;
	}
	public void setCritics_score(Integer critics_score) {
		this.critics_score = critics_score;
	}
	public Integer getAudience_score() {
		return audience_score;
	}
	public void setAudience_score(Integer audience_score) {
		this.audience_score = audience_score;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMpaa_rating() {
		return mpaa_rating;
	}
	public void setMpaa_rating(String mpaa_rating) {
		this.mpaa_rating = mpaa_rating;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getAudience_rating() {
		return audience_rating;
	}
	public void setAudience_rating(String audience_rating) {
		this.audience_rating = audience_rating;
	}
	public String getCritics_rating() {
		return critics_rating;
	}
	public void setCritics_rating(String critics_rating) {
		this.critics_rating = critics_rating;
	}
	

}
