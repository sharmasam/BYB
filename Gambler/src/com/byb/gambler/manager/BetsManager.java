package com.byb.gambler.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.byb.gambler.models.InitiatorUser;
import com.byb.gambler.models.LeagueList;
import com.byb.gambler.models.Match;
import com.byb.gambler.models.OpenBets;
import com.byb.gambler.models.Team;

public class BetsManager {

	private static BetsManager mManager;

	public static BetsManager getInstance() {
		if (mManager == null) {
			mManager = new BetsManager();
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

	public ArrayList<OpenBets> parseBetsList(JSONArray jsonArray){
		JSONObject jsonClinic;
		JSONObject json;
		ArrayList<OpenBets> arrOpenBets = new ArrayList<OpenBets>();
		try{
			
			for(int i = 0;i<jsonArray.length();i++){
				Match match = new Match();
				OpenBets openBets = new OpenBets();
				
				String clinic1 = jsonArray.getString(i);
				jsonClinic = new JSONObject(clinic1);
				if(jsonClinic.has("id")){
					openBets.setID(jsonClinic.getString("id"));
				}
				if(jsonClinic.has("initiatorUserId")){
					openBets.setInitiatorUserID(jsonClinic.getString("initiatorUserId"));
				}
				if(jsonClinic.has("initiatorUser")){
					InitiatorUser user = new InitiatorUser();
					json = new JSONObject(jsonClinic.getString("initiatorUser"));
				    JSONObject  menu = jsonClinic.getJSONObject("initiatorUser");

				    Map<String,String> map = new HashMap<String,String>();
				    Iterator iter = menu.keys();
				    while(iter.hasNext()){
				        String key = (String)iter.next();
				        String value = menu.getString(key);
				        System.out.println("Key = "+key+" value = "+value);
				        map.put(key,value);
				    }
					System.out.println();
					user.setID(json.getString("id"));
					user.setFullName(json.getString("fullName"));
					user.setThumbNail(map.get("thumbnailAvatarURL"));
					Log.d("tag", "URL : "+user.getThumbNail());
					user.setBirthday(json.getString("birthday"));
					openBets.setInitiatorUser(user);
				}
				if(jsonClinic.has("match")){
					json = new JSONObject(jsonClinic.getString("match"));
					match.setID(json.getString("id"));

					if(json.has("league")){
						JSONObject jsonLeague = new JSONObject(json.getString("league"));
						LeagueList league = new LeagueList();
						league.setID(jsonLeague.getString("id"));
						league.setName(jsonLeague.getString("name"));
						league.setSports(jsonLeague.getString("sport"));
						match.setLeague(league);
					}

					if(json.has("teamA")){
						JSONObject jsonTeam = new JSONObject(json.getString("teamA"));
						Team team = new Team();
						team.setID(jsonTeam.getString("id"));
						team.setName(jsonTeam.getString("name"));
						team.setLogoImage(jsonTeam.getString("logoImageURL"));
						match.setTeamA(team);
					}

					if(json.has("teamB")){
						JSONObject jsonTeam = new JSONObject(json.getString("teamB"));
						Team team = new Team();
						team.setID(jsonTeam.getString("id"));
						team.setName(jsonTeam.getString("name"));
						team.setLogoImage(jsonTeam.getString("logoImageURL"));
						match.setTeamB(team);
					}
					match.setCloseTippingTime(json.getString("closeTippingTime"));
					match.setEndTime(json.getString("endTime"));
					match.setResult(jsonClinic.getString("result"));
					match.setMatchedAmount(jsonClinic.getString("matchedAmount"));
					match.setStatus(jsonClinic.getString("status"));
					match.setStartTime(json.getString("startTime"));
					Date time = new java.util.Date(Long.parseLong(match.getStartTime()));
					Log.d("tag", "Time : "+time.toGMTString());
					openBets.setMatch(match);
				}

				openBets.setAcceptedUser(jsonClinic.getString("acceptedUser"));
				openBets.setAmount(jsonClinic.getString("amount"));
				openBets.setBetTeamID(jsonClinic.getString("betTeamId")); 	
				openBets.setBetTime(jsonClinic.getString("betTime"));
				openBets.setID(jsonClinic.getString("id"));
				openBets.setInitiatorUserID(jsonClinic.getString("initiatorUserId"));
				openBets.setLastModified(jsonClinic.getString("lastModifiedTime"));
				arrOpenBets.add(openBets);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return arrOpenBets;
	}
}
