package com.GDT.sidm_2014;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainMenu extends SurfaceView implements OnClickListener,SensorEventListener, SurfaceHolder.Callback{
	public static MediaPlayer bgm;
	private MenuThread myThread = null;
	private SensorManager sensor;
	private Button btn_start;
	private Button btn_help;
	private Button btn_option;
	float aX=0, aY=0;
	private Bitmap bg ;
	public int bX = 0, bY=0;
	int ScreenWidth ;
	int ScreenHeight ;
	private Objects[] Buttons = new Objects[3];
	
	public MainMenu(Context context, Activity activity) {
		super(context);
		sensor = (SensorManager)
				getContext().getSystemService(Context.SENSOR_SERVICE);
				sensor.registerListener(this,
				sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
				SensorManager.SENSOR_DELAY_NORMAL);
				DisplayMetrics metrics= context.getResources().getDisplayMetrics();
				ScreenWidth = metrics.widthPixels;
				ScreenHeight = metrics.heightPixels;		

		Buttons[0] = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.button_start), (int) ((ScreenWidth/2)*(0.8)), ((ScreenHeight/4)*3));
		Buttons[1] = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.button_options), (int) ((ScreenWidth/4)*(0.01)), (int) ((ScreenHeight/4)*2.75));
		Buttons[2] = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.button_help), ((ScreenWidth/4)*3), ((ScreenHeight/4)*3));
		
		btn_start = (Button)findViewById(R.id.btn_start);
	//	btn_start.setOnClickListener(this);
		
		btn_help = (Button)findViewById(R.id.btn_help);
		//btn_help.setOnClickListener(this);
		
		btn_option = (Button)findViewById(R.id.btn_option);
		//btn_option.setOnClickListener(this);
		
		bgm = MediaPlayer.create(getContext(), R.raw.friction);
		bgm.setVolume(Splashpage.BgmVolume, Splashpage.BgmVolume);
		bgm.start();
		getHolder().addCallback(this);
		myThread = new MenuThread(getHolder(), this);
		bg = BitmapFactory.decodeResource(getResources(),R.drawable.backgnd);
		// To track an activity
		MenuPage.activityTracker = activity; 
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		
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
	public void displaytext(Canvas canvas, String string, int Mode)
	{
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(100);
		paint.setTextSize(30);

		canvas.drawText(string +" " +bX+aX, ScreenWidth/2, 50, paint);
		canvas.drawText(string +" " +bY+aY, ScreenWidth/2, 80, paint);
	
	}
	public void Update()
	{
		if(aX<=-160)
		{
			aX=-160;
		}
		if(aX>=160)
		{
			aX=160;
		}
		if(aY<-130)
		{
			aY=-130;
		}
		if(aY>=130)
		{
			aY=130;
		}
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float [ ] SenseData = event.values;
		// Check X axis values i.e. 0 = X axis, 1 = Y axis.
		if(SenseData[0] >= 1){ // if positive x-data exceeds +1,
		aY+= 5; // object moves leftwards.

		}
		else if(SenseData[0] <= -1){ // if negative x-data exceeds -1,
		aY-= 5; // object moves rightwards.

		}
		// Check Y axis values
		if(SenseData[1] >= 1){
			aX += 5;

		}
		else if(SenseData[1] <= -1){
			
			aX -= 5;

		}
	}
	
	public void doDraw(Canvas canvas){
		if(canvas==null)
		{
			return;
		}
		
		canvas.drawBitmap(bg,aX+bX,aY+bY,null);
		displaytext(canvas, "Tada", 1);
		for(int i = 0; i < 3; ++i){

			Buttons[i].draw(canvas);
		}
	}
	
	@Override
	public void onClick(View v){	//check which button is pressed
		Intent intent = new Intent();
		
        
		
	    
		if(v == btn_start){
			intent.setClass(getContext(), GamePage.class);
			
		}
		else if(v == btn_help){
			intent.setClass(getContext(), HelpScreen.class);
		}
		else if(v == btn_option){
			intent.setClass(getContext(), OptionScreen.class);
		}
		MenuPage.activityTracker.startActivity(intent);
		
	}
	
	public boolean onTouchEvent(MotionEvent event){
		Intent intent = new Intent();
		
		int action = event.getAction();//check for action of touch
		short X = (short) event.getX(); 
		short Y = (short) event.getY(); 
		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
			

			if(GamePanelSurfaceView.CheckCollision(Buttons[0].getX(), Buttons[0].getY(), Buttons[0].getSpriteWidth(), Buttons[0].getSpriteHeight(), X,Y,0,0))
			{
				intent.setClass(getContext(), GamePage.class);
			}
			if(GamePanelSurfaceView.CheckCollision(Buttons[1].getX(), Buttons[1].getY(), Buttons[1].getSpriteWidth(), Buttons[1].getSpriteHeight(), X,Y,0,0))
			{
				intent.setClass(getContext(), OptionScreen.class);
			}
			if(GamePanelSurfaceView.CheckCollision(Buttons[2].getX(), Buttons[2].getY(), Buttons[2].getSpriteWidth(), Buttons[2].getSpriteHeight(), X,Y,0,0))
			{
				intent.setClass(getContext(), HelpScreen.class);
			}
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{ 
			//check if the image is clicked on
			}
			MenuPage.activityTracker.startActivity(intent);
			
				break;
			
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			//stopVibrate();
			break;
		}
		
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
