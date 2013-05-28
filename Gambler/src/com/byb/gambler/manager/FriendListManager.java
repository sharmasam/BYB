package com.byb.gambler.manager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.byb.gambler.models.InvitedPerson;

import android.util.Log;

public class FriendListManager {

	private static FriendListManager mManager;

	public static FriendListManager getInstance(){
		if(mManager == null){
			mManager = new FriendListManager();
		}
		return mManager;
	}

	public ArrayList<InvitedPerson> parseData(JSONArray jsonArray){

		JSONObject jsonObject = new JSONObject();
		ArrayList<InvitedPerson> bybFriendList = new ArrayList<InvitedPerson>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {

				InvitedPerson person = new InvitedPerson();

				String data = jsonArray.getString(i);
				jsonObject = new JSONObject(data);
				if(jsonObject.has("facebookId")){
					person.setFacebookID(jsonObject.get("facebookId").toString());
				}

				if(jsonObject.has("meta")){
					JSONObject objMeta = new JSONObject(jsonObject.getString("meta"));
					if(objMeta.has("invitationPending")){
						Log.d("tag", "ID : " + objMeta.get("invitationPending"));
						person.setInvited(objMeta.getBoolean("invitationPending"));
					}
				}

				bybFriendList.add(person);
			} catch (JSONException e) {
				e.printStackTrace();
				return new ArrayList<InvitedPerson>();
			}
		}
		return bybFriendList;
	}
	
	
	public ArrayList<InvitedPerson> parseEmailData(JSONArray jsonArray){

		JSONObject jsonObject = new JSONObject();
		ArrayList<InvitedPerson> bybFriendList = new ArrayList<InvitedPerson>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {

				InvitedPerson person = new InvitedPerson();

				String data = jsonArray.getString(i);
				jsonObject = new JSONObject(data);
				if(jsonObject.has("email")){
					person.setEmail(jsonObject.get("email").toString());
				}

				if(jsonObject.has("meta")){
					JSONObject objMeta = new JSONObject(jsonObject.getString("meta"));
					if(objMeta.has("invitationPending")){
						person.setInvited(objMeta.getBoolean("invitationPending"));
					}
				}

				bybFriendList.add(person);
			} catch (JSONException e) {
				e.printStackTrace();
				return new ArrayList<InvitedPerson>();
			}
		}
		return bybFriendList;
	}
}
