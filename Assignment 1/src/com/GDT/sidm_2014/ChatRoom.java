package com.GDT.sidm_2014;

import java.util.Random;

import android.graphics.Bitmap;

public class ChatRoom {
	private Objects theChatRoom;
	private boolean warning;
	private long timeLastCheck;
	
	public ChatRoom(Bitmap bitmap, int x, int y){
		theChatRoom = new Objects(bitmap, x, y);
		warning = false;
		timeLastCheck = System.currentTimeMillis();
	}
	
	public Objects getObject(){
		return theChatRoom;
	}
	public boolean getWarning(){
		return warning;
	}
	
	public void update(){
		if(warning == false){
			//if(System.currentTimeMillis() - timeLastCheck > 1000)
		}
	}
}
