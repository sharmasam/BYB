package com.byb.gambler.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.byb.gambler.R;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.utility.BYBPreferences;
import com.byb.gambler.utility.Constants;


/** Splash screen */
public class SplashActivity extends Activity
{
	private int DELAY = 2000;
	private int START_SPLASH_ACTIVITY = 1;
	public static final String TAB_TO_BE_SELECTED = "tab";
	


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		Message msg = new Message();
		msg.what = START_SPLASH_ACTIVITY;
		splashHandler.sendMessageDelayed(msg, DELAY);

	}

	Handler splashHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == START_SPLASH_ACTIVITY)
			{
				Intent intent = new Intent();
				
					intent.setClass(SplashActivity.this, WelcomeActivity.class);
				startActivity(intent);
				finish();
			}
		};
	};

	public void onBackPressed()
	{
		if (splashHandler != null)
		{
			splashHandler.removeMessages(START_SPLASH_ACTIVITY);
		}
		super.onBackPressed();
	};


	
}
