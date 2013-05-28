package com.byb.gambler.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byb.gambler.R;
import com.byb.gambler.activity.BetsActivity;
import com.byb.gambler.activity.PlayActivity;
import com.byb.gambler.activity.PlayWebservice;
import com.byb.gambler.adapters.LeagueListAdapter;
import com.byb.gambler.manager.PlayManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.LeagueList;
import com.byb.gambler.models.ShowMessage;
import com.byb.gambler.models.ThisWeekList;
import com.byb.gambler.models.UserInfo;
import com.byb.gambler.service.RestClient;
import com.byb.gambler.service.RestClient.RequestMethod;

public class PlayView extends RelativeLayout {

	View rootLayout;
	LayoutInflater linflator;
	private static PlayView ref;
	private Context context;
	private ListView mLeagueList;
	private ArrayList<LeagueList> mLeagueArray;
	private LeagueListAdapter mAdapeter;
	private TextView btnPoints;
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ShowMessage.getInstance().showMessage(R.string.network_message,context);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				ShowMessage.getInstance().showMessageString(message,
						context);
				Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show();
				break;
			case 2:
				ShowMessage.getInstance().showPDialog(context, R.string.getting_league_details, null);
				break;	
			case 3:
				ShowMessage.getInstance().hidePDialog();
				break;
			case 4:
				ShowMessage.getInstance().hidePDialog();
				ShowMessage.getInstance().showMessage(R.string.connection_time_out_message,context);
				break;
			case 5:
				bun = msg.getData();
				try{
					message = bun.getString("msg");
					JSONArray jsonArray = new JSONArray(message);
					mLeagueArray = PlayManager.getInstance().parseLeagueList(jsonArray);
					createLeagueList();
					break;
				}catch (JSONException e) {
					e.printStackTrace();
					break;
				}
				
			}
		};
	};

	
	public static PlayView getView(Context context) {
//		if (ref == null)
			ref = new PlayView(context);
		return ref;
	}

	private PlayView(final Context context) {
		super(context);
		if (linflator == null)
			linflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.context = context;

		rootLayout = linflator.inflate(R.layout.activity_play_home,(ViewGroup) findViewById(R.id.rlHome));
		
		mLeagueList = (ListView) rootLayout.findViewById(R.id.lv_league);
		
		btnPoints = (TextView) rootLayout.findViewById(R.id.imgBtnHeaderRight);
		btnPoints.setText(""+UserInfo.getInstance().getUserStatistics().getBallsAvailable());
		
		mAdapeter = new LeagueListAdapter(context, new ArrayList<LeagueList>());
		addView(rootLayout);
		getLeagueList();
	}
	
	public static void nullifyView() {
		ref = null;
	}
	
	
	private void createLeagueList() {
//		mAdapeter = new LeagueListAdapter(context, mLeagueArray);
		mAdapeter.updateListAdapter(mLeagueArray);
		mLeagueList.setAdapter(mAdapeter);
	}
	

	private void getLeagueList() {
		if(Developer.getInstance().isNetworkConnected(context)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						RestClient client = new RestClient("leagues_and_matches.json");
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

}
