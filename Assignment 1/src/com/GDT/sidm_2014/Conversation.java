package com.GDT.sidm_2014;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public class Conversation {

	public boolean scam;
	public String text;

	private Objects ChatBubble;
	
	//To change texture color
	private LightingColorFilter FilterWhite = new LightingColorFilter(Color.WHITE, 1),
								FilterRed = new LightingColorFilter(Color.RED, 1);
	private ColorFilter filter = FilterWhite;
	private Paint paint = new Paint();
	
	public Conversation(boolean scam, String text, Objects ChatBubble){
		this.scam = scam;
		this.text = text;
		if(this.scam)
			filter = FilterRed;
		else
			filter = FilterWhite;
		
		this.ChatBubble = ChatBubble;
	}
	
	public boolean GetScam(){
		return scam;
	}
	public String GetText(){
		return text;
	}
	
	public void SetX(int x){
		ChatBubble.setX(x);
	}
	public void SetY(int y){
		ChatBubble.setY(y);
	}
	public void ShiftUp(){
		ChatBubble.setY(ChatBubble.getY() - 160);
	}
	
	public void Draw(Canvas canvas){
		//Draw chat bubble
		paint.setColorFilter(filter);
		
		canvas.drawBitmap(ChatBubble.getBitmap(), ChatBubble.getX(), ChatBubble.getY(), paint);
		
		//Draw text
		paint.setARGB(255, 0, 0, 0);
		paint.setStrokeWidth(100);
		paint.setTextSize(75);
		
		canvas.drawText(text, ChatBubble.getX(), ChatBubble.getY() + 75, paint);
	}
}
