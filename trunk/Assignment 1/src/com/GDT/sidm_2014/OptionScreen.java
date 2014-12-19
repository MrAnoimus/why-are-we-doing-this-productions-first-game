package com.GDT.sidm_2014;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;



public class OptionScreen extends Activity implements OnClickListener{
	

	private Button btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
		setContentView(R.layout.optionscreen);
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
	//return false;
		int action = event.getAction();
		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		}
		return true;
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_back){
			finish();
		
	}
		
		}
	}

