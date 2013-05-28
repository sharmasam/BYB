package com.byb.gambler.manager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.byb.gambler.models.ActionBets;

public class HomeManager {

	private static HomeManager mManager;

	public static HomeManager getInstance() {
		if (mManager == null) {
			mManager = new HomeManager();
		}
		return mManager;
	}


	public ArrayList<ActionBets> parseActionBetsList(JSONArray jsonArray){
		JSONObject jsonClinic;
		ArrayList<ActionBets> arrActionBets = new ArrayList<ActionBets>();
		try{
			
			for(int i = 0;i<jsonArray.length();i++){
				ActionBets actionBets = new ActionBets();
				
				String clinic1 = jsonArray.getString(i);
				jsonClinic = new JSONObject(clinic1);
				if(jsonClinic.has("id")){
					actionBets.setId(jsonClinic.getString("id"));
				}
				if(jsonClinic.has("type")){
					actionBets.setType(jsonClinic.getString("type"));
				}
				if(jsonClinic.has("scope")){
					actionBets.setScope(jsonClinic.getString("scope"));
				}
				if(jsonClinic.has("description")){
					actionBets.setDescription(jsonClinic.getString("description"));
				}
				if(jsonClinic.has("action")){
					actionBets.setAction(jsonClinic.getString("action"));
				}
				if(jsonClinic.has("timestamp")){
					actionBets.setTimestamp(jsonClinic.getString("timestamp"));
				}
				if(jsonClinic.has("iconURL")){
					actionBets.setIconURL(jsonClinic.getString("iconURL"));
				}
				if(jsonClinic.has("highlightRanges")){
					actionBets.setHighlightRanges(jsonClinic.getString("highlightRanges"));
				}
				if(jsonClinic.has("matchId")){
					actionBets.setMatchId(jsonClinic.getString("matchId"));
				}
				if(jsonClinic.has("user")){
				    JSONObject  jsonUser = jsonClinic.getJSONObject("user");
				    if(jsonUser.has("id")){
						actionBets.setUser_id(jsonUser.getString("id"));
					}
				    if(jsonUser.has("fullName")){
						actionBets.setUser_fullName(jsonUser.getString("fullName"));
					}
				    if(jsonUser.has("birthday")){
						actionBets.setUser_birthday(jsonUser.getString("birthday"));
					}
				    if(jsonUser.has("thumbnailAvatarURL")){
						actionBets.setUser_thumbnailAvatarURL(jsonUser.getString("thumbnailAvatarURL"));
					}
				    if(jsonUser.has("lastSeen")){
						actionBets.setUser_lastSeen(jsonUser.getString("lastSeen"));
					}
				}
				arrActionBets.add(actionBets);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return arrActionBets;
	}
}
