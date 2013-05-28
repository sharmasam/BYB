package com.byb.gambler.utility;

import com.byb.gambler.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public class DialogUtility {

	public static final String TAG = "DialogUtility";
	private static AlertDialog dialog;
	private static ProgressDialog mProgressDialog;

	/**
	 * Static method to show the dialog with custom message on it
	 * 
	 * @param context
	 *            Context of the activity where to show the dialog
	 * @param title
	 *            Title to be shown either custom or application name
	 * @param msg
	 *            Custom message to be shown on dialog
	 * @param OK
	 *            Overridden click listener for OK button in dialog
	 * @param isCancelable
	 *            : Sets whether this dialog is cancelable with the BACK key.
	 */
	public static void showDialog(Context context, String title, String msg,
			DialogInterface.OnClickListener OK, boolean isCancelable) {

		if (title == null)
			title = context.getResources().getString(R.string.app_name);

		if (OK == null)
			OK = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface paramDialogInterface,int paramInt) {
					hideDialog();
				}
			};

		if (dialog == null) {
			Builder builder = new Builder(context);
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setPositiveButton("OK", OK);
			dialog = builder.create();
			dialog.setCancelable(isCancelable);
		}

		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Static method to show the dialog with custom message on it
	 * 
	 * @param context
	 *            Context of the activity where to show the dialog
	 * @param title
	 *            Title to be shown either custom or application name
	 * @param msg
	 *            Custom message to be shown on dialog
	 * @param OK
	 *            Overridden click listener for OK button in dialog
	 * @param cancel
	 *            Overridden click listener for cancel button in dialog
	 * @param isCancelable
	 *            : Sets whether this dialog is cancelable with the BACK key.
	 */
	public static void showDialog(Context context, String title, String msg,
			DialogInterface.OnClickListener OK,
			DialogInterface.OnClickListener cancel, boolean isCancelable) {

		if (title == null)
			title = context.getResources().getString(R.string.app_name);

		if (OK == null)
			OK = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					hideDialog();
				}
			};

		if (cancel == null)
			cancel = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					hideDialog();
				}
			};

		if (dialog == null) {
			Builder builder = new Builder(context);
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.setPositiveButton("OK", OK);
			builder.setNegativeButton("Cancel", cancel);
			dialog = builder.create();
			dialog.setCancelable(isCancelable);
		}

		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Static method to show the progress dialog.
	 * 
	 * @param context
	 *            : Context of the activity where to show the dialog
	 * @param isCancelable
	 *            : Sets whether this dialog is cancelable with the BACK key.
	 * @param message
	 *            : Message to be shwon on the progress dialog.
	 * @return Object of progress dialog.
	 */
	public static Dialog showProgressDialog(Context context,
			boolean isCancelable, String message) {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
		mProgressDialog.setCancelable(isCancelable);
		return mProgressDialog;
	}

	/**
	 * Static method to pause the progress dialog.
	 */
	public static void pauseProgressDialog() {
		try {
			if (mProgressDialog != null) {
				mProgressDialog.cancel();
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Static method to cancel the Dialog.
	 */
	public static void cancelDialog() {

		try {
			if (dialog != null) {
				dialog.cancel();
				dialog.dismiss();
				dialog = null;
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Static method to hide the dialog if visible
	 */
	public static void hideDialog() {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog.cancel();
			dialog = null;
		}
	}

	/**
	 * This method will create alert dialog
	 * 
	 * @param context
	 *            Context of calling class
	 * @param title
	 *            Title of the dialog to be shown
	 * @param msg
	 *            Msg of the dialog to be shown
	 * @param btnText
	 *            array of button texts
	 * @param listener
	 */
	public static void showAlertDialog(Context context, String title,
			String msg, String[] btnText,
			DialogInterface.OnClickListener listener) {

		if (listener == null)
			listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					paramDialogInterface.dismiss();
					paramDialogInterface.dismiss();
				}
			};

		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(btnText[0], listener);
		if (btnText.length != 1) {
			builder.setNegativeButton(btnText[1], listener);
		}
		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	public static AlertDialog showCustomtDialog(Context context,
			String title, String msg, String[] btnText,int layout_id,
			DialogInterface.OnClickListener listener) {
		if (listener == null)
			listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					paramDialogInterface.dismiss();
				}
			};
		LayoutInflater factory = LayoutInflater.from(context);
		final View textEntryView = factory.inflate(layout_id,
				null);
		Builder builder = new Builder(context);
		builder.setTitle(title);
		// builder.setMessage(msg);
		// builder.setView(mEmail_forgot);

		builder.setPositiveButton(btnText[0], listener);
		if (btnText.length != 1) {
			builder.setNegativeButton(btnText[1], listener);
		}
		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setView(textEntryView, 10, 10, 10, 10);
		dialog.show();
		return dialog;

	}
	

}
