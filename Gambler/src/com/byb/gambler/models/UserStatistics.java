package com.byb.gambler.models;

public class UserStatistics {
	private int mBallsBet;
	private int mBallsLost;
	private int mTipsMade;
	private int mTipsWon;
	private int mNumberOfBadges;
	private int mBallsAvailable;
	private int mBallsInAir;
	private int mBallsAwaitingGrab;

	public int getBallsInAir() {
		return mBallsInAir;
	}
	public void setBallsInAir(int mBallsInAir) {
		this.mBallsInAir = mBallsInAir;
	}
	public int getBallsAwaitingGrab() {
		return mBallsAwaitingGrab;
	}
	public void setBallsAwaitingGrab(int mBallsAwaitingGrab) {
		this.mBallsAwaitingGrab = mBallsAwaitingGrab;
	}
	public int getBallsAvailable() {
		return mBallsAvailable;
	}
	public void setBallsAvailable(int mBallsAvailable) {
		this.mBallsAvailable = mBallsAvailable;
	}
	public int getBallsBet() {
		return mBallsBet;
	}
	public void setBallsBet(int mBallsBet) {
		this.mBallsBet = mBallsBet;
	}
	public int getBallsLost() {
		return mBallsLost;
	}
	public void setBallsLost(int mBallsLost) {
		this.mBallsLost = mBallsLost;
	}
	public int gettipsMade() {
		return mTipsMade;
	}
	public void settipsMade(int mtipsMade) {
		this.mTipsMade = mtipsMade;
	}
	public int getTipsWon() {
		return mTipsWon;
	}
	public void setTipsWon(int mTipsWon) {
		this.mTipsWon = mTipsWon;
	}
	public int getNumberOfBadges() {
		return mNumberOfBadges;
	}
	public void setNumberOfBadges(int mNumberOfBadges) {
		this.mNumberOfBadges = mNumberOfBadges;
	}
	
}
