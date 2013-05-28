/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.byb.gambler.views;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.byb.gambler.R;
import com.byb.gambler.adapters.ContactFriendListAdapter;
import com.byb.gambler.manager.FriendListManager;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.ContactFriends;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.InvitedPerson;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.models.UserInfo;

public class ContactFriendList {

	private Context context;
	private ArrayList<ContactFriends> frndList;
	private ContactFriendListAdapter adapter;
	private AutoCompleteTextView mAutoSearch;

	public ContactFriendList(Context con) {
		context = con;
	}

	public void getContactList(ListView listView,AutoCompleteTextView autoSearch) {
		mAutoSearch = autoSearch;
		adapter = new ContactFriendListAdapter(context, getNameEmailDetails());
		listView.setAdapter(adapter);

		getBYBFriendList();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				position = position - 1;
				sendInvitation(frndList.get(position).getEmail().toString());
			}

		});

		mAutoSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				contactAutoSearch();
				adapter.setNewList(frndList);
				

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}
	
	private ArrayList<ContactFriends> mContactList = new ArrayList<ContactFriends>();
	ArrayList<ContactFriends> selContactList = new ArrayList<ContactFriends>();
	private void contactAutoSearch() {
		if (mAutoSearch.getText().toString().length() > 0) {
			selContactList.clear();
			for (int i = 0; i < mContactList.size(); i++) {
				if (mContactList
						.get(i)
						.getName()
						.toString()

						.toUpperCase()
						.contains(
								mAutoSearch.getText().toString().toUpperCase())) {
						selContactList.add(mContactList.get(i));
				}

			}
			frndList = selContactList;
		} else {
			frndList = mContactList;
		}

	}

	public ArrayList<ContactFriends> getNameEmailDetails() {
		ArrayList<String> emlRecs = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
		frndList = new ArrayList<ContactFriends>();
		HashSet<String> emlRecsHS = new HashSet<String>();
		ContentResolver cr = context.getContentResolver();
		String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID, 
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.PHOTO_ID,
				ContactsContract.CommonDataKinds.Email.DATA, 
				ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
		String order = "CASE WHEN " 
				+ ContactsContract.Contacts.DISPLAY_NAME 
				+ " NOT LIKE '%@%' THEN 1 ELSE 2 END, " 
				+ ContactsContract.Contacts.DISPLAY_NAME 
				+ ", " 
				+ ContactsContract.CommonDataKinds.Email.DATA
				+ " COLLATE NOCASE";
		String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
		Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
		if (cur.moveToFirst()) {
			do {
				// names comes in hand sometimes
				String name = cur.getString(1);
				String emlAddr = cur.getString(3);

				// keep unique only
				if (emlRecsHS.add(emlAddr.toLowerCase()) && !nameList.contains(name)) {
					emlRecs.add(emlAddr);
					nameList.add(name);

					ContactFriends frnd = new ContactFriends();
					frnd.setEmail(emlAddr);
					frnd.setName(name);
					frnd.setInvited(false);
					frndList.add(frnd);
				}
			} while (cur.moveToNext());
		}

		cur.close();
		System.out.println("Contacts with email id"+emlRecs);
		return frndList;
	}


	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,
						context);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						context);
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(context, R.string.processing, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						context);
				break;
			case 5:
				bun = msg.getData();
				try{
					message = bun.getString("msg");
					JSONArray jsonArray = new JSONArray(message);
					ArrayList<InvitedPerson> invitedList = FriendListManager.getInstance().parseEmailData(jsonArray);
					updateInvitedFriend(invitedList);
					//					createNewFriendList();
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		};
	};

	private void updateInvitedFriend(ArrayList<InvitedPerson> invitedList) {

		if(invitedList.size() != 0){
			for (ContactFriends frnd : frndList) {
				for (InvitedPerson person : invitedList) {
					if(person.getEmail().equalsIgnoreCase(frnd.getEmail())){
						Log.d("tag", "Is invited " + person.isInvited() + ">>>>>>>>>. Email :" +person.getEmail() ) ;
						frnd.setInvited(person.isInvited());
					}
				}
			}
		}
		mContactList = frndList;
		adapter.setNewList(frndList);
	}

	private void getBYBFriendList(){
		if(Developer.getInstance().isNetworkConnected(context)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {

						String text = UserInfo.getInstance().getID()+":"+UserInfo.getInstance().getAccessToken();
						System.out.println("Text = "+text);
						// Sending side
						byte[] data = null;
						data = text.getBytes("UTF-8");
						String base64 = Base64.encodeToString(data, Base64.DEFAULT);

						JSONArray frnArray = new JSONArray();
						//						frnArray.put("a@b.com");
						/*frnArray.put("11805434");
						frnArray.put("606490068");
						frnArray.put("718001554");
						frnArray.put("762133241");
						frnArray.put("1183992592");
						frnArray.put("1367660643");
						frnArray.put("1741759086");
						frnArray.put("100000154382939");
						frnArray.put("100000871391974");
						frnArray.put("100005095274564");
						 */
						for (ContactFriends frnd : frndList) {
							frnArray.put(frnd.getEmail());
						}

						JSONObject jobj = new JSONObject();
						jobj.put("emails", frnArray);
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWSforPost("friends/users.json",
								jobj.toString(),true,base64);

						Log.d("tag", "friend : " +response);
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 5;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							msg.setData(bun);
							mHandler.sendMessage(msg);

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
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);

	}


	private void sendInvitation(final String email){
		if(Developer.getInstance().isNetworkConnected(context)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {

						String text = UserInfo.getInstance().getID()+":"+UserInfo.getInstance().getAccessToken();
						System.out.println("Text = "+text);
						// Sending side
						byte[] data = null;
						data = text.getBytes("UTF-8");
						String base64 = Base64.encodeToString(data, Base64.DEFAULT);

						JSONArray personArray = new JSONArray();
						JSONObject person = new JSONObject();

						person.put("facebookId", "");
						person.put("email", email);
						person.put("fullName", "");
						person.put("location", "");
						person.put("birthday", "");

						personArray.put(person);

						JSONObject jobj = new JSONObject();
						jobj.put("people", personArray);
						//						jobj.put("emails", frnArray);
						Log.e("json string :", jobj.toString());

						byte[] postData = jobj.toString().getBytes();

						String response = UserManager.getInstance().callWSforPost("friends/facebook_invited.json",
								jobj.toString(),true,base64);

						Log.d("tag", "friend : " +response);
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 5;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							msg.setData(bun);
							mHandler.sendMessage(msg);

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
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);

	}

}
