package com.byb.gambler.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.byb.gambler.R;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.utility.BYBPreferences;
import com.byb.gambler.utility.Utility;

/** Application login screen */
public class LoginActivity extends Activity {
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,
						LoginActivity.this);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						LoginActivity.this);
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(LoginActivity.this, R.string.logging_in, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						LoginActivity.this);
				break;
			}
		};
	};

	private EditText etEmail;
	private EditText etPassword;
	private Button btnSignIn, btnBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnBack = (Button) findViewById(R.id.btnHeaderLeft);
		
		btnSignIn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = etEmail.getText().toString().trim();
				String password = etPassword.getText().toString().trim();

				hideKeyBoard();
				if (!isEditTextNull() && Utility.isEmailValid(email))
					loginUserViaEmail(email, password);
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeyBoard();
				LoginActivity.this.finish();
			}
		});

		etEmail.addTextChangedListener(new TextchangeListner());
		etPassword.addTextChangedListener(new TextchangeListner());
		
		etEmail.setText("vyomthakur.mca@gmail.com");
		etPassword.setText("anand218");

	}
	
	
	private void hideKeyBoard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
	}

	private class TextchangeListner implements TextWatcher {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (isEditTextNull()
					|| !Utility.isEmailValid(etEmail.getText().toString()
							.trim()) || (count == 0)) {
				btnSignIn.setClickable(false);
				btnSignIn.setEnabled(false);
			} else {
				btnSignIn.setClickable(true);
				btnSignIn.setEnabled(true);
			}
		}

	}

	private boolean isEditTextNull() {
		if (etEmail.getText() != null
				&& etEmail.getText().toString().trim().length() > 0
				&& etPassword.getText() != null
				&& etPassword.getText().toString().trim().length() > 0) {
			return false;
		}
		return true;
	}

	private void loginUserViaEmail(final String email, final String password) {
		if(Developer.getInstance().isNetworkConnected(LoginActivity.this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						JSONObject jobj = new JSONObject();
						jobj.put("email", email);
						jobj.put("password", password);
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWS("login.json",
								postData,false,"");
						Log.d("tag", "Login : " +response);
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){
							JSONObject jsonData = new JSONObject(json.getString("data"));
							boolean successful = UserManager.getInstance().parseJson(jsonData, LoginActivity.this);
							if(successful){
								startActivity(new Intent(LoginActivity.this, RootTabScreen.class));
								LoginActivity.this.finish();
								WelcomeActivity.welcomeActivity.finish();
								BYBPreferences.getInstance(LoginActivity.this).setIsLoggedin(true,false);
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
