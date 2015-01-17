package com.GDT.sidm_2014;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;



public class OptionScreen extends Activity implements OnClickListener, OnSeekBarChangeListener{

	private SeekBar bar;
	private Button btn_back;
	private TextView textProgress, textAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
		setContentView(R.layout.optionscreen);
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		bar =(SeekBar)findViewById(R.id.seekBar1); //
		bar.setOnSeekBarChangeListener(this);
		//bar.setProgress((int) GamePanelSurfaceView.BgmVolume);
		textProgress = (TextView)findViewById(R.id.textView1);
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		// TODO Auto-generated method stub
		float Volume=(float) progress/100;
		textProgress.setText("The Value is: "+GamePanelSurfaceView.BgmVolume);
		GamePanelSurfaceView.BgmVolume = Volume;
		GamePanelSurfaceView.bgm.setVolume(GamePanelSurfaceView.BgmVolume, GamePanelSurfaceView.BgmVolume);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	public void Update()
	{
		
	}
}

