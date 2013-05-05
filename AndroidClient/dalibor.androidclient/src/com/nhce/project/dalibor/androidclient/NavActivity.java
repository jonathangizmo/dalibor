package com.nhce.project.dalibor.androidclient;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NavActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        final SharedPreferences sSettings = getSharedPreferences(Dalibor.PREFS_NAME, 0);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Toast.makeText(getApplicationContext(), mSensor.getVendor(), Toast.LENGTH_LONG).show();
        PollManager.startPoll();
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
		
		Button calibrateButton = (Button) findViewById(R.id.button1);
		calibrateButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Double latitude = Double.parseDouble(sSettings.getString("lat", "0.00"));
				Double longitude = Double.parseDouble(sSettings.getString("lon", "0.00"));
				int logtime = Integer.parseInt(sSettings.getString("logtime", "0"));
				if(latitude==0 && longitude==0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(NavActivity.this);
					builder.setMessage("Location unavailable at the moment").setTitle("Device Location");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               dialog.cancel();
				           }
				       });
					AlertDialog dialog = builder.create();
					dialog.show();
					return;
				}
				Log.d("Location time stamp", String.valueOf(logtime));
				String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(Device was last seen here)", latitude, longitude,latitude,longitude);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				startActivity(intent);
			}
		});
		
		Button forwardButton = (Button) findViewById(R.id.forwardButton);
		Button reverseButton = (Button) findViewById(R.id.backwardButton);
		
		forwardButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					DataHolder.setDirection(true);
					DataHolder.setPower(true);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					DataHolder.setPower(false);
				}
				return false;
			}
		});
		
		reverseButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					DataHolder.setDirection(false);
					DataHolder.setPower(true);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP) {
					DataHolder.setDirection(true);
					DataHolder.setPower(false);
				}
				return false;
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nav, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onDestroy() {
		PollManager.stopPoll();
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		PollManager.stopPoll();
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		PollManager.stopPoll();
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		PollManager.startPoll();
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		PollManager.startPoll();
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		PollManager.startPoll();
		super.onStart();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onSensorChanged(SensorEvent event) {

		  if((event.values[0])>4.5)
		  {
			  DataHolder.setTurnLeft(true);
			  ((TextView) findViewById(R.id.Direction)).setText("Left");
		  }
		  else if((event.values[0])<-4.5)
		  {
			  DataHolder.setTurnRight(true);
			  ((TextView) findViewById(R.id.Direction)).setText("Right");
		  }
		  else
		  {
			  DataHolder.setTurnLeft(false);
			  DataHolder.setTurnRight(false);
			  ((TextView) findViewById(R.id.Direction)).setText("Stright");
		  }
		
	}
}