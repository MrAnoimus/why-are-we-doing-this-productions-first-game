package com.GDT.sidm_2014;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainMenu extends Activity implements OnClickListener{
	
	private Button btn_start;
	private Button btn_help;
	private Button btn_option;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
		setContentView(R.layout.mainmenu);
		
		btn_start = (Button)findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		
		btn_help = (Button)findViewById(R.id.btn_help);
		btn_help.setOnClickListener(this);
		
		btn_option = (Button)findViewById(R.id.btn_option);
		btn_option.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onClick(View v){	//check which button is pressed
		Intent intent = new Intent();
		
		if(v == btn_start){
			intent.setClass(this, GamePage.class);
		}
		else if(v == btn_help){
			intent.setClass(this, HelpScreen.class);
		}
		else if(v == btn_option){
			intent.setClass(this, OptionScreen.class);
		}
		startActivity(intent);
		
	}
	
     public void onStop() {
         super.onStop();
         
     }
     
     public void onDestroy() {
         super.onDestroy();
         
     }
}
