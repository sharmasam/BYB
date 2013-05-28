package com.byb.gambler.utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class BYBPreferences {

	private SharedPreferences mSharedPreferences;
	private static BYBPreferences mPreference;

	private BYBPreferences(Context context) {
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
	}

	public static BYBPreferences getInstance(Context context) {
		if (mPreference == null) {
			mPreference = new BYBPreferences(context);
		}
		return mPreference;
	}

	/**
	 * Called to store the if the user is logged in or not.
	 * 
	 * @param islogged
	 *            : True if user is loggede in.
	 */
	public void setIsLoggedin(Boolean islogged, boolean isFacebook) {
		Editor e = mSharedPreferences.edit();
		e.putBoolean(Constants.IS_LOGGED, islogged);
		e.putBoolean(Constants.IS_FACEBOOK, isFacebook);
		e.commit();
	}

	/**
	 * Called to get if the user is logged in or not.
	 * 
	 * @return true if user is logged in.
	 */
	public boolean getIsLoggedin() {
		return mSharedPreferences.getBoolean(Constants.IS_LOGGED, false);
	}
	
	public boolean getIsFacebookLoginin() {
		return mSharedPreferences.getBoolean(Constants.IS_FACEBOOK, false);
	}
	
	public void setFBSessionToken(String sessionToken, String ID) {
		Editor e = mSharedPreferences.edit();
		e.putString(Constants.FB_SESSION_TOKEN, sessionToken.trim());
		e.putString(Constants.FB_ID, ID.trim());
		e.commit();
	}
	
	public void setFBExpiry(long fbExpiry){
		Editor e = mSharedPreferences.edit();
		e.putLong(Constants.FB_EXPIRES, fbExpiry);
		e.commit();
	}
	
	public long getFBExpiry(){
		return mSharedPreferences.getLong(Constants.FB_EXPIRES, 0);
	}
	
	public void setUserCredentials(String uName, String password) {
		Editor e = mSharedPreferences.edit();
		e.putString(Constants.USER_NAME, uName.trim());
		e.putString(Constants.PASSWORD, password.trim());
		e.commit();
	}
	
	public String getUname() {
		return mSharedPreferences.getString(Constants.USER_NAME, Constants.DEFAULT);
	}
	
	public String getPassword() {
		return mSharedPreferences.getString(Constants.PASSWORD, Constants.DEFAULT);
	}
	
	public String getFBSessionToken() {
		return mSharedPreferences.getString(Constants.FB_SESSION_TOKEN, Constants.DEFAULT);
	}
	
	
	public String getFBID() {
		return mSharedPreferences.getString(Constants.FB_ID, Constants.DEFAULT);
	}
}
