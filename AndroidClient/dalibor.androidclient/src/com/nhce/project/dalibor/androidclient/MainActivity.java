package com.nhce.project.dalibor.androidclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etHost = (EditText) findViewById(R.id.editTextHost);
        final EditText etPort = (EditText) findViewById(R.id.editTextPort);
        final EditText etUsername = (EditText) findViewById(R.id.editTextUsername);
        final EditText etPassword = (EditText) findViewById(R.id.editTextPassword);
        
        Button btnTestConnection = (Button) findViewById(R.id.buttonTestConnection);
        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        
        btnTestConnection.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Feature not yet available", Toast.LENGTH_SHORT).show();
			}
		});
        
        btnLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String sHost = etHost.getText().toString();
				String sPort = etPort.getText().toString();
				String sUsername = etUsername.getText().toString();
				String sPassword = etPassword.getText().toString();
				HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(sHost+":"+sPort);
			    String sJsonData ="'{\"username\":\""+sUsername+"\",\"password\":\""+sPassword+"\"}'";

			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
