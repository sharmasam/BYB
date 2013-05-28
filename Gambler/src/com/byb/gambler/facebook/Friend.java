package com.byb.gambler.facebook;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.byb.gambler.utility.Constants;


public class Friend {
	private String name;
	private String profilePictureURL;
	private String uid;
	private boolean ismInvited;
//	private Bitmap profilePicture = null;
	
	
	public boolean isInvited() {
		return ismInvited;
	}


	public void setInvited(boolean isInvited) {
		this.ismInvited = isInvited;
	}


	public Friend(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.profilePictureURL = json.getString("pic_square");
        this.uid = json.getString("uid");
        this.ismInvited = false;
	}


	public CharSequence getName() {
		return name;
	}
	
	public String getProfilePictureURL(){
		return profilePictureURL;
	}


	/*public void setProfilePic(Bitmap b) {
		this.profilePicture = Bitmap.createScaledBitmap(b, Constants.size_Profile_Picture_Width, Constants.size_Profile_Picture_Heigth, true);
		
	}

	public boolean hasDownloadedProfilePicture() {
		return profilePicture != null;
	}


	public Bitmap getProfilePicture() {
		return profilePicture;
	}*/
	
	public String getUID(){
		return uid;
	}
	
	@Override
	public boolean equals(Object o) {
		return uid.equals(((Friend)o).uid);
	}
	
}
