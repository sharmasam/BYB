package com.byb.gambler.models;

public class OpenBets {
	private String mAcceptTime;
	private String mAcceptedUser;
	private String mAmount;
	private String mBetTeamID;
	private String mBetTime;
	private String mID;
	private String mInitiatorUserID;
	private String mLastModified;
	private Match mMatch;
	private InitiatorUser mInitiatorUser;
	
	public InitiatorUser getInitiatorUser() {
		return mInitiatorUser;
	}
	public void setInitiatorUser(InitiatorUser mInitiatorUser) {
		this.mInitiatorUser = mInitiatorUser;
	}
	public String getAcceptTime() {
		return mAcceptTime;
	}
	public void setAcceptTime(String mAcceptTime) {
		this.mAcceptTime = mAcceptTime;
	}
	public String getAcceptedUser() {
		return mAcceptedUser;
	}
	public void setAcceptedUser(String mAcceptedUser) {
		this.mAcceptedUser = mAcceptedUser;
	}
	public Match getMatch() {
		return mMatch;
	}
	public void setMatch(Match mMatch) {
		this.mMatch = mMatch;
	}
	public String getAmount() {
		return mAmount;
	}
	public void setAmount(String mAmount) {
		this.mAmount = mAmount;
	}
	public String getBetTeamID() {
		return mBetTeamID;
	}
	public void setBetTeamID(String mBetTeamID) {
		this.mBetTeamID = mBetTeamID;
	}
	public String getBetTime() {
		return mBetTime;
	}
	public void setBetTime(String mBetTime) {
		this.mBetTime = mBetTime;
	}
	public String getID() {
		return mID;
	}
	public void setID(String mID) {
		this.mID = mID;
	}
	public String getInitiatorUserID() {
		return mInitiatorUserID;
	}
	public void setInitiatorUserID(String mInitiatorUserID) {
		this.mInitiatorUserID = mInitiatorUserID;
	}
	public String getLastModified() {
		return mLastModified;
	}
	public void setLastModified(String mLastModified) {
		this.mLastModified = mLastModified;
	}

}
