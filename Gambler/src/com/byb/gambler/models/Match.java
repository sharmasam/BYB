package com.byb.gambler.models;

public class Match {

	private String mID;
	private LeagueList mLeague;
	private String mStartTime;
	private String mEndTime;
	private Team mTeamA;
	private Team mTeamB;
	private String mWonTeamID;
	private String mResult;
	private String mLocation;
	private String mCloseTippingTime;
	private String mStatus;
	private String mMatchedAmount;
	
	public String getStatus() {
		return mStatus;
	}
	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public String getMatchedAmount() {
		return mMatchedAmount;
	}
	public void setMatchedAmount(String mMatchedAmount) {
		this.mMatchedAmount = mMatchedAmount;
	}
	public String getCloseTippingTime() {
		return mCloseTippingTime;
	}
	public void setCloseTippingTime(String mCloseTippingTime) {
		this.mCloseTippingTime = mCloseTippingTime;
	}
	public String getID() {
		return mID;
	}
	public void setID(String mID) {
		this.mID = mID;
	}
	public LeagueList getLeague() {
		return mLeague;
	}
	public void setLeague(LeagueList mLeague) {
		this.mLeague = mLeague;
	}
	public String getStartTime() {
		return mStartTime;
	}
	public void setStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}
	public String getEndTime() {
		return mEndTime;
	}
	public void setEndTime(String mEndTime) {
		this.mEndTime = mEndTime;
	}
	public Team getTeamA() {
		return mTeamA;
	}
	public void setTeamA(Team mTeamA) {
		this.mTeamA = mTeamA;
	}
	public Team getTeamB() {
		return mTeamB;
	}
	public void setTeamB(Team mTeamB) {
		this.mTeamB = mTeamB;
	}
	public String getWonTeamID() {
		return mWonTeamID;
	}
	public void setWonTeamID(String mWonTeamID) {
		this.mWonTeamID = mWonTeamID;
	}
	public String getResult() {
		return mResult;
	}
	public void setResult(String mResult) {
		this.mResult = mResult;
	}
	public String getLocation() {
		return mLocation;
	}
	public void setLocation(String mLocation) {
		this.mLocation = mLocation;
	}
}
