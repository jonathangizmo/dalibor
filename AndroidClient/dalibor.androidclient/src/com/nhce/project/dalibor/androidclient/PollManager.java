package com.nhce.project.dalibor.androidclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

public class PollManager 
{	
	private static boolean isStarted = false;
	
	public static void startPoll() {
		PollManager.isStarted = true;
		PollManager.poll();
	}
	
	public static void stopPoll() {
		PollManager.isStarted = false;
	}
	
	private static void poll() {
		class PollOperation extends AsyncTask<Void, Void, Void>
		{
			private static final String POLL_CONTROLLER = "/poll/";
			@Override
			protected Void doInBackground(Void... params) {
				SharedPreferences settings = Dalibor.getAppContext().getSharedPreferences(Dalibor.PREFS_NAME, 0);
				final String sUsername = settings.getString("username", "");
				final String sHost = settings.getString("host", "");
				final String sPort = settings.getString("port", "");
				final String sSessionKey = settings.getString("sessionkey", "");
				HttpClient client = new DefaultHttpClient();
	            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	            HttpResponse response = null;
	            JSONObject json = new JSONObject();
	            String URL = sHost + ":" + sPort;
	            URL+=POLL_CONTROLLER;

	            try {
	                HttpPost post = new HttpPost(URL);
	                json.put("sessionkey", sSessionKey);
	                json.put("username", sUsername);
	                json.put("message", DataHolder.getMessage());
	                json.put("move", DataHolder.getDirectionString());
	                StringEntity se = new StringEntity( json.toString());  
	                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	                post.setEntity(se);
	                Log.i("poll direction",DataHolder.getDirectionString());
	                response = client.execute(post);

	                if(response!=null){
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	                    StringBuilder builder = new StringBuilder();
	                    for (String line = null; (line = reader.readLine()) != null;) {
	                        builder.append(line).append("\n");
	                    }
	                    JSONObject finalResult = new JSONObject(builder.toString());
	                    Log.i("response", finalResult.toString());
	                }

	            } catch(Exception e) {
	                e.printStackTrace();
	                Log.e("Error", "Cannot Poll");
	            }
	            if(PollManager.isStarted) {
	    			try {
	    				Thread.sleep(500);
	    			} catch (InterruptedException e) {
	    				Log.e("Thread error", e.getMessage());
	    			}
	    			new PollOperation().execute();
	    		}
	            return null;
			}
		}
			
		new PollOperation().execute();		
		
		return;
	}
}
