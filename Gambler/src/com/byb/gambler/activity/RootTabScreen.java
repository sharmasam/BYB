package com.byb.gambler.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.byb.gambler.R;
import com.byb.gambler.views.ContactFriendList;

public class RootTabScreen extends TabActivity {

	public static final int TABHOME = 0;
	public static final int TABPLAY = 1;
	public static final int TABLEADERS = 2;
	public static final int TABBETS = 3;
	public static final int TABMORE = 4;

	public static int HEIGHT, WIDTH;
	public static Context rootContext;

	public static TabHost tHost;
	TabWidget tWidget;
	Intent intent;
	public static RadioGroup rGroup;
//	private static LTDHorizontalScrollView hsvTabbar;

	public static boolean isAlertDialogShowing = false;

	static ProgressDialog pDialog;

	static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (pDialog != null) {
					pDialog.show();
					System.out.println("Progress Dialog is visible now");
				} else {
					System.out
							.println("You tried to show progress dialog but dialog object is null");
				}
				break;
			case 1:
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.dismiss();
					System.out.println("Progress Dialog is invisible now");
				} else {
					System.out
							.println("You tried to hide progress dialog but dialog object is null");
				}
				break;
			case 2:
				if (rootContext != null) {
					Bundle bun = msg.getData();
					String message = bun.getString("msg");
					AlertDialog.Builder builder = new Builder(rootContext);
					builder.setTitle(rootContext.getResources().getString(
							R.string.app_name));
					builder.setMessage(message);
					builder.setNeutralButton("OK", new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							isAlertDialogShowing = false;
							dialog.dismiss();

						}
					});
					if (!isAlertDialogShowing)
						builder.show();
					isAlertDialogShowing = true;
				}
				break;
			}
		}
	};

	public static void showProgressDialog() {
		mHandler.sendEmptyMessage(0);
	}

	public static void hideProgressDialog() {
		mHandler.sendEmptyMessage(1);
	}

	static int tabToBeSelected = 0;
	public static int tabOnWhichPaused = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// if(!isNetworkConnected())
		// showMessage("Network not connected.");
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.roottabscreen);
		rootContext = this;

		pDialog = new ProgressDialog(rootContext);
		System.out.println("Progress Dialog is initialized " + pDialog);
		pDialog.setMessage("Loading..");
		pDialog.setCancelable(false);
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT = getWindowManager().getDefaultDisplay().getHeight();

		tHost = getTabHost();
		tWidget = tHost.getTabWidget();

		intent = new Intent(this, PlayWebservice.class);
		TabSpec specPlay = tHost.newTabSpec("Play").setIndicator(
				"Play").setContent(intent);

		intent = new Intent(this, LeadersActivity.class);
		TabSpec specLeaders = tHost.newTabSpec("Leaders").setIndicator(
				"Leaders").setContent(intent);

		intent = new Intent(this, BetsActivity.class);
		TabSpec specBets = tHost.newTabSpec("Bets").setIndicator(
				"Bets").setContent(intent);

		intent = new Intent(this, HomeActivity.class);
		TabSpec specHome = tHost.newTabSpec("Home").setIndicator(
				"Home").setContent(intent);

		intent = new Intent(this, ContactFriendList.class);
		TabSpec specMore = tHost.newTabSpec("More").setIndicator(
				"More").setContent(intent);

		tHost.addTab(specHome);
		tHost.addTab(specPlay);
		tHost.addTab(specLeaders);
		tHost.addTab(specBets);
		tHost.addTab(specMore);

		tWidget.setVisibility(View.INVISIBLE);

//		hsvTabbar = (LTDHorizontalScrollView) findViewById(R.id.hsvRootTabbar);

		rGroup = (RadioGroup) findViewById(R.id.rgRootTabbar);

		setTab();

		int children = rGroup.getChildCount();

		for (int i = 0; i < children; i++) {
			rGroup.getChildAt(i).setId(i);
		}

		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				tabToBeSelected = checkedId;
				tabOnWhichPaused = tabToBeSelected;
				tHost.setCurrentTab(checkedId);
//				selectedTabImage(checkedId);
			}
		});

	}


	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("OnResume called on RootTab Screen");
		System.out.println("tabonwhichpaused = " + tabOnWhichPaused
				+ "tabtobeSelected = " + tabToBeSelected);
		if (tabOnWhichPaused < 0)
			setTab();
		else {
			tHost.setCurrentTab(tabOnWhichPaused);
			rGroup.check(tabOnWhichPaused);
			rGroup.getChildAt(tabOnWhichPaused).setSelected(true);
			tabToBeSelected = tabOnWhichPaused;
//			selectedTabImage(tabToBeSelected);
		}
	}

	private void setTab() {
		Bundle bun = getIntent().getExtras();
		if (bun != null && bun.containsKey(SplashActivity.TAB_TO_BE_SELECTED)) {
			tabToBeSelected = getIntent().getExtras().getInt(
					SplashActivity.TAB_TO_BE_SELECTED);
		} else {
			tabToBeSelected = TABBETS;
		}
		System.out.println("tabToBeSelected = " + tabToBeSelected);
		tabOnWhichPaused = tabToBeSelected;

		tHost.setCurrentTab(tabToBeSelected);
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		LayoutParams lParams = new LayoutParams(WIDTH / 5,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < rGroup.getChildCount(); i++) {
			rGroup.getChildAt(i).setLayoutParams(lParams);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		tabToBeSelected = 6;
		tHost.setCurrentTab(tabToBeSelected);
//		selectedTabImage(tabToBeSelected);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}