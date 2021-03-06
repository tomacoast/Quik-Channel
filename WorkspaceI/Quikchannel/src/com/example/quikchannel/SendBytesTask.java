package com.example.quikchannel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class SendBytesTask extends AsyncTask<byte[], Void, Void> {
	
	private String ip;
	private int port;
	
	public SendBytesTask(String ip, int port) {
		this.port = port;
		this.ip = ip;
	}
	
	@Override
	protected Void doInBackground(byte[]... arg0) {
		byte[] data = arg0[0];
		Socket s = null;
		try {
			//s = new Socket("192.168.0.6", 42080);
			s = new Socket(ip, port);
			s.getOutputStream().write(data);
			s.getOutputStream().flush();
			s.close();
			Log.d("Sending bytes", "doneskies");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.d("MakePostActivity", data);
		return null;
	}
}
