package com.byb.gambler.views;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byb.gambler.R;
import com.byb.gambler.activity.FriendListActivity;
import com.byb.gambler.activity.HomeActivity;
import com.byb.gambler.adapters.ActionBetsAdapter;
import com.byb.gambler.listner.GrabListerner;
import com.byb.gambler.manager.HomeManager;
import com.byb.gambler.manager.UserManager;
import com.byb.gambler.models.ActionBets;
import com.byb.gambler.models.Developer;
import com.byb.gambler.models.UserInfo;
import com.byb.gambler.service.RestClient;
import com.byb.gambler.service.RestClient.RequestMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HomeView extends RelativeLayout implements GrabListerner{

	View rootLayout;
	LayoutInflater linflator;
	private static HomeView ref;
	private Button mBtnAddFriend;
	private RadioButton rbAction,rbBets,rbMsgs;
	private ListView lvActionBets;
	DisplayImageOptions options;
//	private ArrayList<LeagueList> mLeagueArray;
	private ActionBetsAdapter mActionBetsAdapter;
	private TextView btnPoints;
	private String deviceType = "";
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
				mActionBetsAdapter.clearList();
				Toast.makeText(HomeActivity.homeContext, "No records found", Toast.LENGTH_LONG).show();
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
			case 6:
				bun = msg.getData();
				try{
				message = bun.getString("msg");
				JSONArray jsonArray = new JSONArray(message);
				ArrayList<ActionBets> arrActionBets = HomeManager.getInstance().parseActionBetsList(jsonArray);
				updateList(arrActionBets);
				}catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 7:
				if(mActionBetsAdapter != null)
					mActionBetsAdapter.clearList();
				Toast.makeText(HomeActivity.homeContext, "No records found", Toast.LENGTH_LONG).show();
				break;
			case 8:
				mEmptyLayout.setVisibility(View.VISIBLE);
				//				ShowMessage.getInstance().showPDialog(BetsActivity.betsContext, R.string.getting_bet_data, null);
				break;	
			}
		};
	};
	
	private void updateList(ArrayList<ActionBets> arrActionBets){
		if(arrActionBets.size() > 0){
			if(rbAction.isChecked()){
				mActionBetsAdapter = new ActionBetsAdapter(HomeActivity.homeContext, arrActionBets,"Action",options,deviceType,this);
				lvActionBets.setAdapter(mActionBetsAdapter);
			}else if(rbBets.isChecked()){
				mActionBetsAdapter = new ActionBetsAdapter(HomeActivity.homeContext, arrActionBets,"Bets",options,deviceType,this);
				lvActionBets.setAdapter(mActionBetsAdapter);
			}
		}else{
			if(mActionBetsAdapter != null)
				mActionBetsAdapter.clearList();
			Toast.makeText(HomeActivity.homeContext, "No records found", Toast.LENGTH_LONG).show();
		}
	}
	
	
	public static HomeView getView(Context context) {
//		if (ref == null)
			ref = new HomeView(context);
		return ref;
	}

	private HomeView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (linflator == null)
			linflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rootLayout = linflator.inflate(R.layout.activity_home,(ViewGroup) findViewById(R.id.rlHome));
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		System.out.println("Display Height = "+height+" Display Width = "+width);
		if(width == 720)
			deviceType = "xlarge";
		else if(width == 480)
			deviceType = "large";
		else
			deviceType = "small";
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.loading)
		.showImageOnFail(R.drawable.loading)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
		
		addView(rootLayout);
		
		mEmptyLayout = (FrameLayout) rootLayout.findViewById(R.id.emptyView);
		mBtnAddFriend = (Button) rootLayout.findViewById(R.id.btnAddNewFriend);
		rbAction = (RadioButton) rootLayout.findViewById(R.id.rbAction);
		rbBets = (RadioButton) rootLayout.findViewById(R.id.rbBets);
		rbMsgs = (RadioButton) rootLayout.findViewById(R.id.rbMsgs);
		lvActionBets = (ListView) rootLayout.findViewById(R.id.lvAction);
		btnPoints = (TextView) rootLayout.findViewById(R.id.imgBtnHeaderRight);
		
		btnPoints.setText(""+UserInfo.getInstance().getUserStatistics().getBallsAvailable());
		
		getList(context, "Action", "0","0","0");
		
		rbAction.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rbAction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
					rbAction.setTextColor(Color.WHITE);
					rbBets.setTextColor(Color.GRAY);
					rbMsgs.setTextColor(Color.GRAY);
					getList(context, "Action", "0","0","0");
				}else{
					rbAction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});
		
		rbBets.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rbBets.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
					rbAction.setTextColor(Color.GRAY);
					rbBets.setTextColor(Color.WHITE);
					rbMsgs.setTextColor(Color.GRAY);
					getList(context, "Bets", "0","0","0");
				}else{
					rbBets.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});
		
		rbMsgs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					rbMsgs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_selected_left, 0, R.drawable.divider_selected_right, 0);
					rbAction.setTextColor(Color.GRAY);
					rbBets.setTextColor(Color.GRAY);
					rbMsgs.setTextColor(Color.WHITE);
					if(mActionBetsAdapter != null)
						mActionBetsAdapter.clearList();
					Toast.makeText(HomeActivity.homeContext, "No records found", Toast.LENGTH_LONG).show();
				}else{
					rbMsgs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.divider_unselected, 0, R.drawable.divider_unselected, 0);
				}
			}
		});
		
		mBtnAddFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context, FriendListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
		});
		
	}
	

	public static void nullifyView() {
		ref = null;
	}
	
	private void getList(Context context, final String tabName, final String sinceTime,final String beforeTime,final String count) {
		if(Developer.getInstance().isNetworkConnected(context)){
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
				    
				    RestClient client;
				    if(tabName.equalsIgnoreCase("Action")){
				    	client = new RestClient("events/pending.json");
				    }else{
				    	client = new RestClient("events/bets.json");
				    }
					
					client.AddHeader("Content-type", "application/json");
					client.AddHeader("Authorization", "Basic "+base64);
					if(! sinceTime.equalsIgnoreCase("0"))
						client.AddParam("since_time", sinceTime);
					if(! beforeTime.equalsIgnoreCase("0"))
						client.AddParam("before_time", beforeTime);
					if(! count.equalsIgnoreCase("0"))
						client.AddParam("count", count);
					
					client.Execute(RequestMethod.GET);
					
					String response = client.getResponse();
					
					
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
	
	private void grabEvent(final Context context,final String eventId){
		if(Developer.getInstance().isNetworkConnected(context)){
			mHandler.sendEmptyMessage(2);
			new Thread(){
				public void run() {
					try {
						
						String text = UserInfo.getInstance().getID()+":"+UserInfo.getInstance().getAccessToken();
						System.out.println("Text = "+text);
						// Sending side
						byte[] data = null;
						data = text.getBytes("UTF-8");
						String base64 = Base64.encodeToString(data, Base64.DEFAULT);
						
						
						String response = UserManager.getInstance().callWSforPost("events/action/"+eventId+".json",
								null,true,base64);
						
						Log.d("tag", "Grab response : " +response);
						JSONObject json = new JSONObject(response);
						if(json.getString("status").equalsIgnoreCase("ok")){
							getList(context, "Action", "0","0","0");
						}else if(json.getString("status").equalsIgnoreCase("error")){
							Message msg = new Message();
				    		msg.what = 1;
				    		Bundle bun = new Bundle();
				    		bun.putString("msg", json.getString("errorDescription"));
				    		msg.setData(bun);
							mHandler.sendMessage(msg);
							mHandler.sendEmptyMessage(3);
						}
							
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mHandler.sendEmptyMessage(4);
					}
					
				};
			}.start();
		}else
			mHandler.sendEmptyMessage(0);
		
	}




	@Override
	public void onGrab(String eventId, String requestCode) {
		grabEvent(HomeActivity.homeContext, eventId);
	}

}