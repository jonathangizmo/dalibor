package com.nhce.project.dalibor.androidclient;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String USER_CONTROLLER = "/user/";
	
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
				connectToServer(sHost,sPort,sUsername,sPassword);
			}
		});
    }

    protected void connectToServer(final String sHost, final String sPort, final String sUsername, final String sPassword) {
    	 Thread t = new Thread() {

             public void run() {
                 Looper.prepare();
                 HttpClient client = new DefaultHttpClient();
                 HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                 HttpResponse response;
                 JSONObject json = new JSONObject();
                 String URL = sHost + ":" + sPort;
                 URL+=USER_CONTROLLER;

                 try {
                     HttpPost post = new HttpPost(URL);
                     json.put("username", sUsername);
                     json.put("password", sPassword);
                     
                     StringEntity se = new StringEntity( json.toString());  
                     se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                     post.setEntity(se);
                     response = client.execute(post);

                     if(response!=null){
                         InputStream in = response.getEntity().getContent();
                         Log.i("response", in.toString());
                         Toast.makeText(getApplicationContext(), "Recieved response", Toast.LENGTH_LONG).show();
                     }

                 } catch(Exception e) {
                     e.printStackTrace();
                     Log.e("Error", "Cannot Estabilish Connection");
                 }

                 Looper.loop();
             }
         };

         t.start();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
