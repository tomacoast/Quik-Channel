package com.example.quikchannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.quikchannel.Views.BoardView;
import com.example.quikchannel.data.Board;
import com.example.quikchannel.data.Entry;
import com.example.quikchannel.data.Thread;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class RequestBoardTask extends AsyncTask<Void, Void, String> {
	
	private Activity context;
	private BoardView view;
	private String stringJson;
	private String ip;
	private int port;
	
	public RequestBoardTask(Activity context, BoardView view, String ip, int port) {
		// TODO Auto-generated constructor stub
		this.view = view;
		this.context = context;
		this.ip = ip;
		this.port = port;
		stringJson = "";
	}
	
	@Override
	protected String doInBackground(Void... params) {
		JSONObject requestObject = new JSONObject();
		requestObject.put("request", "board");
		try {
			//Socket s = new Socket("192.168.0.6", 42080);
			Socket s = new Socket(ip, port);
			s.getOutputStream().write(requestObject.toJSONString().getBytes());
			s.getOutputStream().write(255);
			s.getOutputStream().write(217);
			s.getOutputStream().flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String totalData = "";
			String line = reader.readLine();
			while (line != null) {
				Log.d("w", line);
				line = reader.readLine();
				totalData = totalData + line;
			}
			stringJson = totalData;
			s.close();
			context.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					onBoardLoaded(stringJson);
				}
			});
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		view.setBoard(board);
	}
}
