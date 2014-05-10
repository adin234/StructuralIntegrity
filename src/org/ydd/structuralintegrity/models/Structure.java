package org.ydd.structuralintegrity.models;

public class Structure {
	private String imageSrc;
	private String name;
	private String description;
	private String id;
	private float rate;
	
	public Structure(String id, String imageSrc, String name, String description, float rate) {
		this.id = id;
		this.imageSrc = imageSrc;
		this.name = name;
		this.description = description;
		this.rate = rate;
	}
	
	public String getId() {
		return id;
	}

	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
}
