package com.byb.gambler.manager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.byb.gambler.models.AcceptedUser;
import com.byb.gambler.models.CloseBets;
import com.byb.gambler.models.InitiatorUser;
import com.byb.gambler.models.LeagueList;
import com.byb.gambler.models.LiveList;
import com.byb.gambler.models.ResultList;
import com.byb.gambler.models.Team;
import com.byb.gambler.models.ThisWeekList;

public class PlayManager {

	private static PlayManager mManager;

	public static PlayManager getInstance() {
		if(mManager == null){
			mManager = new PlayManager();
		}
		return mManager;
	}

	public ArrayList<LeagueList> parseLeagueList(JSONArray jsonArray) {
		JSONObject jsonClinic;
		JSONObject json;
		ArrayList<LeagueList> leagueList = new ArrayList<LeagueList>();

		try{
			for(int i = 0;i<jsonArray.length();i++){
				LeagueList list = new LeagueList();
				String clinic1 = jsonArray.getString(i);
				jsonClinic = new JSONObject(clinic1);
				if(jsonClinic.has("number_of_matches")){
					list.setNumberOfMatch(jsonClinic.getString("number_of_matches"));
				}
				if(jsonClinic.has("league")){
					json = new JSONObject(jsonClinic.getString("league"));
					list.setName(json.getString("name"));
					list.setSports(json.getString("sport"));
					list.setID(json.getString("id"));	
				}
				leagueList.add(list);
			}
			Log.d("tag", "League list :"+ leagueList.size());;
			return leagueList;
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}


	public ArrayList<ThisWeekList> parseThisWeekList(JSONArray jsonArray){
		JSONObject jsonObject;
		ArrayList<ThisWeekList> arrayThisWeek = new ArrayList<ThisWeekList>();
		try{

			for(int i = 0;i<jsonArray.length();i++){
				ThisWeekList match = new ThisWeekList();

				String clinic1 = jsonArray.getString(i);
				jsonObject = new JSONObject(clinic1);
				if(jsonObject.has("id")){
					match.setID(jsonObject.getString("id"));
				}
				if(jsonObject.has("startTime")){
					match.setStartTime(jsonObject.getString("startTime"));
				}
				if(jsonObject.has("endTime")){
					match.setEndTime(jsonObject.getString("endTime"));
				}
				if(jsonObject.has("closeTippingTime")){
					match.setCloseTippingTime(jsonObject.getString("closeTippingTime"));
				}
				if(jsonObject.has("teamA")){
					JSONObject jsonTeam = new JSONObject(jsonObject.getString("teamA"));
					Team team = new Team();
					team.setID(jsonTeam.getString("id"));
					team.setName(jsonTeam.getString("name"));
					team.setLogoImage(jsonTeam.getString("logoImageURL"));
					match.setTeamA(team);
				}

				if(jsonObject.has("teamB")){
					JSONObject jsonTeam = new JSONObject(jsonObject.getString("teamB"));
					Team team = new Team();
					team.setID(jsonTeam.getString("id"));
					team.setName(jsonTeam.getString("name"));
					team.setLogoImage(jsonTeam.getString("logoImageURL"));
					match.setTeamB(team);
				}
				arrayThisWeek.add(match);
			}


		}catch(JSONException e){
			e.printStackTrace();
		}
		return arrayThisWeek;
	}


	public ArrayList<LiveList> parseLiveList(JSONArray jsonArray){
		JSONObject jsonObject;
		ArrayList<LiveList> arrayThisWeek = new ArrayList<LiveList>();
		try{

			for(int i = 0;i<jsonArray.length();i++){
				LiveList match = new LiveList();

				String clinic1 = jsonArray.getString(i);
				jsonObject = new JSONObject(clinic1);
				if(jsonObject.has("id")){
					match.setID(jsonObject.getString("id"));
				}
				if(jsonObject.has("startTime")){
					match.setStartTime(jsonObject.getString("startTime"));
				}
				if(jsonObject.has("endTime")){
					match.setEndTime(jsonObject.getString("endTime"));
				}
				if(jsonObject.has("closeTippingTime")){
					match.setCloseTippingTime(jsonObject.getString("closeTippingTime"));
				}
				if(jsonObject.has("teamA")){
					JSONObject jsonTeam = new JSONObject(jsonObject.getString("teamA"));
					Team team = new Team();
					team.setID(jsonTeam.getString("id"));
					team.setName(jsonTeam.getString("name"));
					team.setLogoImage(jsonTeam.getString("logoImageURL"));
					match.setTeamA(team);
				}

				if(jsonObject.has("teamB")){
					JSONObject jsonTeam = new JSONObject(jsonObject.getString("teamB"));
					Team team = new Team();
					team.setID(jsonTeam.getString("id"));
					team.setName(jsonTeam.getString("name"));
					team.setLogoImage(jsonTeam.getString("logoImageURL"));
					match.setTeamB(team);
				}
				arrayThisWeek.add(match);
			}


		}catch(JSONException e){
			e.printStackTrace();
		}
		return arrayThisWeek;
	}


	public ArrayList<ResultList> parseResultList(JSONArray jsonArray){
		JSONObject jsonObject;
		ArrayList<ResultList> arrayThisWeek = new ArrayList<ResultList>();
		try{

			for(int i = 0;i<jsonArray.length();i++){
				ResultList match = new ResultList();

				Log.d("tag", "See Here : " + jsonArray.getString(i));
				String clinic1 = jsonArray.getString(i);
				jsonObject = new JSONObject(clinic1);
				if(jsonObject.has("id")){
					match.setID(jsonObject.getString("id"));
				}
				if(jsonObject.has("startTime")){
					match.setStartTime(jsonObject.getString("startTime"));
				}
				if(jsonObject.has("endTime")){
					match.setEndTime(jsonObject.getString("endTime"));
				}
				if(jsonObject.has("closeTippingTime")){
					match.setCloseTippingTime(jsonObject.getString("closeTippingTime"));
				}
				if(jsonObject.has("wonTeamId")){
					match.setWonID(jsonObject.getString("wonTeamId"));
				}
				if(jsonObject.has("result")){
					match.setResult(jsonObject.getString("result"));
				}
				if(jsonObject.has("teamA")){
					JSONObject jsonTeam = new JSONObject(jsonObject.getString("teamA"));
					Team team = new Team();
					team.setID(jsonTeam.getString("id"));
					team.setName(jsonTeam.getString("name"));
					team.setLogoImage(jsonTeam.getString("logoImageURL"));
					match.setTeamA(team);
				}

				if(jsonObject.has("teamB")){
					JSONObject jsonTeam = new JSONObject(jsonObject.getString("teamB"));
					Team team = new Team();
					team.setID(jsonTeam.getString("id"));
					team.setName(jsonTeam.getString("name"));
					team.setLogoImage(jsonTeam.getString("logoImageURL"));
					match.setTeamB(team);
				}
				
				CloseBets close = new CloseBets();
				
				if(jsonObject.has("matchMetaData")){
					JSONObject jsonMatch = new JSONObject(jsonObject.getString("matchMetaData"));
					if(jsonMatch.has("closedBets")){
						JSONObject jsonClose  = new JSONObject(jsonObject.getString("closedBets"));
						close.setAcceptTime(jsonClose.getInt("acceptTime"));
						close.setAmount(jsonClose.getInt("amount"));
						close.setBetsTeamID(jsonClose.getString("betTeamId"));
						close.setAcceptedUserID(jsonClose.getInt("acceptedUserId"));
						close.setBetsTime(jsonClose.getInt("betTime"));
						close.setInitiatorUserID(jsonClose.getInt("initiatorUserId"));
						close.setLastModifiedTime(jsonClose.getInt("lastModifiedTime"));
						close.setMatchID(jsonClose.getString("matchId"));
						close.setMatchedAmount(jsonClose.getInt("matchedAmount"));
						close.setResult(jsonClose.getString("result"));
						close.setStatus(jsonClose.getString("status"));
						close.setID(jsonClose.getInt("id"));
						
						close.setMyBallsMatched(jsonClose.getInt("myBallsMatched"));
						close.setMyBallsUnMatched(jsonClose.getInt("myBallsUnmatched"));
						
						if(jsonClose.has("acceptedUser")){
							JSONObject jsonAcceptedUser  = new JSONObject(jsonObject.getString("acceptedUser"));
							
							InitiatorUser iuser = new InitiatorUser();
							
							iuser.setBirthday(jsonAcceptedUser.getString("birthday"));
							iuser.setFullName(jsonAcceptedUser.getString("fullName"));
							iuser.setID(jsonAcceptedUser.getString("id"));
							iuser.setLastSeen(jsonAcceptedUser.getString("lastSeen"));
							iuser.setThumbNail(jsonAcceptedUser.getString("thumbnailAvatarURL"));
							
							close.setInitiatorUser(iuser);
						}
						
						if(jsonClose.has("initiatorUser")){
							JSONObject jsonAcceptedUser  = new JSONObject(jsonObject.getString("initiatorUser"));
							
							AcceptedUser iuser = new AcceptedUser();
							
							iuser.setBirthday(jsonAcceptedUser.getString("birthday"));
							iuser.setFullName(jsonAcceptedUser.getString("fullName"));
							iuser.setID(jsonAcceptedUser.getString("id"));
							iuser.setLastSeen(jsonAcceptedUser.getString("lastSeen"));
							iuser.setThumbNail(jsonAcceptedUser.getString("thumbnailAvatarURL"));
							
							close.setAcceptedUser(iuser);
						}
					}
				}
				
				match.setCloseBets(close);
				
				arrayThisWeek.add(match);
			}


		}catch(JSONException e){
			e.printStackTrace();
		}
		return arrayThisWeek;
	}

}
