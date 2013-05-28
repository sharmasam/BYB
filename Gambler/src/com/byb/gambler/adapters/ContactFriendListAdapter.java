package com.byb.gambler.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.facebook.Friend;
import com.byb.gambler.models.ContactFriends;
import com.byb.gambler.utility.Constants;

public class ContactFriendListAdapter extends BaseAdapter {
	private final Context context;
	ArrayList<ContactFriends> friends;

	//Fastscoll variables
	HashMap<Character, Integer> letterIndex;
	Character[] sections;

	//Picture for unknown people
	Bitmap mInvited;
	Bitmap mUNInvited;
	
	public ContactFriendListAdapter(Context context,ArrayList<ContactFriends> friends) {
		this.context  = context;
		this.friends = friends;
		Resources r = context.getResources();
		Bitmap b = BitmapFactory.decodeResource(r, R.drawable.button_invited);
		mInvited = Bitmap.createScaledBitmap(b, Constants.size_Profile_Picture_Width, Constants.size_Profile_Picture_Heigth, true);

		b = BitmapFactory.decodeResource(r, R.drawable.button_uninvited);
		mUNInvited = Bitmap.createScaledBitmap(b, Constants.size_Profile_Picture_Width, Constants.size_Profile_Picture_Heigth, true);

	}

	public void setNewList(ArrayList<ContactFriends> frnd) {
		this.friends = frnd;
		this.notifyDataSetChanged();
	}


	//For view recycling (optimization for listview)
	static class ViewHolder {
		public TextView name;
		public ImageView profilePicture;
		public ImageView ivInvited;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.list_item_friend, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) layout.findViewById(R.id.real_life_name);
			viewHolder.profilePicture = (ImageView) layout.findViewById(R.id.profile_picture);
			viewHolder.ivInvited = (ImageView) layout.findViewById(R.id.iv_invited);
			layout.setTag(viewHolder);
		}

		ContactFriends friend = friends.get(position);
		ViewHolder holder = (ViewHolder) layout.getTag();

		holder.name.setText(friend.getName());
	
		if(friend.isInvited()){
			holder.ivInvited.setImageBitmap(mInvited);//setBackgroundResource(R.drawable.button_invited);
		}else{
			holder.ivInvited.setImageBitmap(mUNInvited);//setBackgroundResource(R.drawable.button_uninvited);
		}
		return layout;
	}

	@Override
	public int getCount() {
		return friends.size();
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}