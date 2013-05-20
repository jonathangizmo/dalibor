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

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class PollManager 
{	
	private static boolean isStarted = false;
	
	public static void startPoll() {
		PollManager.isStarted = true;
		PollManager.poll();
		Log.d("poll", "starting poll");
	}
	
	public static void stopPoll() {
		PollManager.isStarted = false;
		Log.d("poll","stopping poll");
	}
	
	private static void poll() {
		class PollOperation extends AsyncTask<Void, Void, Void>
		{
			private static final String POLL_CONTROLLER = "/poll/";
			@Override
			protected Void doInBackground(Void... params) {
				Log.d("poll","polling");
				SharedPreferences settings = Dalibor.getAppContext().getSharedPreferences(Dalibor.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				final String sUsername = settings.getString("username", "");
				final String sHost = settings.getString("host", "");
				final String sPort = settings.getString("port", "");
				final String sSessionKey = settings.getString("sessionkey", "");
				HttpClient client = new DefaultHttpClient();
	            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	            HttpResponse response = null;
	            JSONObject json = new JSONObject();
	            
	            String URL = "";
	            if(!sPort.equals(""))
               	 URL = sHost + ":" + sPort;
                else
               	 URL = String.valueOf(sHost);
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
	                Log.i("poll direction, session",DataHolder.getDirectionString()+" " +sSessionKey);
	                Log.i("poll user", sUsername);
	                response = client.execute(post);

	                if(response!=null){
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	                    StringBuilder builder = new StringBuilder();
	                    for (String line = null; (line = reader.readLine()) != null;) {
	                        builder.append(line).append("\n");
	                    }
	                    JSONObject finalResult = new JSONObject(builder.toString());
	                    try {
							String sLat = finalResult.getString("lat");
							String sLon = finalResult.getString("lon");
							if(Double.parseDouble(sLat) !=0 && Double.parseDouble(sLon) !=0)
							{
								Log.d("poll", "Setting location");
								editor.putString("lat", finalResult.getString("lat"));
							    editor.putString("lon", finalResult.getString("lon"));
							    editor.putString("logtime", finalResult.getString("logtime"));
							    editor.commit();
							}
						} catch (Exception e) {
							Log.e("poll", "Location unavailable");
							e.printStackTrace();
						}
	                    
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
