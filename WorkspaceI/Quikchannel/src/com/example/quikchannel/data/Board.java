package com.example.quikchannel.data;

import java.util.ArrayList;

public class Board {
	private String title;
	private ArrayList<Thread> threads;
	
	public Board(String title) {
		this.threads = new ArrayList<Thread>();
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ArrayList<Thread> getThreads() {
		return threads;
	}
}