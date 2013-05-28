package com.byb.gambler.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;

public class Utility {

	private static ConnectivityManager mConnectivityManager;
	private static NetworkInfo mWifiInfo, mMobileInfo;


	/**
	 * Checks the validation of email address.
	 * Takes pattern and checks the text entered is valid email address or not.
	 * @param email : email in string format
	 * @return
	 * True if email address is correct.
	 */
	public static boolean isEmailValid(String email) {
		String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}else if(email.equals("")){
			return true;
		}
		return false;
	}


	/**
	 * Method checks if the given phone number is valid or not.
	 * @param number : Phone number is to be checked.
	 * @return
	 * True if the number is valid.
	 * False if number is not valid.
	 */
	public static boolean isPhoneNumberValid(String number){

		String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";

		if(number.length()<10 || number.length()>13 || number.matches(regexStr)==false  ) {
			Log.d("tag", "Number is not valid");
			return false;
		}

		return true;
	}


	/**
	 * Checks if any text box is null or not.
	 * @param text : Text view for which validation is to be checked.
	 * @return
	 * True if not null.
	 */
	public static boolean isEditTextNull(EditText text) {
		if (text.getText() != null && text.getText().length() > 0){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * Check for TYPE_WIFI and TYPE_MOBILE connection using isConnected()
	 * Checks for generic Exceptions and writes them to logcat as CheckConnectivity Exception.
	 * Make sure AndroidManifest.xml has appropriate permissions.
	 * @param context : Application context
	 * @return 
	 * True if network is available.
	 */
	public static Boolean isNetworkAvailable(Context context){

		try{
			mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			mWifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			mMobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  

			if(mWifiInfo.isConnected() || mMobileInfo.isConnected())
			{
				return true;
			}
		}
		catch(Exception e){
			System.out.println("CheckConnectivity Exception: " + e.getMessage());
		}

		return false;
	}

	public static Location getLocation(Context context){
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		return location;
	}

	public static String[]  getMessageLatLong(String message){
		String[] splitMessage = message.split(",");

		if(splitMessage.length != 1){
			splitMessage[0] = splitMessage[0].subSequence(splitMessage[0].indexOf("=")+1, splitMessage[0].length()).toString();
			splitMessage[1] = splitMessage[1].subSequence(splitMessage[1].indexOf("=")+1, splitMessage[1].length()).toString();
			return splitMessage;
		}
		//		Log.d("tag", "Long : "+splitMessage[1].subSequence(splitMessage[1].indexOf("=")+1, splitMessage[1].length()));
		return null;
	}
}
