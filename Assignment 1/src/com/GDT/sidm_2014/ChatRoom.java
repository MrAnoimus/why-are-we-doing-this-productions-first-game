package com.GDT.sidm_2014;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class ChatRoom {
	private Objects theChatRoom;
	private boolean warning;
	private long timeActive = 0;
	private ChatBox theChatBox;
	
	//To change texture color
	private LightingColorFilter FilterWhite = new LightingColorFilter(Color.WHITE, 1),
								FilterRed = new LightingColorFilter(Color.RED, 1);
	private ColorFilter filter = FilterWhite;
	private Paint paint = new Paint();
	
	public ChatRoom(Bitmap bitmap, int x, int y, Bitmap bitmap2, int x2, int y2){
		theChatRoom = new Objects(bitmap, x, y);
		theChatBox = new ChatBox(bitmap2, x2, y2);
		
		warning = false;
	}
	
	public Objects getObjects(){
		return theChatRoom;
	}
	public boolean getWarning(){
		return warning;
	}
	public long getTimeActive(){
		return timeActive;
	}
	public ChatBox getChatBox(){
		return theChatBox;
	}
	
	public void setWarning(boolean warning){
		this.warning = warning;
	}
	public void Deactivate(){
		theChatBox.Deactivate();
	}
	public void setText(Conversation newConversation){
		theChatBox.setText(newConversation.GetScam(), newConversation.GetText());
	}
	
	public void Activate(){
		warning = true;
		timeActive = System.currentTimeMillis();
	}
	
	public boolean TrySetActive(){
		//30% chance to change chatroom to warning mode
		if(warning == false){
			Random r = new Random();
			if(r.nextInt(100) < 30){
				warning = true;
				timeActive = System.currentTimeMillis();
				return true;
			}
		}
		
		
		return false;
	}
	
	public void update(){
	}
	
	public void draw(Canvas canvas){
		if(warning){
			filter = FilterRed;
		}
		else{
			filter = FilterWhite;
		}
		paint.setColorFilter(filter);
		canvas.drawBitmap(theChatRoom.getBitmap(), theChatRoom.getX(), theChatRoom.getY(), paint);
	}
}
