package com.example.quikchannel;

import java.util.concurrent.ExecutionException;

import com.example.quikchannel.Views.BoardView;
import com.example.quikchannel.data.*;
import com.example.quikchannel.data.Thread;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BoardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_board);
//		RequestImageTask task = new RequestImageTask((ImageView) this.findViewById(R.id.imageView1), this);
//		task.execute(4);
//		RequestImageTask task2 = new RequestImageTask((ImageView) this.findViewById(R.id.imageView2), this);
//		task2.execute(0);
		
		onBoardLoaded(this.getIntent().getExtras().getString("board_data"));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_reply:
			Intent i = new Intent(this, MakeThreadActivity.class);
			this.startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onBoardLoaded(String sBoard) {
		JSONParser parser = new JSONParser();
		JSONObject jBoard = new JSONObject();
		try {
			jBoard = (JSONObject) parser.parse(sBoard);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Board board = new Board((String) jBoard.get("title"));
		JSONArray jThreads = (JSONArray) jBoard.get("threads");
		for (int i = 0; i < jThreads.size(); i++) {
			JSONObject jThread = (JSONObject) jThreads.get(i);
			jThread = (JSONObject) jThread;
			Thread thread = new Thread((String) jThread.get("title"));
			board.getThreads().add(thread);
			JSONArray jEntries = (JSONArray) jThread.get("entries");
			for (int j = 0; j < jEntries.size(); j++) {
				JSONObject jEntry = (JSONObject) jEntries.get(j);
				Entry entry = new Entry((String) jEntry.get("text"));
				entry.setAuthor((String)jEntry.get("author"));
				entry.setID(((Number) jEntry.get("id")).intValue());
				entry.setTime((String)jEntry.get("timestamp"));
				entry.setTitle((String)jEntry.get("title"));
				thread.getEntries().add(entry);
			}
		}
		((BoardView) this.findViewById(R.id.boardView1)).setBoard(board);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_and_reply, menu);
        return true;
    }
}