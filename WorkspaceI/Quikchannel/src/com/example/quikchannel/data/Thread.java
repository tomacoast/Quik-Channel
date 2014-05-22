package com.example.quikchannel.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Thread implements Serializable {
	private String title;
	private ArrayList<Entry> entries;
	
	public Thread(String title) {
		this.entries = new ArrayList<Entry>();
		this.title = title;
	}
	
	public ArrayList<Entry> getEntries() {
		return entries;
	}
 	
	public String getTitle() {
		return title;
	}
	
	public Thread asPreview(int threads) {
		Thread prev = new Thread(this.title);
		prev.entries.add(entries.get(0));
		for (int i = this.entries.size() - threads; i < this.entries.size(); i++) {
			prev.entries.add(this.entries.get(i));
		}
		return prev;
	}
}
