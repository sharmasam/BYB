package com.byb.gambler.views;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.byb.gambler.R;
import com.byb.gambler.adapters.FriendListAdapter;
import com.byb.gambler.facebook.FacebookTools;
import com.byb.gambler.facebook.Friend;
import com.byb.gambler.manager.FriendListManager;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.ContactFriends;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.InvitedPerson;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.models.UserInfo;
import com.byb.gambler.utility.BYBPreferences;
import com.byb.gambler.utility.Constants;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.android.Facebook;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class FacebookFriendList {

	Facebook facebook = new Facebook("169177979911982");

	Dialog dialog;

	ArrayList<Friend> friends;
	FriendListAdapter friendListAdapter;
	ListView friendListView;
	private Context context;
	private  AutoCompleteTextView mAutoSearch;

	public FacebookFriendList(Context con) {
		context = con;
	}

	public void getFacebookFriendList(ListView listView, AutoCompleteTextView autocomplete) {
		//Get facebook access info
		/*		final Intent recieve_intent = getIntent();
		String access_token = recieve_intent.getStringExtra(Constants.bundle_Access_Token);
		long expires = recieve_intent.getLongExtra(Constants.bundle_Access_Expires, 0);
		if(access_token != null) facebook.setAccessToken(access_token);
		if(expires != 0) facebook.setAccessExpires(expires);
		 */

		facebook.setAccessToken(BYBPreferences.getInstance(context).getFBSessionToken());
		facebook.setAccessExpires(BYBPreferences.getInstance(context).getFBExpiry());
		friends = new ArrayList<Friend>();

		//Show dialog to distract user while downloading friends
		dialog = new ProgressDialog(context);
		dialog.show();
		dialog.setContentView(R.layout.custom_progress_dialog);


		//Load friends if cached
		String jsonFriends = FacebookTools.getStringFromFile(getFriendsJSONCacheFile());
		Log.d("tag", "Facebook friend Response : " + jsonFriends);
		if(jsonFriends != null){
			parseJSONFriendsToArrayList(jsonFriends,friends);
			dialog.cancel();

			/*for(Friend f : friends){
				File file = new File(context.getCacheDir(), f.getUID());
				Bitmap b = FacebookTools.getBitmapFromFile(file);
				if(b != null) f.setProfilePic(b);
			}*/
		}

		getBYBFriendList();

		friendListAdapter = new FriendListAdapter(context, friends);//new FriendListAdapter(FriendList.this, friends);
		friendListView = listView;//(ListView) findViewById(R.id.friend_list);
		friendListView.setAdapter(friendListAdapter);

		friendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				position = position-1;
				sendRequestDialog(friends.get(position).getUID());
			}

		});

		mAutoSearch = autocomplete;

		mAutoSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				contactAutoSearch();
				friendListAdapter.setNewList(friends);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


		//Download all friends
		downloadFacebookFriends_async();
	}

	//Download friends and put them in cache
	protected void downloadFacebookFriends_async() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				//Get all friends with FQL
				Bundle params = new Bundle();
				params.putString("method", "fql.query");
				params.putString("query", "SELECT name, uid, pic_square FROM user WHERE uid IN (select uid2 from friend where uid1=me()) order by name");

				try {
					String response = facebook.request(params);
					File file = new File(context.getCacheDir(), Constants.cache_JSON_Friends);
					FacebookTools.saveStringToFile(response, file);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				friendDownloadCompleteHandler.sendEmptyMessage(0);
			}

		}).start();
	}

	private void parseJSONFriendsToArrayList(String jsonFriends, ArrayList<Friend> arraylist) {
		try {
			JSONArray array = new JSONArray(jsonFriends);
			for(int i = 0; i < array.length(); i++){
				arraylist.add(new Friend(array.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected Handler friendDownloadCompleteHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){

			if(updateToLatestFriendList() || friends.size() == 0){
				//TODO: should notify user
				friendListAdapter = new FriendListAdapter(context, friends);
				friendListView.setAdapter(friendListAdapter);
				getBYBFriendList();
			}

			//			downloadProfilePictures_async();
			dialog.cancel();

		}
	};

	/*protected void downloadProfilePictures_async() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < friends.size(); i++){
					int bestIndex = getBestFriend(i);
					final Friend f = friends.get(bestIndex);

					if(f.hasDownloadedProfilePicture()) continue;

					File file = new File(context.getCacheDir(), f.getUID());
					Bitmap picture = FacebookTools.getBitmapFromFile(file); 

					if(picture == null){
						picture = FacebookTools.downloadBitmap(f.getProfilePictureURL());
					}
					if(picture != null){
						f.setProfilePic(picture);
						FacebookTools.storePictureToFile(file, picture);
					}

					updateProfilePictureAtIndex(f, bestIndex);

					//need to retry to download the old picture
					if(bestIndex != i) i--;
				}

			}

			//Updates the picture in the listView
			private void updateProfilePictureAtIndex(final Friend f, final int index) {
				new Thread(new Runnable()  {
					@Override
					public void run() {
						View v = friendListView.getChildAt(index - friendListView.getFirstVisiblePosition());
						if(v != null){
							ImageView iv = (ImageView) v.findViewById(R.id.profile_picture);
							if(f.hasDownloadedProfilePicture()){
								iv.setImageBitmap(f.getProfilePicture());
							}
						}
					}
				});
			}

			//Simple method to find the best friend to download right now (one that the user is looking at is better than one that is not visible)
			private int getBestFriend(int index) {
				int viewingIndex = friendListView.getFirstVisiblePosition();
				int lastViewingIndex = friendListView.getLastVisiblePosition();
				while(viewingIndex <= lastViewingIndex && viewingIndex <= friends.size()){
					if(!friends.get(viewingIndex).hasDownloadedProfilePicture()){
						index = viewingIndex;
						break;
					}
					viewingIndex++;
				}

				return index;
			}

		}).start();

	}*/

	//Updates friends to friends found in cache (returns true if updated)
	protected boolean updateToLatestFriendList() {
		ArrayList<Friend> newFriendList = new ArrayList<Friend>();
		String jsonFriends = FacebookTools.getStringFromFile(getFriendsJSONCacheFile());
		parseJSONFriendsToArrayList(jsonFriends, newFriendList);

		if(!friendListsAreEqual(friends,newFriendList)){
			friends = newFriendList;
			return true;
		}

		return false;
	}

	private boolean friendListsAreEqual(ArrayList<Friend> a, ArrayList<Friend> b) {
		if(a.size() != b.size()) return false;
		for(int i = 0; i < a.size(); i++) if(!a.get(i).equals(b.get(i))) return false;
		return true;
	}

	private File getFriendsJSONCacheFile(){
		return new File(context.getCacheDir(), Constants.cache_JSON_Friends);
	}

	private ArrayList<InvitedPerson> mBybFriendList;

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
					mBybFriendList = FriendListManager.getInstance().parseData(jsonArray);
					updateInvitedFriend();
					//					createNewFriendList();
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	private void updateInvitedFriend() {

		if(mBybFriendList.size() !=0 ){
			for (Friend frnd : friends) {
				for (InvitedPerson person : mBybFriendList) {
					if(person.getFacebookID().equalsIgnoreCase(frnd.getUID())){
						frnd.setInvited(person.isInvited());
					}
				}
			}
		}
		mContactList = friends;
		friendListAdapter.setNewList(friends);
	}


	private ArrayList<Friend> mContactList = new ArrayList<Friend>();
	ArrayList<Friend> selContactList = new ArrayList<Friend>();
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
			friends = selContactList;
		} else {
			friends = mContactList;
		}

	}


	/*private void createNewFriendList() {
		ArrayList<Friend> newFriendList = new ArrayList<Friend>();
		if(mBybFriendList.size() != 0){
			for (Friend friend : friends) {
				for (int i = 0; i < mBybFriendList.size(); i++) {
					if(!mBybFriendList.get(i).equalsIgnoreCase(friend.getUID())){
						newFriendList.add(friend);
					}
				}
			}
			friends = newFriendList;
			friendListAdapter.setNewList(friends);
		}
	}*/

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
						for (Friend frnd : friends) {
							frnArray.put(frnd.getUID());
						}

						JSONObject jobj = new JSONObject();
						jobj.put("facebook_ids", frnArray);
						//						jobj.put("emails", frnArray);
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

	private void sendRequestDialog(final String ID) {
		Bundle params = new Bundle();
		params.putString("message", "Send BYB friend invite.");

		WebDialog requestsDialog = (
				new WebDialog.RequestsDialogBuilder(context,
						facebook.getSession(),
						params)).setTo(ID)

						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error != null) {
									if (error instanceof FacebookOperationCanceledException) {
										Toast.makeText(context.getApplicationContext(), 
												"Request cancelled", 
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(context.getApplicationContext(), 
												"Network Error", 
												Toast.LENGTH_SHORT).show();
									}
								} else {
									final String requestId = values.getString("request");
									if (requestId != null) {
										sendInvitation(ID);
										Toast.makeText(context.getApplicationContext(), 
												"Request sent",  
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(context.getApplicationContext(), 
												"Request cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								}   
							}

						})
						.build();
		requestsDialog.show();
	}



	private void sendInvitation(final String FBID){
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

						person.put("facebookId", FBID);
						person.put("email", "");
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