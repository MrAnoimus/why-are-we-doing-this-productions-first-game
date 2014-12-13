package com.GDT.sidm_2014;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Objects {
	private Bitmap bitmap;		// the animation sequence
	private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
	private int frame;			// number of frames in animation
	private int currentFrame;	// the current frame
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)
	
	private int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	private int spriteHeight;	// the height of the sprite
	
	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)
	
	public Objects(Bitmap bitmap, int x, int y) 
	{
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		spriteWidth = bitmap.getWidth();
		spriteHeight = bitmap.getHeight();
		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

	}

	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Rect getSourceRect() {
		return sourceRect;
	}
	public void setSourceRect(Rect sourceRect) {
		this.sourceRect = sourceRect;
	}
	public int getFrame() {
		return frame;
	}
	public void setFrame(int frame) {
		this.frame = frame;
	}
	public int getCurrentFrame() {
		return currentFrame;
	}
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	public int getFramePeriod() {
		return framePeriod;
	}
	public void setFramePeriod(int framePeriod) {
		this.framePeriod = framePeriod;
	}
	public int getSpriteWidth() {
		return spriteWidth;
	}
	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}
	public int getSpriteHeight() {
		return spriteHeight;
	}
	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
		
	public void update(long gameTime) {
		if (gameTime > frameTicker + framePeriod) {
			frameTicker = gameTime;
			// increment the frame
			currentFrame++;
			if (currentFrame >= frame) {
				currentFrame = 0;
			}
		}
		// define the rectangle to cut out sprite
		this.sourceRect.left = currentFrame * spriteWidth;
		this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}
	public void draw(Canvas canvas) {
		// where to draw the sprite
		Rect destRect = new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
}