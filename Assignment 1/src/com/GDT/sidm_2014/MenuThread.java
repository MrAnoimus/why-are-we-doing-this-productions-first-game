package com.GDT.sidm_2014; // Note: Differs with your project name
import java.util.Objects;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

public class MenuThread extends Thread {
	// The actual view that handles inputs and draws to the surface
			public static MainMenu menuView;
			// Surface holder that can access the physical surface
			private SurfaceHolder holder;
			
			// Flag to hold game state 
			private boolean isRun;
			private boolean isPause = false;
			long curr = 0;
			long prev = 0;
			short fps = 20;
			
			// Constructor for this class 
			public MenuThread(SurfaceHolder holder, MainMenu myView){
				super();
				isRun =true; //for running
				this.menuView = myView;
				this.holder = holder;
				
			}
		
			public void pause(){
				synchronized(holder)
				{
					isPause=true;
				}
			}
			public void unPause()
			{
				synchronized(holder)
				{
					isPause=false;
					holder.notifyAll();
				}
			}
			public boolean getPause()
			{
				return isPause;
			}
			public void startRun(boolean r){
				isRun = r;
			}
				
			@Override
			public void run(){
				while (isRun){  
				//Update game state and render state to the screen
					Canvas c = null;
					curr = System.currentTimeMillis();
					if ((long)(curr-prev)>(long)(1000/fps)){
						try {
							c = holder.lockCanvas();
							synchronized(holder){		

								if(menuView!=null)
								{
									if(getPause()==false)
									{
										menuView.Update();
										menuView.doDraw(c); 
									}
								}
							}
							synchronized(holder)
							{
								while(getPause()==true)
								{
									try {
										holder.wait();
										
									}
									catch(InterruptedException e)
									{
										
									}
								}
							}
						}
						finally{
							if (c!=null){
								holder.unlockCanvasAndPost(c);
							}
						}
						prev = curr;
					}
					else{
						try{
							Thread.sleep((long)((1000/fps)-prev));
						}
						catch (Exception e){
							
						}
					}
				}

			}
}
