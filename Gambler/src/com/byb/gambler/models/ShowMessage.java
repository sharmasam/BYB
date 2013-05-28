package com.byb.gambler.models;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;

import com.byb.gambler.R;


public class ShowMessage{
	
	
	Context mContext;
	String progressMessage;
	Thread t;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 1:
				hidePDialog();
				break;
			}
		};
	};
	
	
	private static ShowMessage message = null;
	private static boolean isAlertDialogShowing = false;
	
	private ProgressDialog pDialog;
	
	public void showPDialog(Context context, int message, final Thread t){
		if(pDialog == null){
			pDialog = new ProgressDialog(context);
			pDialog.setTitle(R.string.app_name);
			pDialog.setCancelable(false);
		}
		pDialog.setMessage(context.getResources().getString(message));
		
		pDialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {
			}
		});
		
		try {
			pDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void hidePDialog(){
		if(pDialog != null && pDialog.isShowing()){
			pDialog.hide();
			pDialog = null;
		}
	}
	
	private ShowMessage()
	{
		
	}
	
	public static ShowMessage getInstance()
	{
		// Checks for ShowMessage object.
		//If ShowMessage object is null it will create the object.
		// Else return the existing object.
		if (message == null) message = new ShowMessage();
		return message;
	}
	
	/*
	 * Used to show message on Alert Dialog from any screen by just passing mesasge content and Screen's context
	 */
	public void showMessage(int message,Context context)
	{
		try {
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle(context.getResources().getString(R.string.app_name));
			builder.setMessage(message);
			builder.setCancelable(false);
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					isAlertDialogShowing = false;
					dialog.dismiss();
					
				}
			});
			if(!isAlertDialogShowing)
			builder.show();
			isAlertDialogShowing = true;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showMessageString(String message,Context context)
	{
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(context.getResources().getString(R.string.app_name));
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				isAlertDialogShowing = false;
				dialog.dismiss();
				
			}
		});
//		builder.setPositiveButton("OK", l);
		if(!isAlertDialogShowing)
		builder.show();
		isAlertDialogShowing = true;
	}
	
}