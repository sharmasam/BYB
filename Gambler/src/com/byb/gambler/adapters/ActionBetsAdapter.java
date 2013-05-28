package com.byb.gambler.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.listner.GrabListerner;
import com.byb.gambler.models.ActionBets;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ActionBetsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<ActionBets> mActionBetsList;
	private Context context;
	private String tabName;
	private String deviceType;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private GrabListerner grabListerner; 
	
	public ActionBetsAdapter(Context context) {
		this.context = context;
	}

	public ActionBetsAdapter(Context context, ArrayList<ActionBets> actionBets,
			String tabName, DisplayImageOptions options,String deviceType, GrabListerner grabListerner) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mActionBetsList = actionBets;
		this.tabName = tabName;
		this.options = options;
		this.deviceType = deviceType;
		animateFirstListener = new AnimateFirstDisplayListener();
		imageLoader = ImageLoader.getInstance();
		this.grabListerner = grabListerner;
	}

	public void clearList() {
		this.mActionBetsList.clear();
		this.notifyDataSetChanged();
	}

	public int getCount() {
		return mActionBetsList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.home_action_bets_list_item,
					null);
			holder = new ViewHolder();

			holder.imgBtnThumbnail = (ImageButton) convertView
					.findViewById(R.id.imgBtnThumbnail);
			holder.imgBtnUserBorder = (ImageButton) convertView
					.findViewById(R.id.imgBtnUserborder);
			holder.tvDetails = (TextView) convertView
					.findViewById(R.id.tvDetails);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tvTime);
			holder.btnGrab = (Button) convertView
					.findViewById(R.id.btnGrab);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.tvTime.setText(calculateDifference(Long
				.parseLong(mActionBetsList.get(position).getTimestamp())));
		holder.imgBtnUserBorder
		.setBackgroundResource(R.drawable.image_border);

		if(tabName.equalsIgnoreCase("Bets")){
			holder.btnGrab.setVisibility(View.GONE);
			
			if(mActionBetsList.get(position).getDescription() != null){
				SpannableStringBuilder str = new SpannableStringBuilder(mActionBetsList.get(position).getDescription().trim());
				if(mActionBetsList.get(position).getHighlightRanges() != null){
					String highlightTextRange = mActionBetsList.get(position).getHighlightRanges().trim();
					ArrayList<Integer> startArray = new ArrayList<Integer>();
					ArrayList<Integer> endArray = new ArrayList<Integer>();
					if(highlightTextRange != null){
						highlightTextRange = highlightTextRange.replaceAll("\\[", "").replaceAll("\\]", "");
						String[] splittedRange = highlightTextRange.split(",");
						
						for(int i = 0;i<splittedRange.length;i++){
							if(i == 0 || ((i % 2) == 0))
								startArray.add(Integer.parseInt(splittedRange[i]));
							else if((i % 2) == 1)
								endArray.add(Integer.parseInt(splittedRange[i]));
						}
					}
					
					for(int i = 0;i<startArray.size();i++){
						str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), startArray.get(i), startArray.get(i)+endArray.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					
				}
			    holder.tvDetails.setText(str);
			}
			
			if (mActionBetsList.get(position).getIconURL() != null)
				imageLoader.displayImage(mActionBetsList.get(position).getIconURL().trim(),
						holder.imgBtnThumbnail, options, animateFirstListener);
		}else if(tabName.equalsIgnoreCase("Action")){
			holder.btnGrab.setVisibility(View.VISIBLE);
			holder.tvDetails.setText(mActionBetsList.get(position).getDescription().trim());
			if (mActionBetsList.get(position).getUser_thumbnailAvatarURL() != null)
				imageLoader.displayImage(mActionBetsList.get(position).getUser_thumbnailAvatarURL().trim(),
						holder.imgBtnThumbnail, options, animateFirstListener);
		}
		
		holder.btnGrab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				grabListerner.onGrab(mActionBetsList.get(position).getId(), tabName);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		ImageButton imgBtnThumbnail,imgBtnUserBorder;
		TextView tvDetails, tvTime;
		Button btnGrab;
	}
	
	private class AnimateFirstDisplayListener extends
	SimpleImageLoadingListener {

		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {

				ImageView imageView = (ImageView) view;

				if (imageView.getId() == R.id.imgBtnThumbnail) {
					Bitmap imageBitmap = getRoundedShape(loadedImage, true);
					imageView.setImageBitmap(imageBitmap);
				}

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage, boolean isSmall) {

		int targetWidth = 0;
		int targetHeight = 0;
		if (isSmall) {
			if(deviceType.equalsIgnoreCase("xlarge")){
				targetWidth = 85;
				targetHeight = 85;
			}else if(deviceType.equalsIgnoreCase("large")){
				targetWidth = 60;
				targetHeight = 60;
			}else{
				targetWidth = 35;
				targetHeight = 35;
			}
		} else {
			targetWidth = 180;
			targetHeight = 180;
		}
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
						targetHeight), null);
		return targetBitmap;
	}
	
	private String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	private String calculateDifference(long date) {

		DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long l1 = date;
			long l2 = writeFormat.parse(getDateTime()).getTime();
			long diff = l2 - l1;

			long secondInMillis = 1000;
			long minuteInMillis = secondInMillis * 60;
			long hourInMillis = minuteInMillis * 60;
			long dayInMillis = hourInMillis * 24;
			long yearInMillis = dayInMillis * 365;

			long elapsedYears = diff / yearInMillis;
			diff = diff % yearInMillis;
			long elapsedDays = diff / dayInMillis;
			diff = diff % dayInMillis;
			long elapsedHours = diff / hourInMillis;
			diff = diff % hourInMillis;
			long elapsedMinutes = diff / minuteInMillis;
			diff = diff % minuteInMillis;
			long elapsedSeconds = diff / secondInMillis;

			if (elapsedYears == 0 && elapsedDays == 0 && elapsedHours == 0
					&& elapsedMinutes == 0 && elapsedSeconds != 0) {
				return String.valueOf(elapsedSeconds) + " seconds ago";
			} else if (elapsedYears == 0 && elapsedDays == 0
					&& elapsedHours == 0 && elapsedMinutes != 0) {
				return String.valueOf(elapsedMinutes) + " minute ago";
				// return minute
			} else if (elapsedYears == 0 && elapsedDays == 0
					&& elapsedHours != 0) {
				return String.valueOf(elapsedHours) + " hours ago";
			} else if ((elapsedYears == 0 && elapsedDays != 0)) {
				return String.valueOf(elapsedDays) + " days ago";
			} else if (elapsedYears != 0) {
				return String.valueOf(elapsedYears) + " years ago";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "Error";
	}

}
