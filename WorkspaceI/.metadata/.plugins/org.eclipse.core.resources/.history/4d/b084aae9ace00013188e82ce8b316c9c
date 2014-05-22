package com.example.quikchannel;

import com.example.quikchannel.Views.BoardView;
import com.example.quikchannel.data.*;
import com.example.quikchannel.data.Thread;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.os.Bundle;

public class BoardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		JSONParser parser = new JSONParser();
		JSONObject jBoard = new JSONObject();
		try {
			jBoard = (JSONObject) parser.parse(this.getIntent().getExtras().getString("board_data"));
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
		
		
		this.setContentView(R.layout.activity_board);
		
		((BoardView) this.findViewById(R.id.boardView1)).setBoard(board);
//		((ThreadView) this.findViewById(R.id.threadView1)).setThread(board.getThreads().get(0));
//		((ThreadView) this.findViewById(R.id.threadView2)).setThread(board.getThreads().get(1));
	}
}