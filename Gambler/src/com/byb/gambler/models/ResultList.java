package com.byb.gambler.models;

public class ResultList {
	
	private String mID;
	private Team mTeamA;
	private Team mTeamB;
	private String mStartTime;
	private String mEndTime;
	private String mCloseTippingTime;
	private String mWonID;
	private String mResult;
	private CloseBets mCloseBets;
	
	public String getID() {
		return mID;
	}
	public void setID(String mID) {
		this.mID = mID;
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
	public String getCloseTippingTime() {
		return mCloseTippingTime;
	}
	public void setCloseTippingTime(String mCloseTippingTime) {
		this.mCloseTippingTime = mCloseTippingTime;
	}
	public String getWonID() {
		return mWonID;
	}
	public void setWonID(String mWonID) {
		this.mWonID = mWonID;
	}
	public String getResult() {
		return mResult;
	}
	public void setResult(String mResult) {
		this.mResult = mResult;
	}
	public CloseBets getCloseBets() {
		return mCloseBets;
	}
	public void setCloseBets(CloseBets mCloseBets) {
		this.mCloseBets = mCloseBets;
	}
	
	
	
}
