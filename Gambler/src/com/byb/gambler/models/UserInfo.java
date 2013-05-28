package com.byb.gambler.models;

public class UserInfo {
	
	private String mID;
	private String mFullName;
	private String mEmail;
	private String mBirthday;
	private String mThumbNailURL;
	private String mFullAvatarURL;
	private String mAccessToken;
	public String getAccessToken() {
		return mAccessToken;
	}

	public void setAccessToken(String mAccessToken) {
		this.mAccessToken = mAccessToken;
	}
	private UserStatistics mUserStatistics;
	private String mLocation;
	private int mLastSeen;
	private String mFB_ID;
	private String mFB_AccessToken;
	private String mPassword;
	private boolean mPublicAccount;
	private boolean mNotifyWhenWonABet;
	private boolean mNotifyWhenLostBet;
	private boolean mNotifWhenAdded;
	private int mInsertTime;
	
	
	public int getInsertTime() {
		return mInsertTime;
	}

	public void setInsertTime(int mInsertTime) {
		this.mInsertTime = mInsertTime;
	}
	private static UserInfo mUserInfo;
	
	public static UserInfo getInstance(){
		if(mUserInfo == null){
			mUserInfo = new UserInfo();
		}
		return mUserInfo;
	}
	
	public String getID() {
		return mID;
	}
	public void setID(String mID) {
		this.mID = mID;
	}
	public String getFullName() {
		return mFullName;
	}
	public void setFullName(String mFullName) {
		this.mFullName = mFullName;
	}
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public String getBirthday() {
		return mBirthday;
	}
	public void setBirthday(String mBirthday) {
		this.mBirthday = mBirthday;
	}
	public String getThumbNailURL() {
		return mThumbNailURL;
	}
	public void setThumbNailURL(String mThumbNailURL) {
		this.mThumbNailURL = mThumbNailURL;
	}
	public String getFullAvatarURL() {
		return mFullAvatarURL;
	}
	public void setFullAvatarURL(String mFullAvatarURL) {
		this.mFullAvatarURL = mFullAvatarURL;
	}
	public UserStatistics getUserStatistics() {
		return mUserStatistics;
	}
	public void setUserStatistics(UserStatistics mUserStatistics) {
		this.mUserStatistics = mUserStatistics;
	}
	public String getLocation() {
		return mLocation;
	}
	public void setLocation(String mLocation) {
		this.mLocation = mLocation;
	}
	public int getLastSeen() {
		return mLastSeen;
	}
	public void setLastSeen(int mLastSeen) {
		this.mLastSeen = mLastSeen;
	}
	public String getFB_ID() {
		return mFB_ID;
	}
	public void setFB_ID(String mFB_ID) {
		this.mFB_ID = mFB_ID;
	}
	public String getFB_AccessToken() {
		return mFB_AccessToken;
	}
	public void setFB_AccessToken(String mFB_AccessToken) {
		this.mFB_AccessToken = mFB_AccessToken;
	}
	public String getPassword() {
		return mPassword;
	}
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}
	public boolean isPublicAccount() {
		return mPublicAccount;
	}
	public void setPublicAccount(boolean mPublicAccount) {
		this.mPublicAccount = mPublicAccount;
	}
	public boolean isNotifyWhenWonABet() {
		return mNotifyWhenWonABet;
	}
	public void setNotifyWhenWonABet(boolean mNotifyWhenWonABet) {
		this.mNotifyWhenWonABet = mNotifyWhenWonABet;
	}
	public boolean isNotifyWhenLostBet() {
		return mNotifyWhenLostBet;
	}
	public void setNotifyWhenLostBet(boolean mNotifyWhenLostBet) {
		this.mNotifyWhenLostBet = mNotifyWhenLostBet;
	}
	public boolean isNotifWhenAdded() {
		return mNotifWhenAdded;
	}
	public void setNotifWhenAdded(boolean mNotifWhenAdded) {
		this.mNotifWhenAdded = mNotifWhenAdded;
	}

}
