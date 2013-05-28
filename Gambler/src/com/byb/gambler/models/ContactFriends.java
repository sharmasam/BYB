package com.byb.gambler.models;

public class ContactFriends {

	private String mName;
	private String mEmail;
	private boolean isInvited;
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String email) {
		mEmail = email;
	}
	public boolean isInvited() {
		return isInvited;
	}
	public void setInvited(boolean isInvited) {
		this.isInvited = isInvited;
	}

}
