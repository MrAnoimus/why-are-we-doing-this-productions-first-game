package com.GDT.sidm_2014;

import java.util.Random;
import java.util.Vector;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class ChatBox {
	
	private Vector<Conversation> theChatHistory = new Vector<Conversation>();
	
	private Objects ChatBubble;
	private Paint paint = new Paint();
	
	public ChatBox(Bitmap bitmap, int x, int y){
		ChatBubble = new Objects(bitmap, x, y);
	}
	
	public boolean getScam(){
		for(int i = 0; i < theChatHistory.size(); ++i){
			if(theChatHistory.get(i).GetScam())
				return true;
		}
		
		return false;
	}

	public void Deactivate(){
		for(int i = 0; i < theChatHistory.size(); ++i){
			if(theChatHistory.get(i).GetScam()){
				theChatHistory.remove(i);
				
				//Shift up messages below
				for(int j = i; j < theChatHistory.size(); ++j)
					theChatHistory.get(j).ShiftUp();
			}
		}
	}
	public void setText(boolean scam, String text){		
		//Push existing chat bubbles up
		for(int i = 0; i < theChatHistory.size(); ++i){
			theChatHistory.get(i).ShiftUp();
		}
		
		Objects newChatBubble = new Objects(ChatBubble);
		Conversation newConversation = new Conversation(scam, text, newChatBubble);
		theChatHistory.add(newConversation);
		
		//If there too much conversations in chat history destroy earliest ones
		while(theChatHistory.size() > 5){
			theChatHistory.remove(0);
		}
	}
	
	public void draw(Canvas canvas){
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(100);
		paint.setTextSize(100);
		
		//Render text
//		if(scam){
//			canvas.drawText("Scam", 800, 400, paint);
//		}
//		else{
//			canvas.drawText("!Scam", 800, 400, paint);
//		}
		
		//Draw text and chat bubble
		for(int i = 0; i < theChatHistory.size(); ++i){
			theChatHistory.get(i).Draw(canvas);
		}
	}
	public void update(){
	}
}