package com.GDT.sidm_2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainMenu extends Activity implements OnClickListener,SensorEventListener{
	public static MediaPlayer bgm;
	private MenuThread myThread = null;
	private SensorManager sensor;
	private Button btn_start;
	private Button btn_help;
	private Button btn_option;
	float aX=0, aY=0;
	private Bitmap bg ;
	public int bX = 0, bY=50;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensor = (SensorManager)
				getBaseContext().getSystemService(Context.SENSOR_SERVICE);
				sensor.registerListener(this,
				sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
				SensorManager.SENSOR_DELAY_NORMAL);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
		setContentView(R.layout.mainmenu);
		
		btn_start = (Button)findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		
		btn_help = (Button)findViewById(R.id.btn_help);
		btn_help.setOnClickListener(this);
		
		btn_option = (Button)findViewById(R.id.btn_option);
		btn_option.setOnClickListener(this);
		
		bgm = MediaPlayer.create(getBaseContext(), R.raw.friction);
		bgm.setVolume(Splashpage.BgmVolume, Splashpage.BgmVolume);
		bgm.start();
		
		bg = BitmapFactory.decodeResource(getResources(),R.drawable.backgnd);
	}
	public void surfaceCreated(SurfaceHolder holder){
		// Create the thread 
		if (!myThread.isAlive()){
			myThread = new MenuThread(holder, this);
			myThread.startRun(true);
			myThread.start();

		}
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder){
		// Destroy the thread 
		if (myThread.isAlive()){
			myThread.startRun(false);
		}
		boolean retry = true;
		while(retry){
			try{
				myThread.join();
				retry=false;
			}
			catch(InterruptedException e)
			{
				
			}
		}
	
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float [ ] SenseData = event.values;
		// Check X axis values i.e. 0 = X axis, 1 = Y axis.
		if(SenseData[0] >= 1){ // if positive x-data exceeds +1,
		aY+= 10; // object moves leftwards.

		}
		else if(SenseData[0] <= -1){ // if negative x-data exceeds -1,
		aY-= 10; // object moves rightwards.

		}
		// Check Y axis values
		if(SenseData[1] >= 1){
			aX += 10;

		}
		else if(SenseData[1] <= -1){
			
			aX -= 10;

		}
	}
	
	public void doDraw(Canvas canvas){
		if(canvas==null)
		{
			return;
		}
		
		canvas.drawBitmap(bg,aX+bX,aY+bY,null);

	}
	
	@Override
	public void onClick(View v){	//check which button is pressed
		Intent intent = new Intent();
		
		if(v == btn_start){
			intent.setClass(this, GamePage.class);
		}
		else if(v == btn_help){
			intent.setClass(this, HelpScreen.class);
		}
		else if(v == btn_option){
			intent.setClass(this, OptionScreen.class);
		}
		startActivity(intent);
		
	}
	
     public void onStop() {
         super.onStop();
         
     }
     
     public void onDestroy() {
         super.onDestroy();
         
     }



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
