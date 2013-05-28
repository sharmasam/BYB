package com.byb.gambler.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.byb.gambler.R;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.utility.Utility;

public class ResetPswdActivity extends Activity {
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,
						ResetPswdActivity.this);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						ResetPswdActivity.this);
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(ResetPswdActivity.this, R.string.processing, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						ResetPswdActivity.this);
			case 5:
				ShowMessage.getInstance().showMessageString(getResources().getString(R.string.password_changed_successfully),
						ResetPswdActivity.this);
				break;
			}
		};
	};
	
	private EditText etEmail;
	private Button btnResetPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reset_password);
		etEmail = (EditText) findViewById(R.id.etEmail);
		btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
		
		etEmail.addTextChangedListener(new TextchangeListner());
		
		btnResetPassword.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Developer.hideSoftInput(ResetPswdActivity.this,etEmail);
				resetPassword(etEmail.getText().toString().trim());
			}
		});

	}
	
	public void reset(View v){
		switch (v.getId()) {
		case R.id.btnHeaderLeft:
			ResetPswdActivity.this.finish();
			break;
		}
	}
	
	private class TextchangeListner implements TextWatcher{

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(isEditTextNull() || !Utility.isEmailValid(etEmail.getText().toString().trim()) || (count == 0)){
				btnResetPassword.setClickable(false);
				btnResetPassword.setEnabled(false);
			}else{
				btnResetPassword.setClickable(true);
				btnResetPassword.setEnabled(true);
			}
		}

	}
	
	private boolean isEditTextNull() {
		if (etEmail.getText() != null && etEmail.getText().toString().trim().length() > 0){
			return false;
		}
		return true;
	}
	
	private void resetPassword(final String email) {
		if(Developer.getInstance().isNetworkConnected(ResetPswdActivity.this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						JSONObject jobj = new JSONObject();
						jobj.put("email", email);
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWS("resetpass.json",
								postData,false,"");
						
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){
							mHandler.sendEmptyMessage(5);
						}else{
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
