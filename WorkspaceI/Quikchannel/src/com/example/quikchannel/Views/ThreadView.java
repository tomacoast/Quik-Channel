package com.example.quikchannel.Views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quikchannel.data.Thread;
import com.example.quikchannel.data.Entry;

public class ThreadView extends LinearLayout {

	private Thread thread;
	private Thread expandedThread;
	private EntryView topPost;
	private ArrayList<EntryView> entryViews;
	
	public ThreadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		create();
		entryViews = new ArrayList<EntryView>();
		topPost = new EntryView(getContext());
		this.addView(topPost);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(p);
		topPost.setLayoutParams(p);
		this.setOrientation(LinearLayout.VERTICAL);
	}
	
	public ThreadView(Context context) {
		super(context);
		create();
	}
	
	private void create() {
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
	}

	public void setThread(Thread thread) {
		this.thread = thread;
		this.entryViews = new ArrayList<EntryView>();
		topPost.setEntry(thread.getEntries().get(0));
		this.setId(thread.getEntries().get(0).getID());
		for (int i = 0; i < thread.getEntries().size(); i++) {
			if (i != 0) {
				entryViews.add(new EntryView(getContext()));
				entryViews.get(i-1).setEntry(thread.getEntries().get(i));
			}
		}
		
		for (EntryView view : entryViews) {
			view.setPadding(20, 0, 0, 0);
			this.addView(view);
		}
	}
	
	public Thread getExpandedThread() {
		return expandedThread;
	}
	
	public void setExpandedThread(Thread thread) {
		this.expandedThread = thread;
	}
	
	public Thread getThread() {
		return thread;
	}
}
