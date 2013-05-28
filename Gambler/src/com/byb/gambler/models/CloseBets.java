package com.byb.gambler.models;

public class CloseBets {

	private InitiatorUser mInitiatorUser;
	private AcceptedUser mAcceptedUser;
	private int mAcceptTime;
	private int mAcceptedUserID;
	private int mAmount;
	private String mBetsTeamID;
	private int mBetsTime;
	private int mInitiatorUserID;
	private int mLastModifiedTime;
	private String mMatchID;
	private int mMatchedAmount;
	private String mStatus;
	private int mMyBallsMatched;
	private String mResult;
	private int mID;
	
	public int getID() {
		return mID;
	}
	public void setID(int iD) {
		mID = iD;
	}
	public String getResult() {
		return mResult;
	}
	public void setResult(String result) {
		mResult = result;
	}
	public InitiatorUser getmInitiatorUser() {
		return mInitiatorUser;
	}
	public void setInitiatorUser(InitiatorUser mInitiatorUser) {
		this.mInitiatorUser = mInitiatorUser;
	}
	public AcceptedUser getAcceptedUser() {
		return mAcceptedUser;
	}
	public void setAcceptedUser(AcceptedUser mAcceptedUser) {
		this.mAcceptedUser = mAcceptedUser;
	}
	public int getAcceptTime() {
		return mAcceptTime;
	}
	public void setAcceptTime(int mAcceptTime) {
		this.mAcceptTime = mAcceptTime;
	}
	public int getAcceptedUserID() {
		return mAcceptedUserID;
	}
	public void setAcceptedUserID(int mAcceptedUserID) {
		this.mAcceptedUserID = mAcceptedUserID;
	}
	public int getAmount() {
		return mAmount;
	}
	public void setAmount(int mAmount) {
		this.mAmount = mAmount;
	}
	public String getBetsTeamID() {
		return mBetsTeamID;
	}
	public void setBetsTeamID(String mBetsTeamID) {
		this.mBetsTeamID = mBetsTeamID;
	}
	public int getBetsTime() {
		return mBetsTime;
	}
	public void setBetsTime(int mBetsTime) {
		this.mBetsTime = mBetsTime;
	}
	public int getInitiatorUserID() {
		return mInitiatorUserID;
	}
	public void setInitiatorUserID(int mInitiatorUserID) {
		this.mInitiatorUserID = mInitiatorUserID;
	}
	public int getLastModifiedTime() {
		return mLastModifiedTime;
	}
	public void setLastModifiedTime(int mLastModifiedTime) {
		this.mLastModifiedTime = mLastModifiedTime;
	}
	public String getMatchID() {
		return mMatchID;
	}
	public void setMatchID(String mMatchID) {
		this.mMatchID = mMatchID;
	}
	public int getMatchedAmount() {
		return mMatchedAmount;
	}
	public void setMatchedAmount(int mMatchedAmount) {
		this.mMatchedAmount = mMatchedAmount;
	}
	public String getStatus() {
		return mStatus;
	}
	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public int getMyBallsMatched() {
		return mMyBallsMatched;
	}
	public void setMyBallsMatched(int mMyBallsMatched) {
		this.mMyBallsMatched = mMyBallsMatched;
	}
	public int getMyBallsUnMatched() {
		return mMyBallsUnMatched;
	}
	public void setMyBallsUnMatched(int mMyBallsUnMatched) {
		this.mMyBallsUnMatched = mMyBallsUnMatched;
	}
	private int mMyBallsUnMatched;
}
