package heinrisch.friendlist.view;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.android.Facebook;

public class FriendList extends Activity {

	Facebook facebook = new Facebook("424405194241987");

	Dialog dialog;

	ArrayList<Friend> friends;

	FriendListAdapter friendListAdapter;
	ListView friendListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friend_list);
		//Get facebook access info
		final Intent recieve_intent = getIntent();
		String access_token = recieve_intent.getStringExtra(Constants.bundle_Access_Token);
		long expires = recieve_intent.getLongExtra(Constants.bundle_Access_Expires, 0);
		if(access_token != null) facebook.setAccessToken(access_token);
		if(expires != 0) facebook.setAccessExpires(expires);

		friends = new ArrayList<Friend>();

		//Show dialog to distract user while downloading friends
		dialog = new ProgressDialog(this);
		dialog.show();
		dialog.setContentView(R.layout.custom_progress_dialog);


		//Load friends if cached
		String jsonFriends = Tools.getStringFromFile(getFriendsJSONCacheFile());
		if(jsonFriends != null){
			parseJSONFriendsToArrayList(jsonFriends,friends);
			dialog.cancel();

			for(Friend f : friends){
				File file = new File(getCacheDir(), f.getUID());
				Bitmap b = Tools.getBitmapFromFile(file);
				if(b != null) f.setProfilePic(b);
			}
		}

		friendListAdapter = new FriendListAdapter(FriendList.this, friends);
		friendListView = (ListView) findViewById(R.id.friend_list);
		friendListView.setAdapter(friendListAdapter);


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
					File file = new File(getCacheDir(), Constants.cache_JSON_Friends);
					Tools.saveStringToFile(response, file);

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
				friendListAdapter = new FriendListAdapter(FriendList.this, friends);
				friendListView.setAdapter(friendListAdapter);
			}

			downloadProfilePictures_async();
			dialog.cancel();

		}
	};

	protected void downloadProfilePictures_async() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < friends.size(); i++){
					int bestIndex = getBestFriend(i);
					final Friend f = friends.get(bestIndex);

					if(f.hasDownloadedProfilePicture()) continue;

					File file = new File(getCacheDir(), f.getUID());
					Bitmap picture = Tools.getBitmapFromFile(file); 

					if(picture == null){
						picture = Tools.downloadBitmap(f.getProfilePictureURL());
					}
					if(picture != null){
						f.setProfilePic(picture);
						Tools.storePictureToFile(file, picture);
					}

					updateProfilePictureAtIndex(f, bestIndex);

					//need to retry to download the old picture
					if(bestIndex != i) i--;
				}

			}

			//Updates the picture in the listView
			private void updateProfilePictureAtIndex(final Friend f, final int index) {
				runOnUiThread(new Runnable() {
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

	}

	//Updates friends to friends found in cache (returns true if updated)
	protected boolean updateToLatestFriendList() {
		ArrayList<Friend> newFriendList = new ArrayList<Friend>();
		String jsonFriends = Tools.getStringFromFile(getFriendsJSONCacheFile());
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
		return new File(getCacheDir(), Constants.cache_JSON_Friends);
	}

}