package com.example.quikchannel.data;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Entry implements Serializable {
	private String text;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	private String author;
	private String time;
	private String title;
	private Bitmap image;
	private int ID;
	
	public Entry(String text) {
		this.text = text;
	}
}
