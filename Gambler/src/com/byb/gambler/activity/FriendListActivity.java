package com.byb.gambler.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.models.UserInfo;
import com.byb.gambler.utility.BYBPreferences;
import com.byb.gambler.utility.Constants;
import com.byb.gambler.utility.DialogUtility;
import com.byb.gambler.views.ContactFriendList;
import com.byb.gambler.views.FacebookFriendList;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FriendListActivity extends Activity {

	private static View currentView;
	static Animation animLeftIn,animRightOut,animRightIn,animLeftOut;
	private static FriendListActivity friendsActivity;
	public static Context friendsContext;
	private static View prevView;

	private RadioButton rbContacts,rbFacebook;
	public final String APP_ID = "169177979911982";
	Facebook authenticatedFacebook = new Facebook(APP_ID);
	private static final String[] PERMISSIONS = new String[] {"offline_access", "email", "user_birthday", "publish_stream"};
	private FacebookFriendList fbList;
	private ContactFriendList contactList;
	private ListView lvFriendList;
	private AutoCompleteTextView mEtAutoComplete;
	private TextView tvPoints;
	private Button btnBack;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,
						FriendListActivity.this);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						FriendListActivity.this);
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(FriendListActivity.this, R.string.processing, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						FriendListActivity.this);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friendsContext = this;
		animLeftIn = AnimationUtils.loadAnimation(friendsContext, R.anim.slide_in_left);
		animLeftIn.setDuration(500);
		animRightOut = AnimationUtils.loadAnimation(friendsContext, R.anim.slide_out_right);
		animRightOut.setDuration(500);
		animRightIn = AnimationUtils.loadAnimation(friendsContext, R.anim.slide_in_right);
		animRightIn.setDuration(500);
		animLeftOut = AnimationUtils.loadAnimation(friendsContext, R.anim.slide_out_left);
		animLeftOut.setDuration(500);
		friendsActivity = this;

		/*if(savedInstanceState==null)
		setView(BetsView.getView(getApplicationContext()));*/

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friends);

		rbFacebook = (RadioButton) findViewById(R.id.rbFacebook);
		rbContacts = (RadioButton) findViewById(R.id.rbContacts);
		lvFriendList= (ListView) findViewById(R.id.lvFriendList);
		tvPoints = (TextView) findViewById(R.id.imgBtnHeaderRight);
		btnBack = (Button) findViewById(R.id.btnHeaderLeft);
		
		
		mEtAutoComplete = (AutoCompleteTextView) findViewById(R.id.et_autoComplete);
				

		fbList = new FacebookFriendList(this);
		contactList = new ContactFriendList(this);
		
		rbContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
		contactList.getContactList(lvFriendList,mEtAutoComplete);
		
		tvPoints.setText(""+UserInfo.getInstance().getUserStatistics().getBallsAvailable());
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FriendListActivity.this.finish();
			}
		});

		rbFacebook.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					if(BYBPreferences.getInstance(FriendListActivity.this).getFBSessionToken().equalsIgnoreCase(Constants.DEFAULT)){
						rbFacebook.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
						
						DialogUtility.showDialog(FriendListActivity.this, getResources().getString(R.string.title_dialog), getResources().getString(R.string.warning_dialog_message), 
								new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								authenticatedFacebook.authorize(FriendListActivity.this, PERMISSIONS,new TestLoginListener());
								dialog.dismiss();
								dialog.cancel();
							}
						}, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								dialog.dismiss();
								rbContacts.setChecked(true);
								rbFacebook.setChecked(false);
								rbContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
							}
						}, false);
						
					}else{
						rbFacebook.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
						fbList.getFacebookFriendList(lvFriendList, mEtAutoComplete);
					}
				}else{
					rbFacebook.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});

		rbContacts.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					rbContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
					contactList.getContactList(lvFriendList,mEtAutoComplete);
				}else{
					rbContacts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});


	}
	
	public class TestLoginListener implements DialogListener {

		public void onComplete(Bundle values) {
			mHandler.sendEmptyMessage(3);
			BYBPreferences.getInstance(FriendListActivity.this).setFBSessionToken(authenticatedFacebook.getAccessToken(), "");
			BYBPreferences.getInstance(FriendListActivity.this).setFBExpiry(authenticatedFacebook.getAccessExpires());
			fbList.getFacebookFriendList(lvFriendList,mEtAutoComplete);
		}

		public void onCancel() {
			mHandler.sendEmptyMessage(3);
		}

		public void onError(DialogError e) {
			e.printStackTrace();
		}

		public void onFacebookError(FacebookError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		authenticatedFacebook.authorizeCallback(requestCode, resultCode, data);
		mHandler.sendEmptyMessage(2);
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
		friendsActivity.setContentView(newView);
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
		friendsActivity.setContentView(currentView);
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
