package org.ydd.structuralintegrity.models;

public class Comments {
	private String images;
	private String id;
	private String comment;
	private String date;
	private float rating;
	
	
	public Comments(String id, String comment, String date, String images, float rating) {
		super();
		this.id = id;
		this.comment = comment;
		this.date = date;
		this.images = images;
		this.rating = rating;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public float getRate() {
		return this.rating;
	}
}
