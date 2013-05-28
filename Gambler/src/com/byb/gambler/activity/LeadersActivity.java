package com.byb.gambler.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.byb.gambler.R;
import com.byb.gambler.views.LeadersView;

public class LeadersActivity extends Activity {
	
	private static View currentView;
	static Animation animLeftIn,animRightOut,animRightIn,animLeftOut;
	private static LeadersActivity leadersActivity;
	public static Context leadersContext;
	private static View prevView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		leadersContext = this;
		animLeftIn = AnimationUtils.loadAnimation(leadersContext, R.anim.slide_in_left);
		animLeftIn.setDuration(500);
		animRightOut = AnimationUtils.loadAnimation(leadersContext, R.anim.slide_out_right);
		animRightOut.setDuration(500);
		animRightIn = AnimationUtils.loadAnimation(leadersContext, R.anim.slide_in_right);
		animRightIn.setDuration(500);
		animLeftOut = AnimationUtils.loadAnimation(leadersContext, R.anim.slide_out_left);
		animLeftOut.setDuration(500);
		leadersActivity = this;
		
		if(savedInstanceState==null)
		setView(LeadersView.getView(getApplicationContext()));
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		System.out.println("onRetainNonConfigurationInstance");
		return super.onRetainNonConfigurationInstance();
	}
	
	@Override
	public Object getLastNonConfigurationInstance() {
		// TODO Auto-generated method stub
		System.out.println("getLastNonConfigurationInstance");
		return super.getLastNonConfigurationInstance();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		System.out.println("onSaveInstanceState");
		if(currentView != null){
		prevView = currentView;
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if(prevView!=null){
			setView(prevView);
			}
	}
	
	public static void setView(View newView){
//		setView(newView, false);
		leadersActivity.setContentView(newView);
	}
	public static void setView(View newView,boolean isBack){
		if(currentView != null){
			if(isBack)
			currentView.startAnimation(animRightOut);
			else
				currentView.startAnimation(animLeftOut);
			currentView = null;
		}
		currentView = newView;
		leadersActivity.setContentView(currentView);
		if(isBack)
		currentView.startAnimation(animLeftIn);
		else
			currentView.startAnimation(animRightIn);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(currentView != null && currentView.getParent() != null)
		((ViewGroup)currentView.getParent()).removeAllViews();
		super.onDestroy();
	}

}
