package com.example.quikchannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Scanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button directConnectButton;
	private Button browseBoardsButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        directConnectButton = (Button) findViewById(R.id.direct_connect_button);
        browseBoardsButton = (Button) findViewById(R.id.browse_boards_button);
        
        directConnectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(v.getContext(), BoardActivity.class);
//				InputStream stream = null;
//				stream = v.getContext().getResources().openRawResource(R.raw.betterexample);
//				Scanner s = new Scanner(stream).useDelimiter("\\A");
//				
//				i.putExtra("board_data", s.next());
//				s.close();
//				v.getContext().startActivity(i);
				
				Intent i = new Intent(v.getContext(), IpSelectActivity.class);
				v.getContext().startActivity(i);
			}
		});
    }
}
