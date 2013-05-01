package com.nhce.project.dalibor.androidclient;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NavActivity extends Activity implements SensorListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float calVal[] = {0,0,0};
	private float curValues[] ={0,0,0};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Toast.makeText(getApplicationContext(), mSensor.getVendor(), Toast.LENGTH_LONG).show();
        PollManager.startPoll();
		mSensorManager.registerListener(this, Sensor.TYPE_ACCELEROMETER, 2000);
		
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				calVal[0] = curValues[0];
				calVal[1] = curValues[1];
				calVal[2] = curValues[2];
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

	public void onAccuracyChanged(int arg0, int arg1) {

	}

	public void onSensorChanged(int sensor, float[] values) {
		  curValues[0] = values[0];
		  curValues[1] = values[1];
		  curValues[2] = values[2];
		  float alpha = (float) 0.8;
		  float gravity[] = {0,0,0};
		  float linear_acceleration[] = {0,0,0};
		  // Isolate the force of gravity with the low-pass filter.
		  gravity[0] = alpha * gravity[0] + (1 - alpha) * values[0];
		  gravity[1] = alpha * gravity[1] + (1 - alpha) * values[1];
		  gravity[2] = alpha * gravity[2] + (1 - alpha) * values[2];

		  // Remove the gravity contribution with the high-pass filter.
		  linear_acceleration[0] = values[0] - gravity[0];
		  linear_acceleration[1] = values[1] - gravity[1];
		  linear_acceleration[2] = values[2] - gravity[2];
		  ((TextView) findViewById(R.id.tvx)).setText(String.format("%.0f", linear_acceleration[0]));
		  ((TextView) findViewById(R.id.tvy)).setText(String.format("%.0f", linear_acceleration[1]));
		  ((TextView) findViewById(R.id.tvz)).setText(String.format("%.0f", linear_acceleration[2]));
		  if((Math.abs(calVal[1]-curValues[1])>40) && (Math.abs(calVal[2]-curValues[2])>40))
		  {
			  calVal[0] = curValues[0];
			  calVal[1] = curValues[1];;
			  calVal[2] = curValues[2];;
		  }
		  if((curValues[0]-calVal[0])<-20)
		  {
			  DataHolder.setTurnLeft(true);
			  ((TextView) findViewById(R.id.textView2)).setText("Left");
		  }
		  else if((curValues[0]-calVal[0])>20)
		  {
			  DataHolder.setTurnRight(true);
			  ((TextView) findViewById(R.id.textView2)).setText("Right");
		  }
		  else
		  {
			  DataHolder.setTurnLeft(false);
			  DataHolder.setTurnRight(false);
			  ((TextView) findViewById(R.id.textView2)).setText("Stright");
		  }
		  
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
}