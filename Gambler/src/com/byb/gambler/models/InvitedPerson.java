package com.byb.gambler.models;

public class InvitedPerson {
	
	private String mFacebookID;
	private boolean ismInvited;
	private String mEmail;
	
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public boolean isInvited() {
		return ismInvited;
	}
	public void setInvited(boolean isInvited) {
		this.ismInvited = isInvited;
	}
	public String getFacebookID() {
		return mFacebookID;
	}
	public void setFacebookID(String mFacebookID) {
		this.mFacebookID = mFacebookID;
	}
}
