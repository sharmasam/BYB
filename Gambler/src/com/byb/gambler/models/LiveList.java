package com.byb.gambler.models;

public class LiveList {
	
	private String mID;
	private Team mTeamA;
	private Team mTeamB;
	private String mStartTime;
	private String mEndTime;
	private String mCloseTippingTime;
	public String getID() {
		return mID;
	}
	public void setID(String iD) {
		mID = iD;
	}
	public Team getTeamA() {
		return mTeamA;
	}
	public void setTeamA(Team teamA) {
		mTeamA = teamA;
	}
	public Team getTeamB() {
		return mTeamB;
	}
	public void setTeamB(Team teamB) {
		mTeamB = teamB;
	}
	public String getStartTime() {
		return mStartTime;
	}
	public void setStartTime(String startTime) {
		mStartTime = startTime;
	}
	public String getEndTime() {
		return mEndTime;
	}
	public void setEndTime(String endTime) {
		mEndTime = endTime;
	}
	public String getCloseTippingTime() {
		return mCloseTippingTime;
	}
	public void setCloseTippingTime(String closeTippingTime) {
		mCloseTippingTime = closeTippingTime;
	}
	

}
