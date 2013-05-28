package com.byb.gambler.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.utility.BYBPreferences;
import com.byb.gambler.utility.Constants;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class WelcomeActivity extends Activity implements OnClickListener {

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,
						WelcomeActivity.this);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						WelcomeActivity.this);
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(WelcomeActivity.this, R.string.logging_in, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						WelcomeActivity.this);
			}
		};
	};

	private Button btnSignInViaFacebook,btnSignInViaEmail,btnSignIn;
	private TextView forgotPassword;
	public final String APP_ID = "169177979911982";
	public static WelcomeActivity welcomeActivity;
	private BYBPreferences mPreference;
	private static final String[] PERMISSIONS = new String[] {"offline_access", "email", "user_birthday", "publish_stream"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		
		welcomeActivity = this;
		
		mPreference = BYBPreferences.getInstance(this);
		
		if(mPreference.getIsLoggedin()){
			loginUser();
		}

		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnSignInViaEmail = (Button) findViewById(R.id.btnSignUpViaEmail);
		btnSignInViaFacebook = (Button) findViewById(R.id.btnSignUpViaFacebook);
		forgotPassword= (TextView) findViewById(R.id.tvForgotPassword);

		forgotPassword.setText(Html.fromHtml("<a href='com.byb.gambler.ResetPswdActivity://Kode'>Forgot password?</a>"));
		forgotPassword.setClickable(true);
		forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

		btnSignIn.setOnClickListener(this);
		btnSignInViaEmail.setOnClickListener(this);
		btnSignInViaFacebook.setOnClickListener(this);
		getHashKey();
	}
	
	private void getHashKey()
    {
            PackageInfo info;
            try {
            info = getPackageManager().getPackageInfo("com.byb.gambler", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                       MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
                       //String something = new String(Base64.encode(md.digest(), 0));
                         String something = new String(Base64.encode(md.digest(),0));
                       Log.e("tag","Key : "+ something);
            } 
            }
            catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
            }

            catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
            }
            catch (Exception e){
            Log.e("exception", e.toString());
            }

    }

	Facebook authenticatedFacebook = new Facebook(APP_ID);
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSignIn:
			startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));	
			break;

		case R.id.btnSignUpViaEmail:
			startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));	
			break;
		case R.id.btnSignUpViaFacebook:
			if(BYBPreferences.getInstance(this).getFBSessionToken() == Constants.DEFAULT){
				authenticatedFacebook.authorize(WelcomeActivity.this, PERMISSIONS,new TestLoginListener());
			}else{
				loginUserViaFacebook(BYBPreferences.getInstance(this).getFBID(),BYBPreferences.getInstance(this).getFBSessionToken());
			}
			break;
		}		
	}

	public class TestLoginListener implements DialogListener {

		public void onComplete(Bundle values) {
			new Thread(){
				public void run() {
					testAuthenticatedApi();
				};}.start();
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

	public boolean testAuthenticatedApi() {
		if (!authenticatedFacebook.isSessionValid()) return false;
		try {
			String response = authenticatedFacebook.request("me");
			JSONObject obj = Util.parseJson(response);
			Log.d("Tests", "Testing request for 'me'"+ authenticatedFacebook.getAccessToken()+obj.getString("id"));
			
			loginUserViaFacebook( obj.getString("id"),authenticatedFacebook.getAccessToken());
			BYBPreferences.getInstance(this).setFBSessionToken(authenticatedFacebook.getAccessToken(), obj.getString("id"));
			BYBPreferences.getInstance(this).setFBExpiry(authenticatedFacebook.getAccessExpires());
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		authenticatedFacebook.authorizeCallback(requestCode, resultCode, data);
		mHandler.sendEmptyMessage(2);
	}

	private void loginUserViaFacebook(final String facebook_id, final String facebook_access_token) {
		if(Developer.getInstance().isNetworkConnected(WelcomeActivity.this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						JSONObject jobj = new JSONObject();
						jobj.put("email", "");
						jobj.put("password", "");
						jobj.put("facebook_id", facebook_id);
						jobj.put("facebook_access_token", facebook_access_token);
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWS("login.json",
								postData,false,"");

						Log.d("tag", "Response : "+response);
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){
							JSONObject jsonData = new JSONObject(json.getString("data"));
							BYBPreferences.getInstance(WelcomeActivity.this).setFBSessionToken(facebook_access_token, facebook_id);
							boolean successful = UserManager.getInstance().parseJson(jsonData, WelcomeActivity.this);
							if(successful){
								startActivity(new Intent(WelcomeActivity.this, RootTabScreen.class));
								BYBPreferences.getInstance(WelcomeActivity.this).setIsLoggedin(true, true);
								finish();
							}
							else
								mHandler.sendEmptyMessage(4);
						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);

	}
	
private void loginUser() {
		
		Log.d("tag", "ID : "+mPreference.getFBID() + ">>>>>>>>>" +mPreference.getFBSessionToken());
		Log.d("tag", "User : "+mPreference.getUname() + ">>>>>>>>>" +mPreference.getPassword());
		
		if(Developer.getInstance().isNetworkConnected(WelcomeActivity.this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						JSONObject jobj = new JSONObject();

						if(mPreference.getUname().equalsIgnoreCase(Constants.DEFAULT)){
							Log.d("tag", "FB lOgin");
							jobj.put("email", "");
							jobj.put("password", "");
							jobj.put("facebook_id", mPreference.getFBID());
							jobj.put("facebook_access_token", mPreference.getFBSessionToken());
						}else{
							Log.d("tag", "Email lOgin");
							jobj.put("email", mPreference.getUname());
							jobj.put("password", mPreference.getPassword());
							jobj.put("facebook_id", "");
							jobj.put("facebook_access_token", "");
						}
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWS("login.json",
								postData,false,"");

						Log.d("tag", "Response : "+response);
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){
							JSONObject jsonData = new JSONObject(json.getString("data"));
							boolean successful = UserManager.getInstance().parseJson(jsonData, WelcomeActivity.this);
							if(successful){
								startActivity(new Intent(WelcomeActivity.this, RootTabScreen.class));
								BYBPreferences.getInstance(WelcomeActivity.this).setIsLoggedin(true, true);
								finish();
							}
							else
								mHandler.sendEmptyMessage(4);
						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);

	}


}
