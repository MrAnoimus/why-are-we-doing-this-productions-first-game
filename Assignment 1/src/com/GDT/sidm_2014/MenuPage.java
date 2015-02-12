package com.GDT.sidm_2014;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MenuPage extends Activity{

	SharedPreferences sp;
	String name = "ScoreData";
	public static int currentHighscore;
	
	public static Activity activityTracker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
		setContentView(new MainMenu(this, this));
		
		try{
			sp = getSharedPreferences(name,Context.MODE_PRIVATE);
			int currentData= sp.getInt("Highscore", 0);
			currentHighscore= currentData;
			int newData = getIntent().getIntExtra("Highscore", 0);
			if(newData>currentData)
			{
				SharedPreferences.Editor Ed = sp.edit();
				Ed.putInt("Highscore", newData);
				Ed.commit();
				currentHighscore=newData;
			}
			
		}
		catch(Exception e)
		{
			
		}
		
		
	}
}
