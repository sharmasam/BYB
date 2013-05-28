package com.byb.gambler.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.byb.gambler.R;
import com.byb.gambler.manager.BetsManager;
import com.byb.gambler.manager.PlayManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.LeagueList;
import com.byb.gambler.models.ResultList;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.models.Team;
import com.byb.gambler.models.ThisWeekList;
import com.byb.gambler.service.RestClient;
import com.byb.gambler.service.RestClient.RequestMethod;

public class PlayWebservice extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_home);
		getLeagueList();
//		"status":"ok","data":[{"number_of_matches":117,"league":{"id":"LSS-829181","sport":"AFL","name":"AFL"}},{"number_of_matches":11,"league":{"id":"LSS-828423","sport":"Basketball","name":"NBA"}},{"number_of_matches":0,"league":{"id":"LSS-827666","sport":"Football","name":"Australian A-League"}},{"number_of_matches":0,"league":{"id":"LSS-827652","sport":"Football","name":"English Premier League"}},{"number_of_matches":1730,"league":{"id":"LSS-830787","sport":"MLB","name":"MLB"}},{"number_of_matches":32,"league":{"id":"LSS-828955","sport":"Rugby","name":"Super Rugby"}},{"number_of_matches":112,"league":{"id":"LSS-829880","sport":"Rugby League","name":"NRL"}}]}

		getThisWeekList("LSS-829181", "0", BYBAPISortOrderTime);
		
		getLiveList("LSS-829181", "0", BYBAPISortOrderTime);
		
		getResultList("LSS-829181", "0");
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,BetsActivity.betsContext);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						PlayWebservice.this);
				Toast.makeText(BetsActivity.betsContext, "No records found", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Log.d("tag", "Set visibility");
				ShowMessage.getInstance().showPDialog(BetsActivity.betsContext, R.string.getting_league_details, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				/*ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,
						BetsActivity.betsContext);*/
				break;
			case 5:
				bun = msg.getData();
				try{
					message = bun.getString("msg");
					JSONArray jsonArray = new JSONArray(message);
					ArrayList<LeagueList> leagueLists = PlayManager.getInstance().parseLeagueList(jsonArray);
					break;
				}catch (JSONException e) {
					e.printStackTrace();
					break;
				}
				
			case 6:
				bun = msg.getData();
				try{
					message = bun.getString("msg");
					JSONArray jsonArray = new JSONArray(message);
					ArrayList<ResultList> week = PlayManager.getInstance().parseResultList(jsonArray);//parseThisWeekList(jsonArray);
					Log.d("tag", "Week Size : "+ week.size());
					resultLogic(week);
					
					break;
				}catch (JSONException e) {
					e.printStackTrace();
					break;
				}

			}
		};
	};

	private void getLeagueList() {
		if(Developer.getInstance().isNetworkConnected(this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						RestClient client = new RestClient("leagues_and_matches.json");
						//						client.AddParam("league_id", "0");
						client.AddParam("skip", "0");
						client.AddParam("count", "");

						try {
							client.Execute(RequestMethod.GET);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String response = client.getResponse();


						Log.d("tag", "League List Response : "+ response);

						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 5;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							msg.setData(bun);
							mHandler.sendMessage(msg);

						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}

					} catch (JSONException e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);
	}
	
	private String BYBAPISortOrderTime = "0";
	private String BYBAPISortOrderAlphabetical = "1";
	
	
	private void getThisWeekList(final String league_id, final String skip, final String order) {
		if(Developer.getInstance().isNetworkConnected(this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						RestClient client = new RestClient("matches.json");
						client.AddParam("league_id", league_id);
						client.AddParam("tipped_only", "false");
						if(order.equalsIgnoreCase(BYBAPISortOrderAlphabetical)){
							client.AddParam("league", BYBAPISortOrderAlphabetical);
						}else{
							client.AddParam("start_time", BYBAPISortOrderTime);
						}
						
						client.AddParam("skip", skip);
						client.AddParam("count", "");

						try {
							client.Execute(RequestMethod.GET);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String response = client.getResponse();


						Log.d("tag", "Week List Response : "+ response);

						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 6;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							msg.setData(bun);
							mHandler.sendMessage(msg);

						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}

					} catch (JSONException e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);
	}

	
	private void getLiveList(final String league_id, final String skip, final String order) {
		if(Developer.getInstance().isNetworkConnected(this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						RestClient client = new RestClient("matches/live.json");
						client.AddParam("league_id", league_id);
						if(order.equalsIgnoreCase(BYBAPISortOrderAlphabetical)){
							client.AddParam("league", BYBAPISortOrderAlphabetical);
						}else{
							client.AddParam("start_time", BYBAPISortOrderTime);
						}
						
						client.AddParam("skip", skip);
						client.AddParam("count", "");

						try {
							client.Execute(RequestMethod.GET);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String response = client.getResponse();


						Log.d("tag", "Live List Response : "+ response);

						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 6;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							msg.setData(bun);
							mHandler.sendMessage(msg);

						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}

					} catch (JSONException e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);
	}
	
	
	private void getResultList(final String league_id, final String skip) {
		if(Developer.getInstance().isNetworkConnected(this)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						RestClient client = new RestClient("matches/results/completed.json");
						client.AddParam("league_id", league_id);
						
						client.AddParam("skip", skip);
						client.AddParam("count", "");

						try {
							client.Execute(RequestMethod.GET);
						} catch (Exception e) {
							e.printStackTrace();
						}

						String response = client.getResponse();

						Log.d("tag", "Result List Response : "+ response);

						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 6;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							msg.setData(bun);
							mHandler.sendMessage(msg);

						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}

					} catch (JSONException e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);
	}

	private void resultLogic(ArrayList<ResultList> resultList) {
		for (ResultList result : resultList) {
			TeamColorLogic(result);
			UserColorLogic(result);
		}
	}
	
	private void TeamColorLogic(ResultList result) {
		Team teamA = result.getTeamA();
		Team teamB = result.getTeamB();
		if(result.getWonID().equalsIgnoreCase(teamA.getID())){
//			Show Team A Green
		}else if(result.getWonID().equalsIgnoreCase(teamB.getID())){
//			Show Team B Green
		}else{
//			Both team as Grey
		}
	}
	
	
	private void UserColorLogic(ResultList result){
		
	}

}
