package com.GDT.sidm_2014; // Note: Differs with your project name

import android.R.string;
import android.app.Activity;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
//Implement this interface to receive information about changes to the surface.
	
public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

		private boolean pausepress=true;
		private GameThread myThread = null; // Thread to control the rendering
		private Objects PauseB1;
		private Objects PauseB2;
		private Bitmap[] star = new Bitmap[1];
		//Chatroom stuff
		private ChatRoom[] theChatRooms = new ChatRoom[4];
		private static long timeLastCheck = System.currentTimeMillis();	
		private int maxWarnings = 2;
		private int activeWarningRooms = 0;
		
		// 1) Variables used for background rendering 
		private Bitmap bg;
		private Bitmap scaleBg;
		private short bgX=0, bgY=0;
		int ScreenWidth ;
		int ScreenHeight ;
		int Scoreno =0;
		
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
			scaleBg= Bitmap.createScaledBitmap(bg, (int)(ScreenWidth),(int)(ScreenHeight), true);
			
			// Create the game loop thread
			
			PauseB1 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause1),200,300);
			PauseB2 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause),1,1);
			
			//Back = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.button_back),10,300);
			theChatRooms[0] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),1000,000);
			theChatRooms[1] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),1000,500);
			theChatRooms[2] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),500,000);
			theChatRooms[3] = new ChatRoom(BitmapFactory.decodeResource(getResources(), R.drawable.chatroom),500,500);
			star[0] = BitmapFactory.decodeResource(getResources(),R.drawable.star);

			myThread = new GameThread(getHolder(), this);
			
			// Make the GamePanel focusable so it can handle events
			setFocusable(true);
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
		public boolean CheckCollision(int x1, int y1 ,int w1,int h1, int x2, int y2, int w2, int h2)
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
			bgY-=8; // Change the number of panning speed if number is larger, it moves faster.
			if (bgY<-ScreenHeight) 
			{ // Check if reaches 1280, if does, set bgX = 0. 
				bgY=0; 
			}
			
			//try and activate chatrooms every second
			if(System.currentTimeMillis()-timeLastCheck > 1000){
				if(Scoreno<0)
				{
					Scoreno=0;
				}
				
				
				for(int i = 0; i < 4; ++i){
					//Only update rooms when there isn't a max number of active rooms
					if(activeWarningRooms < maxWarnings)
					{
						if(theChatRooms[i].TrySetActive()){
							//A chatroom becomes active
							++activeWarningRooms;
						}
					}
					
					
					if(theChatRooms[i].getWarning()==true)
					{
					if(System.currentTimeMillis()-(theChatRooms[i].getTimeActive())>3000)
						{
						theChatRooms[i].setWarning(false);
						Scoreno-=10;
						--activeWarningRooms;
						}
					}
				}
			}
			
			//Update chatrooms
			for(int i = 0; i < 4; ++i){
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
				
		// 8) Draw the spaceships
			/*
			canvas.drawBitmap(ship[shipIndex], mX, mY, null);
			canvas.drawBitmap(stone[shipIndex], aX, aY, null);
			*/
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
			/*P_sprite.draw(canvas);
			P_sprite.setY(600);
			*/
			
			//Draw chatrooms
			for(int i = 0; i < 4; ++i){
				theChatRooms[i].draw(canvas);
			}
		//	canvas.drawBitmap(Back.getBitmap(), 10, ScreenHeight-(ScreenHeight/4), null);
			
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event){
		
			int action = event.getAction();//check for action of touch
			short X = (short) event.getX(); 
			short Y = (short) event.getY(); 
			switch(action)
			{
			case MotionEvent.ACTION_DOWN:
				
				for(int i = 0; i < 4; ++i){
					Objects tempObj = theChatRooms[i].getObjects();
					if(CheckCollision(tempObj.getX(),tempObj.getY(),tempObj.getSpriteWidth(),tempObj.getSpriteHeight(), X,Y,0,0))
					{
						
						if(theChatRooms[i].getWarning())
						{
							Scoreno +=10;
							theChatRooms[i].setWarning(false);
							--activeWarningRooms;
						}
						else
						{
							Scoreno -=10;
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
			}
			
			return true;
		}
}