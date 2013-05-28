package com.byb.gambler.activity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.utility.BYBPreferences;
import com.byb.gambler.utility.Utility;

public class RegisterActivity extends Activity{
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,
						RegisterActivity.this);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						RegisterActivity.this);
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(RegisterActivity.this, R.string.processing, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						RegisterActivity.this);
			}
		};
	};

	private EditText etBirthDay, etName, etEmail, etPassword;
	private UserManager mManager;
	private final int DATE_DIALOG_ID = 1;
	private Button mBtnSignup,btnBack;
	private CheckBox checkbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		
		mManager = UserManager.getInstance();

		etName = (EditText) findViewById(R.id.etUserName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etBirthDay = (EditText) findViewById(R.id.etBirthDay);
		btnBack = (Button) findViewById(R.id.btnHeaderLeft);

		mBtnSignup = (Button) findViewById(R.id.btnSignup);
		mBtnSignup.setEnabled(false);
		
		mBtnSignup.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registerUser(etName.getText().toString().trim(), etEmail.getText().toString().trim(), etBirthDay.getText().toString().trim(), etPassword.getText().toString().trim());
			}
		});
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
			}
		});

		etBirthDay.setKeyListener(null);
		etBirthDay.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					showDialog(DATE_DIALOG_ID);
				}
				return false;
			}
		});

		etName.addTextChangedListener(new TextchangeListner());
		etEmail.addTextChangedListener(new TextchangeListner());
		etPassword.addTextChangedListener(new TextchangeListner());
		etBirthDay.addTextChangedListener(new TextchangeListner());
		createIAgree();
		
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked && !isEditTextNull()){
					mBtnSignup.setClickable(true);
					mBtnSignup.setEnabled(true);
				}else{
					mBtnSignup.setClickable(false);
					mBtnSignup.setEnabled(false);
				}
			}
		});

	}

	private void createIAgree() {
		checkbox = (CheckBox) findViewById(R.id.checkBox1);
		TextView textView = (TextView) findViewById(R.id.textView2);

		checkbox.setText("");
		textView.setText(Html
				.fromHtml("I agree to the "
						+ "<a href='com.byb.gambler.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));
		textView.setClickable(true);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, cyear, cmonth,
					cday);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String date_selected = String.valueOf(monthOfYear + 1) + " /"
					+ String.valueOf(dayOfMonth) + " /" + String.valueOf(year);
			etBirthDay.setText(date_selected);
		}
	};

//	public void onClick(View v) {
//		Log.d("tag", "Btn Click");
//		UserInfo user = UserInfo.getInstance();
//
//		user.setFullName(etName.getText().toString().trim());
//		user.setBirthday(etBirthDay.getText().toString().trim());
//		user.setEmail(etEmail.getText().toString().trim());
//		user.setPassword(etPassword.getText().toString().trim());
//
////		mManager.registerViaEmail(this);
//	}

	private boolean isEditTextNull() {
		if (etEmail.getText() != null
				&& etEmail.getText().toString().trim().length() > 0
				&& etPassword.getText() != null
				&& etPassword.getText().toString().trim().length() > 0
				&& etBirthDay.getText() != null
				&& etBirthDay.getText().toString().trim().length() > 0
				&& etName.getText() != null
				&& etName.getText().toString().trim().length() > 0) {
			return false;
		}
		return true;
	}

	private class TextchangeListner implements TextWatcher {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (isEditTextNull() || !Utility.isEmailValid(etEmail.getText().toString().trim()) || (count == 0) || !checkbox.isChecked()) {
				mBtnSignup.setClickable(false);
				mBtnSignup.setEnabled(false);
			} else {
				mBtnSignup.setClickable(true);
				mBtnSignup.setEnabled(true);
			}
		}

	}
	
	private void registerUser(final String name, final String email,final String birthday, final String password) {
		if(Developer.getInstance().isNetworkConnected(RegisterActivity.this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						
						JSONObject jobj = new JSONObject();
						JSONObject mParams = new JSONObject();
						
						 mParams.put("birthday", birthday);
						 mParams.put("email", email);
						 mParams.put("fullName", name);
						 mParams.put("notifyWhenAdded", false);
						 mParams.put("notifyWhenLostABet", false);
						 mParams.put("notifyWhenWonABet", false);
						 mParams.put("password", password);
						 mParams.put("publicAccount", 0);
						
						 jobj.put("action", "create");
						 jobj.put("user", mParams);
						
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWS("users.json",
								postData,false,"");
						
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){
							JSONObject jsonData = new JSONObject(json.getString("data"));
							boolean successful = UserManager.getInstance().parseJson(jsonData,RegisterActivity.this);
							if(successful){
								startActivity(new Intent(RegisterActivity.this, RootTabScreen.class));
								finish();
								WelcomeActivity.welcomeActivity.finish();
								BYBPreferences.getInstance(RegisterActivity.this).setIsLoggedin(true,false);
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
