package com.byb.gambler.models;

import java.net.URI;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Developer{
	
	private static Developer developer = null;
	
	private Developer()
	{
		
	}
	
	public static Developer getInstance()
	{
		// Checks for UserLogInDetails object.
		//If Developer object is null it will create the object.
		// Else return the existing object.
		if (developer == null) developer = new Developer();
		return developer;
	}
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
//		return super.clone();
		throw new CloneNotSupportedException("No need to replicate the object.");
	}
	
	/* Method to check for network connectivity */
	public boolean isNetworkConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager == null) {
			return false;
		} else {
			NetworkInfo[] info = connManager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void hideSoftInput(Context ctx,EditText editText)
	{
	    InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
	    inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	public static String encodeURL(String urlImg)
	{
		String imgUrl = urlImg;
		try{
			URL url = new URL(imgUrl);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			url = uri.toURL();
			return url.toString();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return imgUrl;
	}
	
}