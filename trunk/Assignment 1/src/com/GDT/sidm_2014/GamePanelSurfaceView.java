package com.GDT.sidm_2014; // Note: Differs with your project name

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;

import java.io.FileNotFoundException;
import java.util.Random;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//Implement this interface to receive information about changes to the surface.
	
public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback, OnSeekBarChangeListener{
		boolean gameOver=false;
		public int noOfRooms;
		public int health =3;
		public int aX = 80, aY=80;
		public int bX = 0, bY=50;
		private SoundPool sounds;
		private int soundcorrect, soundwrong, soundbonus;
		
		public Vibrator v;
		
		//private boolean pausepress=true;
		private GameThread myThread = null; // Thread to control the rendering
		//private Objects PauseB1;
		//private Objects PauseB2;
		private Bitmap[] star = new Bitmap[2];
		private Bitmap[] healthicon = new Bitmap[3];
		//Chatroom stuff
		private ChatRoom[] theChatRooms = new ChatRoom[4];
		private static long timeLastCheck = System.currentTimeMillis();	
		private int maxWarnings = 2;
		private int activeWarningRooms = 0;
		
		// 1) Variables used for background rendering 
		private Bitmap bg;
		//private Bitmap scaleBg;
		private short bgX=0, bgY=0;
		int ScreenWidth ;
		int ScreenHeight ;
		int Scoreno = 0;
		
		AlertDialog.Builder alert = null;
		Activity activityTracker2;
		
		//For chatbox verification
		private Objects tick, cross;
		private int OpenedChatRoomID = -1;
		
		//Conversation messages for chatboxes
		private ConversationTextManager ConvoTextMgr;
		
		//constructor for this GamePanelSurfaceView class
		public GamePanelSurfaceView (Context context){
			// Context is the current state of the application/object
			super(context);
			
			//set things to get screen size
			DisplayMetrics metrics= context.getResources().getDisplayMetrics();
			ScreenWidth = metrics.widthPixels;
			ScreenHeight = metrics.heightPixels;			
			
			// Adding the callback (this) to the surface holder to intercept events
			getHolder().addCallback(this);
			
			// 2)load the image when this class is being instantiated
			bg = BitmapFactory.decodeResource(getResources(),R.drawable.help2);
			//scaleBg= Bitmap.createScaledBitmap(bg, (int)(ScreenWidth),(int)(ScreenHeight), true);
			
			// Create the game loop thread
			
			//PauseB1 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause1),200,300);
			//PauseB2 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause),1,1);
			
			//Back = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.button_back),10,300);
			theChatRooms[0] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),500,500, BitmapFactory.decodeResource(getResources(), R.drawable.chatbubble), 200, 650);
			theChatRooms[1] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),1000,500, BitmapFactory.decodeResource(getResources(), R.drawable.chatbubble), 200, 650);
			theChatRooms[2] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),500,000, BitmapFactory.decodeResource(getResources(), R.drawable.chatbubble), 200, 650);
			theChatRooms[3] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),1000,000, BitmapFactory.decodeResource(getResources(), R.drawable.chatbubble), 200, 650);
			star[0] = BitmapFactory.decodeResource(getResources(),R.drawable.star);
			star[1] = BitmapFactory.decodeResource(getResources(),R.drawable.star);
			healthicon[0] = BitmapFactory.decodeResource(getResources(),R.drawable.health);
			healthicon[1] = BitmapFactory.decodeResource(getResources(),R.drawable.health);
			healthicon[2] = BitmapFactory.decodeResource(getResources(),R.drawable.health);

			tick = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.tick), 500, 775);
			cross = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.cross), 1000, 775);
			
			myThread = new GameThread(getHolder(), this);
			
			
			sounds = new SoundPool(10,AudioManager.STREAM_MUSIC,0);
			soundcorrect = sounds.load(context,  R.raw.correct,1);
			soundwrong = sounds.load(context,R.raw.incorrect, 1);
			// Make the GamePanel focusable so it can handle events
			setFocusable(true);
			
			if(OptionScreen.getDifficulty() == 1)
			{
				maxWarnings = 1;
				noOfRooms=2;
			}
			else
			{
				maxWarnings = 2;
				noOfRooms=4;
			}
			
			ConvoTextMgr = new ConversationTextManager(context);
		}

		public void alertdialog()
		{
			alert = new AlertDialog.Builder(getContext());

			alert.setMessage("Score: "+ Scoreno);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent intent = new Intent();
		            
	                intent.putExtra("Highscore", Scoreno);
		            
		            ((Activity) getContext()).setResult(Activity.RESULT_OK, intent);
		            intent.setClass(getContext(), MenuPage.class);
		            ((Activity)getContext()).startActivityForResult(intent, 1);
		            ((Activity)getContext()).finish();
					// TODO Auto-generated method stub
					
					
					activityTracker2.startActivity(intent);
				}
			});
			
		}
		//must implement inherited abstract methods
		public void surfaceCreated(SurfaceHolder holder){
			// Create the thread 
			if (!myThread.isAlive()){
				myThread = new GameThread(getHolder(), this);
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
			
			sounds.unload(soundcorrect);
			sounds.unload(soundwrong);
			sounds.release();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

		}
		public void displaytext(Canvas canvas, String string, int Mode)
		{
			Paint paint = new Paint();
			paint.setARGB(255, 255, 255, 255);
			paint.setStrokeWidth(100);
			paint.setTextSize(30);
			if(Mode==1)
			{
			canvas.drawText(string +" " +Scoreno, ScreenWidth/2, 50, paint);
			}
			else
			{
				canvas.drawText(string +" " +Scoreno, ScreenWidth/2, 50, paint);
				canvas.drawText("Give A for Assignment Plox", ScreenWidth/2, 150, paint);
			}
		}
		public static boolean CheckCollision(int x1, int y1 ,int w1,int h1, int x2, int y2, int w2, int h2)
		{
			if(x2>=x1 && x2<=x1+w1) //start to detect collision of the top left corner
			{
				if(y2>=y1 && y2<=y1+h1) //comparing yellow box to blue box
				{
					return true;
				}
			}
			if(x2+w2>=x1&&x2+w2<=x1+w1)//top right corner
			{
				if(y2>=y1&&y2<=y1+h1)
				{
					return true;
				}
			}
			if(x2>=x1&&x2<x1+w1) // bottom left
			{
				if(y2+h2>y1&&y2+h2<y1+h1)
				{
					return true;
				}
			}
			if(x2+w2 >=x1 && x2+w2<x1+w1)//bottom right
			{
				if(y2+h2>y1&&y2+h2<y1+h1)
				{
					return true;
				}
			}
			return false;
		}
		
		public void update(){
			alertdialog();
			bgY-=8; // Change the number of panning speed if number is larger, it moves faster.
			if (bgY<-ScreenHeight) 
			{ // Check if reaches 1280, if does, set bgX = 0. 
				bgY=0; 
			}
			
			if(Scoreno<0)
			{
				Scoreno=0;
			}
			
			Random r = new Random();
			for(int i = 0; i < noOfRooms; ++i){
				//try and activate chatrooms every second
				if(System.currentTimeMillis()-timeLastCheck > 1000){
					//Only update rooms when there isn't a max number of active rooms
					if(activeWarningRooms < maxWarnings)
					{
						if(!theChatRooms[i].getWarning()){
							//30% to get scam text
							if (r.nextInt(100) < 30){
								theChatRooms[i].setText(ConvoTextMgr.GetScamConversationText());
								theChatRooms[i].Activate();
								//A chatroom becomes active
								++activeWarningRooms;
							}
							//Else try to get false warning
							else{
								theChatRooms[i].setText(ConvoTextMgr.GetConversationText());
								
								if(theChatRooms[i].TrySetActive()){
									//A chatroom becomes active
									++activeWarningRooms;
								}
							}
						}
					}
					else{
						//Do not allow chatrooms with warning to get more text
						if(!theChatRooms[i].getWarning())
							theChatRooms[i].setText(ConvoTextMgr.GetConversationText());
					}
				}
				
				//If not tapped within 3s, score-10
				if(theChatRooms[i].getWarning()==true && OpenedChatRoomID != i)
				{
					if(System.currentTimeMillis()-(theChatRooms[i].getTimeActive())>3000)
					{
						theChatRooms[i].setWarning(false);
						Scoreno-=10;
						--activeWarningRooms;
						Toast.makeText(getContext(), "Gameover", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			//Update timeLastCheck
			if(System.currentTimeMillis()-timeLastCheck > 1000)
				timeLastCheck = System.currentTimeMillis();
			
			//Update chatrooms
			for(int i = 0; i < noOfRooms; ++i){
				theChatRooms[i].update();
			}
			
		}
				
		
		// Rendering is done on Canvas
		public void doDraw(Canvas canvas){

			
			if(canvas==null)
			{
				return;
			}

				canvas.drawBitmap(bg,bgX,bgY,null);
				canvas.drawBitmap(bg,bgX,bgY+ScreenHeight,null);
				
			/*
			canvas.drawBitmap(ship[shipIndex], mX, mY, null);
			canvas.drawBitmap(stone[shipIndex], aX, aY, null);
			*/
				
			//Draw ChatRoom's chatbox if opened
			if(OpenedChatRoomID > -1){
				theChatRooms[OpenedChatRoomID].getChatBox().draw(canvas);
				
				canvas.drawBitmap(tick.getBitmap(), tick.getX(), tick.getY(), null);
				canvas.drawBitmap(cross.getBitmap(), cross.getX(), cross.getY(), null);
			}
			//Else draw main game screen
			else{
				if(health>=3)
				{
					canvas.drawBitmap(healthicon[0], (ScreenWidth/2), 50,null);
					canvas.drawBitmap(healthicon[1], (ScreenWidth/2)+healthicon[0].getWidth(), 50,null);
					canvas.drawBitmap(healthicon[2], (ScreenWidth/2)+healthicon[0].getWidth()+healthicon[0].getWidth(), 50,null);
					
				}
				if(health>=2)
				{
					canvas.drawBitmap(healthicon[0], (ScreenWidth/2), 50,null);
					canvas.drawBitmap(healthicon[1], (ScreenWidth/2)+healthicon[0].getWidth(), 50,null);
					
					
				}
				if(health>=1)
				{
					canvas.drawBitmap(healthicon[0], (ScreenWidth/2), 50,null);
				}
				if(Scoreno>=300)
				{
					displaytext(canvas, "Score", 2);
					canvas.drawBitmap(star[0], (ScreenWidth/2)+28, 50,null);
					canvas.drawBitmap(star[0], (ScreenWidth/2)+58, 50,null);
					canvas.drawBitmap(star[0], (ScreenWidth/2)+88, 50,null);
					
				}
				if(Scoreno>=200)
				{
					
					displaytext(canvas, "Score", 1);
					
					canvas.drawBitmap(star[0], (ScreenWidth/2)+28, 50,null);
					canvas.drawBitmap(star[0], (ScreenWidth/2)+58, 50,null);
					
				}
				if(Scoreno>=100)
				{
					displaytext(canvas, "Score", 1);
					
					canvas.drawBitmap(star[0], (ScreenWidth/2)+28, 50,null);
			
					
				}
				if(Scoreno>=0)
				{
					displaytext(canvas, "Score", 1);
				}
				
				
				//Draw chatrooms
				for(int i = 0; i < noOfRooms; ++i){
					theChatRooms[i].draw(canvas);
				}
			}
		//	canvas.drawBitmap(Back.getBitmap(), 10, ScreenHeight-(ScreenHeight/4), null);
			
		}
		public void startvibrate()
		{
			long pattern[]={0,100,000};
			v = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(pattern,-1);
			
		}
		public void stopVibrate(){
			v.cancel();
		}
		@Override
		public boolean onTouchEvent(MotionEvent event){
		
			int action = event.getAction();//check for action of touch
			short X = (short) event.getX(); 
			short Y = (short) event.getY(); 
			switch(action)
			{
			case MotionEvent.ACTION_DOWN:
				
				//Detect touch for 'tick' and 'cross' buttons if chatroom is opened
				if(OpenedChatRoomID > -1){
					//Tick button
					if(CheckCollision(tick.getX(), tick.getY(), tick.getSpriteWidth(), tick.getSpriteHeight(), X,Y,0,0)){
						if(theChatRooms[OpenedChatRoomID].getChatBox().getScam()){
							sounds.play(soundcorrect, 1.0f, 1.0f, 0, 0, 1.5f);
							Scoreno += 10;
							Toast.makeText(getContext(), "Right Choice, +10 Score", Toast.LENGTH_SHORT).show();
							
						}
						else{
							Scoreno -= 10;
							health-=1;
							Toast.makeText(getContext(), "Wrong Choice, -10 Score! Minus 1 Heath!", Toast.LENGTH_SHORT).show();
							if(health<=0&&gameOver==false)
							{
								gameOver=true;
								alert.show();
							}
						}
						theChatRooms[OpenedChatRoomID].setWarning(false);
						--activeWarningRooms;
						theChatRooms[OpenedChatRoomID].Deactivate();
						OpenedChatRoomID = -1;
					}
					//Cross button
					else if(CheckCollision(cross.getX(), cross.getY(), cross.getSpriteWidth(), cross.getSpriteHeight(), X,Y,0,0)){
						if(!theChatRooms[OpenedChatRoomID].getChatBox().getScam()){
							sounds.play(soundcorrect, 1.0f, 1.0f, 0, 0, 1.5f);
							Scoreno += 10;
							Toast.makeText(getContext(), "Right Choice, +10 Score", Toast.LENGTH_SHORT).show();
						}
						else{
							Scoreno -= 10;
							health-=1;
							Toast.makeText(getContext(), "Wrong Choice, -10 Score! Minus 1 Heath!", Toast.LENGTH_SHORT).show();
							if(health<=0&&gameOver==false)
							{
								alert.show();
							}
						}
						theChatRooms[OpenedChatRoomID].setWarning(false);
						--activeWarningRooms;
						theChatRooms[OpenedChatRoomID].Deactivate();
						OpenedChatRoomID = -1;
					}
				}
				//Else detect touch for chatrooms
				else{
					for(int i = 0; i < noOfRooms; ++i){
						Objects tempObj = theChatRooms[i].getObjects();
						if(CheckCollision(tempObj.getX(),tempObj.getY(),tempObj.getSpriteWidth(),tempObj.getSpriteHeight(), X,Y,0,0))
						{
							
							if(theChatRooms[i].getWarning())
							{
								sounds.play(soundcorrect, 1.0f, 1.0f, 0, 0, 1.5f);
								startvibrate();
								
								OpenedChatRoomID = i;
							}
							else
							{
								//sounds.play(soundwrong, 1.0f, 1.0f, 0, 0, 1.5f);
								Scoreno -=10;
							}
						}
					}
				}
				
				
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{ 
				//check if the image is clicked on
				}
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
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		
}