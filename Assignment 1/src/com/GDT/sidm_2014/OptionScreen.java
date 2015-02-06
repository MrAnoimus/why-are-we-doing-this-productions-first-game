package com.GDT.sidm_2014;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;


public class OptionScreen extends Activity implements OnClickListener, OnSeekBarChangeListener{
	private RadioGroup DifficultyGroup;
	private RadioButton DifficultyButton, Easy, Normal;
	private static short Difficulty = 1;
	private SeekBar bar;
	private Button btn_back;
	private TextView textProgress, textAction;
	int SelectedId;
	
	//For facebook private
	Button btn_FB;
	private LoginButton loginBtn;
	private TextView userName;
	private UiLifecycleHelper uiHelper;
	private static final String TAG = null;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static String message = "I am playing Scannzers right now!";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
		setContentView(R.layout.optionscreen);
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		bar =(SeekBar)findViewById(R.id.seekBar1); //
		bar.setProgress(ConvertFloatToInt(Splashpage.BgmVolume));
		bar.setOnSeekBarChangeListener(this);
		bar.setMax(100);
		//bar.setProgress(0);
		Easy=(RadioButton) findViewById(R.id.EasyButton);
		Normal=(RadioButton) findViewById(R.id.NormalButton);
		if(Difficulty == 1)
		{
			Easy.setChecked(true);
		}
		else if(Difficulty == 2)
		{
			Normal.setChecked(true);
		}
		AddListenerOnButton();
		
		textProgress = (TextView)findViewById(R.id.textView1);
		textProgress.setText("Volume Level "+ConvertFloatToInt(Splashpage.BgmVolume)+"%");
		
		//For facebook
		userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    userName.setText("Hello, " + user.getName());
                } else {
                    userName.setText("You are not logged");
                }
            }
        });

        btn_FB = (Button)findViewById(R.id.btn_FB);
        btn_FB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConnectFacebook();
			}
		});
		
		
		buttonsEnabled(true);
		
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.GDT.sidm_2014", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	}
	
	public void ConnectFacebook() {
	     Log.v(TAG,"test");
	     Session session = new Session(this);
	     Session.setActiveSession(session);
	     Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
	     Session.StatusCallback statusCallback = new Session.StatusCallback() {

		     @Override
		     public void call(Session session, SessionState state, Exception exception) {
	
		    	 String message = "Facebook session status changed - " + session.getState() + " - Exception: " + exception;
		    	 Toast.makeText(OptionScreen.this, message, Toast.LENGTH_SHORT).show();
		    	 Log.w("Facebook test", message);
		    	 
		    	 if (session.isOpened() || session.getPermissions().contains("publish_actions")) {
		    		 postStatusMessage();
		    	 }
		    	 else if (session.isOpened()) {
		    		 OpenRequest open = new OpenRequest(OptionScreen.this).setCallback(this);
		    		 List<String> permission = new ArrayList<String>();
		    		 permission.add("publish_actions");
		    		 open.setPermissions(permission);
		    		 Log.w("Facebook test", "Open for publish");
		    		 session.openForPublish(open);
		    	 }
		    	 else{
		    		 Log.w("Facebook test", "Unable to publish");
		    	 }
		     }
	     };
	     
	     if (!session.isOpened() && !session.isClosed() && session.getState() != SessionState.OPENING) {
	    	 session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	     }
	     else {
	    	 Log.w("Facebook test", "Open active session");
	    	 Session.openActiveSession(this, true, statusCallback);
	     }
	}
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (state.isOpened()) {
				buttonsEnabled(true);
				Log.d("FacebookSampleActivity", "Facebook session opened");
			} else if (state.isClosed()) {
				buttonsEnabled(false);
				Log.d("FacebookSampleActivity", "Facebook session closed");
			}
		}
	};
	public void buttonsEnabled(boolean isEnabled) {
		btn_FB.setEnabled(isEnabled);
	}
	public void postStatusMessage() {
		if (checkPermissions()) {
			Request request = Request.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					if (response.getError() == null)
						Toast.makeText(OptionScreen.this, "Status updated successfully", Toast.LENGTH_LONG).show();
				}
			});
			
			request.executeAsync();
		} else {
			requestPermissions();
		}
	}
	public boolean checkPermissions() {
		Session s = Session.getActiveSession();
		
		if (s != null) {
			return s.getPermissions().contains("publish_actions");
		} else
			return false;
	}
	public void requestPermissions() {
		Session s = Session.getActiveSession();
		
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
	}
	
	
	
	public void AddListenerOnButton()
	{
		DifficultyGroup =(RadioGroup)findViewById(R.id.Diffuculty);
	}
	
	
	//@Override
    //public void onBackPressed() {
       //  boolean fromNewActivity=true;
	   //  Intent mainIntent = new Intent(getBaseContext(), GamePanelSurfaceView.class);
	    // Bundle bundleObj = new Bundle();
	    // bundleObj.putString("fromNewActivity", Boolean.toString(fromNewActivity));
	    // mainIntent.putExtras(bundleObj);
	    // startActivityForResult(mainIntent, 0);
	   //  finish();
        //}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_back)
		{
			finish();
		}
		SelectedId = DifficultyGroup.getCheckedRadioButtonId();
		DifficultyButton =(RadioButton)findViewById(SelectedId);
		if(Easy.isChecked())
		{
			Difficulty = 1;
		}
		else if(Normal.isChecked())
		{
			Difficulty = 2;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
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
		finish();
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}
	
	public int ConvertFloatToInt(float Value)
	{
		float ReturnValue = (Value*100);
		return (int)ReturnValue;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		// TODO Auto-generated method stub
		float Volume=(float) progress/100;
		
		Splashpage.BgmVolume = Volume;
		MainMenu.bgm.setVolume(Splashpage.BgmVolume, Splashpage.BgmVolume);
		textProgress.setText("Volume Level "+ConvertFloatToInt(Splashpage.BgmVolume)+"%");
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
	
	public static short getDifficulty(){
		return Difficulty;
	}
}

