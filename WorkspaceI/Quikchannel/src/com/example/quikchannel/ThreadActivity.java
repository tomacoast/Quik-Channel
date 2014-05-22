package com.example.quikchannel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.quikchannel.Views.ThreadView;
import com.example.quikchannel.data.Thread;

public class ThreadActivity extends Activity {
	
	public Thread t;
	private String ip;
	private int port;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.ip = this.getIntent().getExtras().getString("IP");
		this.port = this.getIntent().getExtras().getInt("PORT");
		this.setContentView(R.layout.activity_thread);
		t = (Thread) this.getIntent().getSerializableExtra("thread");
		((ThreadView) this.findViewById(R.id.threadView1)).setThread(t);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_reply:
			Intent i = new Intent(this, MakePostActivity.class);
			i.putExtra("threadid", ((ThreadActivity) this).t.getEntries().get(0).getID());
			i.putExtra("PORT", port);
			i.putExtra("IP", ip);
			this.startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.reply, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
