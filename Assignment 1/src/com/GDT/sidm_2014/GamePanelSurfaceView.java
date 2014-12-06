package com.GDT.sidm_2014; // Note: Differs with your project name

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	// Implement this interface to receive information about changes to the surface.
	
		private GameThread myThread = null; // Thread to control the rendering
		
		// 1) Variables used for background rendering 
		private Bitmap bg;
		private Bitmap scaleBg;
		private short bgX=0, bgY=0;
		private short mX = 0, mY = 0;
		int ScreenWidth ;
		int ScreenHeight ;
		int aX;
		int aY;
		int Scoreno;
		int hit=3;
		// 5) bitmap array to stores 4 images of the spaceship
		private Bitmap[ ] star = new Bitmap[1];
		private Bitmap[ ] ship = new Bitmap[4];
		private Bitmap[ ] stone = new Bitmap[12];
		// 6) Variable as an index to keep track of the spaceship images
		private short shipIndex = 0;
		private short stoneIndex = 0;
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
			bg = BitmapFactory.decodeResource(getResources(),R.drawable.gamescene);
			scaleBg= Bitmap.createScaledBitmap(bg, (int)(ScreenWidth),(int)(ScreenHeight), true);
			// 7) Load the images of the spaceships
			
			ship[0] = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_1); 
			ship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2); 
			ship[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3); 
			ship[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);
			stone[0] = BitmapFactory.decodeResource(getResources(),R.drawable.asteroid01); 
			stone[1] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid02); 
			stone[2] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid03); 
			stone[3] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid04);
			stone[4] = BitmapFactory.decodeResource(getResources(),R.drawable.asteroid05); 
			stone[5] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid06); 
			stone[6] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid07); 
			stone[7] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid08);
			stone[8] = BitmapFactory.decodeResource(getResources(),R.drawable.asteroid09); 
			stone[9] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid10); 
			stone[10] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid11); 
			stone[11] = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid12);
			star[0] = BitmapFactory.decodeResource(getResources(),R.drawable.star); 
		
			// Create the game loop thread
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
		public void displaytext(Canvas canvas)
		{
			Paint paint = new Paint();
			paint.setARGB(255, 0, 0, 0);
			paint.setStrokeWidth(100);
			paint.setTextSize(30);
			
			canvas.drawText("Score:"+" " +Scoreno, 600, 50, paint);
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
		    // 4) An update function to update the game 
			
			bgX-=8; // Change the number of panning speed if number is larger, it moves faster.
			if (bgX<-1280) 
			{ // Check if reaches 1280, if does, set bgX = 0. 
				bgX=0; 
				}
			shipIndex++; 
			shipIndex%=4;
			stoneIndex++; 
			stoneIndex%=12;
			
			}
		    // 9) Update the spaceship images / shipIndex so that the animation will occur.
				
		
		// Rendering is done on Canvas
		public void doDraw(Canvas canvas){

			
			if(canvas==null)
			{
				return;
			}
			
		// 3) Re-draw 2nd image after the 1st image ends	

				canvas.drawBitmap(bg,bgX,bgY,null);
				canvas.drawBitmap(bg,bgX+1920,bgY,null);
	
		
		// 8) Draw the spaceships
			
			canvas.drawBitmap(ship[shipIndex], mX, mY, null);
			canvas.drawBitmap(stone[shipIndex], aX, aY, null);
			
			if(hit==3)
			{
				canvas.drawBitmap(star[0], 28, 10,null);
				canvas.drawBitmap(star[0], 58, 10,null);
				canvas.drawBitmap(star[0], 88, 10,null);
			}
			if(hit==2)
			{
				canvas.drawBitmap(star[0], 28, 10,null);
				canvas.drawBitmap(star[0], 58, 10,null);
				
			}
			if(hit==1)
			{
				canvas.drawBitmap(star[0], 28, 10,null);
		
				
			}
			displaytext(canvas);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event){
		
		// 10) In event of touch on screen, the spaceship will relocate to the point of touch
			int action = event.getAction();//check for action of touch
			short X = (short) event.getX(); 
			short Y = (short) event.getY(); 
			switch(action)
			{
			case MotionEvent.ACTION_DOWN:
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{ 
				//check if the image is clicked on
				}
					break;
				
			case MotionEvent.ACTION_MOVE:
				// New location where the image to land on 
					mX = (short)(X - ship[shipIndex].getWidth()/2); 
					mY = (short)(Y - ship[shipIndex].getHeight()/2); 
					if(CheckCollision(mX,mY, ship[shipIndex].getWidth(),ship[shipIndex].getHeight(),aX,aY,stone[stoneIndex].getWidth(),stone[stoneIndex].getHeight()))
					{
						hit--;
						Scoreno +=10;
						Random r= new Random();
						aX= r.nextInt(ScreenWidth);
						aY = r.nextInt(ScreenHeight);
					}
			//manage move
				break;
			}

			//In the event to locate the location where touch action occurs 
			
			
			
			//check if ship and stone collide
			
			return true;
		}
}
