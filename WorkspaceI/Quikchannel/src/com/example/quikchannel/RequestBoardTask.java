package com.example.quikchannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class RequestBoardTask extends AsyncTask<Void, Void, String> {
	@Override
	protected String doInBackground(Void... params) {
		JSONObject requestObject = new JSONObject();
		requestObject.put("request", "board");
		try {
			Socket s = new Socket("192.168.0.6", 42080);
			s.getOutputStream().write(requestObject.toJSONString().getBytes());
			s.getOutputStream().write('\n');
			s.getOutputStream().flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				Log.d("w", line);
				line = reader.readLine();
			}
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
