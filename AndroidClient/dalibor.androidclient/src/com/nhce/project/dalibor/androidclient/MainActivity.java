package com.nhce.project.dalibor.androidclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
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
        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        
        SharedPreferences settings = getSharedPreferences(Dalibor.PREFS_NAME, 0);
        etHost.setText(settings.getString("host", ""));
        etPort.setText(settings.getString("port", ""));
        etUsername.setText(settings.getString("username", ""));
        etPassword.setText(settings.getString("password", ""));
        btnLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String sHost = etHost.getText().toString();
				String sPort = etPort.getText().toString();
				String sUsername = etUsername.getText().toString();
				String sPassword = etPassword.getText().toString();
				connectToServer(sHost,sPort,sUsername,sPassword);
				//Intent navIntent = new Intent(getApplicationContext(), NavActivity.class);
           	 	//startActivity(navIntent);
			}
		});
    }

    private void connectToServer(final String sHost, final String sPort, final String sUsername, final String sPassword) {
    	final ProgressDialog progress = ProgressDialog.show(MainActivity.this, "Trying to connect", "Please wait..");
    		Thread t = new Thread(){
    			public void run()
    			{
				Looper.prepare();
				HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                HttpResponse response;
                JSONObject json = new JSONObject();
                String URL ="";
                if(!sPort.equals(""))
               	 URL = sHost + ":" + sPort;
                else
               	 URL = String.valueOf(sHost);
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
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();
                        for (String line = null; (line = reader.readLine()) != null;) {
                            builder.append(line).append("\n");
                        }
                        JSONObject finalResult = new JSONObject(builder.toString());
                        Log.i("response", finalResult.toString());
                        if(finalResult.getString("auth").equalsIgnoreCase("true")){
	                       	SharedPreferences settings = getSharedPreferences(Dalibor.PREFS_NAME, 0);
	                        SharedPreferences.Editor editor = settings.edit();
	                        editor.putString("username", sUsername);
	                        editor.putString("password", sPassword);
	                        editor.putString("host", sHost);
	                        editor.putString("port", sPort);
	                        editor.putString("sessionkey", finalResult.getString("sessionkey"));
	                        editor.commit();
	                        progress.cancel();
	                       	Intent navIntent = new Intent(getApplicationContext(), NavActivity.class);
	                       	startActivity(navIntent);
                        }
                        else {
                        	progress.cancel();
                       	 	showErrorDialog("Invalid Username/Password");
                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    Log.e("Error", "Cannot Estabilish Connection");
                    progress.cancel();
                    showErrorDialog("Could not reach server");
                }
                Looper.loop();
			}
    	};
    	t.start();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
	
	private void showErrorDialog(String sMessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(sMessage)
		       .setTitle("Connecting to server");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.cancel();
	           }
	       });
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
