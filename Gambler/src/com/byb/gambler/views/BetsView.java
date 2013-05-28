package com.byb.gambler.views;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.byb.gambler.R;
import com.byb.gambler.activity.BetsActivity;
import com.byb.gambler.adapters.OpenBetsAdapter;
import com.byb.gambler.manager.BetsManager;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.LeagueList;
import com.byb.gambler.models.OpenBets;
import com.byb.gambler.models.UserInfo;
import com.byb.gambler.service.RestClient;
import com.byb.gambler.service.RestClient.RequestMethod;
import com.byb.gambler.views.LoadMoreListView.OnLoadMoreListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BetsView extends RelativeLayout implements OnLoadMoreListener {

	View rootLayout;
	LayoutInflater linflator;
	private static BetsView ref;
	private Spinner mSpinner;
	private RadioButton rbOpen,rbClosed;
	private LoadMoreListView lvMatches;
	DisplayImageOptions options;
	private ArrayList<LeagueList> mLeagueArray;
	private OpenBetsAdapter mOpenBetsAdapter;
	private TextView btnPoints;
	private FrameLayout mEmptyLayout;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mEmptyLayout.setVisibility(View.VISIBLE);
				//				ShowMessage.getInstance().showMessage(R.string.network_message,BetsActivity.betsContext);
				break;
			case 1:
				Bundle bun = msg.getData();
				String message = bun.getString("msg");
				/*ShowMessage.getInstance().showMessageString(message,
						getContext());*/
				mEmptyLayout.setVisibility(View.VISIBLE);
				mOpenBetsAdapter.clearList();
				Toast.makeText(BetsActivity.betsContext, "No records found", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Log.d("tag", "Set visibility");
				mEmptyLayout.setVisibility(View.VISIBLE);
				//				ShowMessage.getInstance().showPDialog(BetsActivity.betsContext, R.string.getting_league_details, null);
				break;	
			case 3:
				mEmptyLayout.setVisibility(View.GONE);
				//				ShowMessage.getInstance().hidePDialog();
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
					ArrayList<LeagueList> leagueLists = BetsManager.getInstance().parseLeagueList(jsonArray);
					Log.d("tag", "spinner League list before :"+ leagueLists.size());
					setSpinner(leagueLists);
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
					mArrayBetsList = BetsManager.getInstance().parseBetsList(jsonArray);
					if(bun.getBoolean("isLoadMore")){
						mOpenBetsAdapter.updateListAdapter(mArrayBetsList);
					}else{
						updateList(mArrayBetsList);
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 7:
				if(mOpenBetsAdapter != null)
					mOpenBetsAdapter.clearList();
				Toast.makeText(BetsActivity.betsContext, "No records found", Toast.LENGTH_LONG).show();
				break;
			case 8:
				mEmptyLayout.setVisibility(View.VISIBLE);
				//				ShowMessage.getInstance().showPDialog(BetsActivity.betsContext, R.string.getting_bet_data, null);
				break;	
			}
		};
	};

	ArrayList<OpenBets> mArrayBetsList;

	private void updateList(ArrayList<OpenBets> arrOpenBets){
		if(arrOpenBets.size() > 0){
			if(rbOpen.isChecked()){
				mOpenBetsAdapter = new OpenBetsAdapter(BetsActivity.betsContext, arrOpenBets,true,options);
				lvMatches.setAdapter(mOpenBetsAdapter);
			}else if(rbClosed.isChecked()){
				mOpenBetsAdapter = new OpenBetsAdapter(BetsActivity.betsContext, arrOpenBets,false,options);
				lvMatches.setAdapter(mOpenBetsAdapter);
			}
		}else{
			if(mOpenBetsAdapter != null)
				mOpenBetsAdapter.clearList();
			Toast.makeText(BetsActivity.betsContext, "No records found", Toast.LENGTH_LONG).show();
		}

	}


	public static BetsView getView(Context context) {
		//		if (ref == null)
		ref = new BetsView(context);
		return ref;
	}

	private BetsView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (linflator == null)
			linflator = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rootLayout = linflator.inflate(R.layout.activity_bets,(ViewGroup) findViewById(R.id.rlBets));


		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.loading)
		.showImageOnFail(R.drawable.loading)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(10))
		.build();

		addView(rootLayout);
		Log.d("tag", "View Created");
		mEmptyLayout = (FrameLayout) rootLayout.findViewById(R.id.emptyView);
		mSpinner = (Spinner) rootLayout.findViewById(R.id.sbCategory);
		rbOpen = (RadioButton) rootLayout.findViewById(R.id.rbOpen);
		rbClosed = (RadioButton) rootLayout.findViewById(R.id.rbClosed);
		lvMatches = (LoadMoreListView) rootLayout.findViewById(R.id.lvMatches);
		btnPoints = (TextView) rootLayout.findViewById(R.id.imgBtnHeaderRight);

		/*mSpinner.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getLeagueList();	
			}
		});*/

		btnPoints.setText(""+UserInfo.getInstance().getUserStatistics().getBallsAvailable());

		rbOpen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rbOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
					int position = (int) mSpinner.getSelectedItemId();
					if (position != 0) {
						getBetsList(context, true,mLeagueArray.get(position-1).getID(), "0",false);
					}else{
						getBetsList(context, true,"", "0",false);
					}

				}else{
					rbOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});

		rbClosed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rbClosed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
					int position = (int) mSpinner.getSelectedItemId();
					if (position != 0) {
						getBetsList(context, false,mLeagueArray.get(position-1).getID(), "0",false);
					}else{
						getBetsList(context, false,"", "0",false);
					}
				}else{
					rbClosed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});

		lvMatches.setOnLoadMoreListener(this);

		getLeagueList();
		//		getBetsList(context, true,"");
	}

	@SuppressWarnings("unchecked")
	private void setSpinner(ArrayList<LeagueList> leagueList) {
		mLeagueArray = leagueList;
		Log.d("tag", "spinner League list after :"+ leagueList.size());
		ArrayList<String> leagueNameArray = new ArrayList<String>();
		leagueNameArray.add("All Sports");
		for (LeagueList leagueName : leagueList) {
			leagueNameArray.add(leagueName.getName());
		}

		ArrayAdapter spinnerAdapter = new ArrayAdapter(
				getContext(),
				android.R.layout.simple_spinner_item, 
				leagueNameArray);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(position != 0){
					mHandler.sendEmptyMessage(8);
					getBetsList(BetsActivity.betsContext, rbOpen.isChecked(), mLeagueArray.get(position-1).getID(), "0",false);
				}else{
					mHandler.sendEmptyMessage(8);
					getBetsList(BetsActivity.betsContext, rbOpen.isChecked(), "", "0",false);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinnerAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(spinnerAdapter);

	}

	public static void nullifyView() {
		ref = null;
	}

	private void getBetsList(Context context, final boolean isOpen, final String league_id, final String skip, final boolean isPullToRefresh) {
		if(Developer.getInstance().isNetworkConnected(context)){
			if(!isPullToRefresh)
				mHandler.sendEmptyMessage(8);
			new Thread(){
				public void run() {
					try {
						String text = UserInfo.getInstance().getID()+":"+UserInfo.getInstance().getAccessToken();
						System.out.println("Text = "+text);
						// Sending side
						byte[] data = null;
						data = text.getBytes("UTF-8");
						String base64 = Base64.encodeToString(data, Base64.DEFAULT);

						/*JSONObject jobj = new JSONObject();					jobj.put("league_id", "");
					jobj.put("skip", 0);
					jobj.put("count", 5);

					Log.e("json string :", jobj.toString());

					byte[] postData = jobj.toString().getBytes();

					String response = UserManager.getInstance().callWS("bets/open.json",
							postData,true,base64);*/

						RestClient client;
						if(isOpen){
							client = new RestClient("bets/open.json");
						}else{
							client = new RestClient("bets/closed.json");
						}

						client.AddHeader("Content-type", "application/json"); 
						client.AddHeader("Authorization", "Basic "+base64);
						client.AddParam("league_id", league_id);
						client.AddParam("Skip", skip);
						client.AddParam("Count", "");
						//			        client.AddParam("access_token", text);
						System.out.println("Client = "+client);
						client.Execute(RequestMethod.GET);

						String response = client.getResponse();

						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){

							Message msg = new Message();
							msg.what = 6;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("data"));
							bun.putBoolean("isLoadMore", isPullToRefresh);
							msg.setData(bun);
							mHandler.sendMessage(msg);
							if(isPullToRefresh){
								lvMatches.onLoadMoreComplete();
							}

						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
							msg.what = 1;
							Bundle bun = new Bundle();
							bun.putString("msg", json.getString("errorDescription"));
							msg.setData(bun);
							mHandler.sendMessage(msg);
						}else if(json.getString("status").equalsIgnoreCase("fail")){
							mHandler.sendEmptyMessage(7);
						}

						System.out.println("Response = "+response);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(3);
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);
	}

	private void getLeagueList() {
		if(Developer.getInstance().isNetworkConnected(getContext())){
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


						Log.d("tag", "Response : "+ response);

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


	@Override
	public void onLoadMore() {
		Log.d("tag", "Load more");
		int position = (int) mSpinner.getSelectedItemId();
		boolean isOpen = false;

		if(rbOpen.isChecked()){
			isOpen = true;
		}
		if (position != 0) {
			getBetsList(BetsActivity.betsContext, isOpen,mLeagueArray.get(position-1).getID(), ""+mArrayBetsList.size(),true);
		}else{
			getBetsList(BetsActivity.betsContext,  isOpen,"", ""+mArrayBetsList.size(),true);
		}
	}
}