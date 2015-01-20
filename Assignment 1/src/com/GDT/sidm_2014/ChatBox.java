package com.GDT.sidm_2014;

import java.util.Random;

import android.R.string;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ChatBox {
	
	private string[] theChatHistory = new string[5];
	private boolean scam = false;

	//Temp
	//For text
	private Paint paint = new Paint();
	
	public ChatBox() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean getScam(){
		return scam;
	}

	public void setScam(boolean scam){
		this.scam = scam;
	}
	
	public void TrySetActive(){
		//50% chance to have scam in chatroom
		Random r = new Random();
		if(r.nextInt(2) < 1){
			scam = true;
		}
	}
	
	public void draw(Canvas canvas){
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(100);
		paint.setTextSize(100);
		
		if(scam){
			canvas.drawText("Scam", 800, 400, paint);
		}
		else{
			canvas.drawText("!Scam", 800, 400, paint);
		}
	}
	
	public void update(){
		
	}
}