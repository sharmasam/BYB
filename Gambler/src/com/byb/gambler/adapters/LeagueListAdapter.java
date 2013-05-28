package com.byb.gambler.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.models.LeagueList;

public class LeagueListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<LeagueList> mLeagueList;
	private Context context;

	public LeagueListAdapter(Context context) {
		this.context = context;
		mLeagueList = new ArrayList<LeagueList>();
	}

	public LeagueListAdapter(Context context, ArrayList<LeagueList> leagueList) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mLeagueList = leagueList;
	}

	public void updateListAdapter(ArrayList<LeagueList> openBets){
		this.mLeagueList = openBets;
		this.notifyDataSetChanged();
	}

	public void clearList() {
		this.mLeagueList.clear();
		this.notifyDataSetChanged();
	}

	public int getCount() {
		return mLeagueList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.play_home_list_item,
					null);
			holder = new ViewHolder();

			holder.btn_League_Badge = (Button) convertView.findViewById(R.id.btn_match_badge);
			holder.tv_LeagueName = (TextView) convertView.findViewById(R.id.tv_league_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LeagueList league = mLeagueList.get(position);
		if(league != null){
			holder.btn_League_Badge.setText(league.getNumberOfMatch());
			holder.tv_LeagueName.setText(league.getName());
		}
		return convertView;
	}

	static class ViewHolder {
		Button btn_League_Badge;
		TextView tv_LeagueName;
	}

}
