package com.byb.gambler.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.byb.gambler.R;
import com.byb.gambler.models.InitiatorUser;
import com.byb.gambler.models.Match;
import com.byb.gambler.models.OpenBets;
import com.byb.gambler.models.Team;
import com.byb.gambler.models.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class OpenBetsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<OpenBets> mOpenBetsList;
	private Context context;
	private boolean isOpen;
	private ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener;
	private DisplayImageOptions options;
	
	private interface spinnerChangeListner{
		public void textChanged(int value);
	}

	public OpenBetsAdapter(Context context) {
		this.context = context;
	}

	public OpenBetsAdapter(Context context, ArrayList<OpenBets> openBets,
			boolean isOpen, DisplayImageOptions options) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mOpenBetsList = openBets;
		this.isOpen = isOpen;
		this.options = options;
		animateFirstListener = new AnimateFirstDisplayListener();
		imageLoader = ImageLoader.getInstance();
	}
	
	public void updateListAdapter(ArrayList<OpenBets> openBets){
		this.mOpenBetsList = openBets;
		this.notifyDataSetChanged();
	}

	public void clearList() {
		this.mOpenBetsList.clear();
		this.notifyDataSetChanged();
	}

	public int getCount() {
		return mOpenBetsList.size();
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
			convertView = mInflater.inflate(R.layout.bets_matches_list_item,
					null);
			holder = new ViewHolder();

			holder.imgBtnTeamLogoA = (ImageButton) convertView
					.findViewById(R.id.imgBtnTeamLogoA);
			holder.imgBtnTeamLogoB = (ImageButton) convertView
					.findViewById(R.id.imgBtnTeamLogoB);
			holder.imgBtnUserLogoA = (ImageButton) convertView
					.findViewById(R.id.imgBtnUserLogoA);
			holder.imgBtnUserLogoB = (ImageButton) convertView
					.findViewById(R.id.imgBtnUserLogoB);
			holder.tvBetAmount = (TextView) convertView
					.findViewById(R.id.tvBetAmount);
			holder.tvBetTime = (TextView) convertView
					.findViewById(R.id.tvBetTime);
			holder.tvTeamNameA = (TextView) convertView
					.findViewById(R.id.tvTeamNameA);
			holder.tvTeamNameB = (TextView) convertView
					.findViewById(R.id.tvTeamNameB);
			holder.btnAcceptBet = (TextView) convertView
					.findViewById(R.id.btnAcceptBet);
			holder.imgBtnUserBorderB = (ImageButton) convertView
					.findViewById(R.id.imgBtnUserborderB);
			holder.imgBtnUserBorderA = (ImageButton) convertView
					.findViewById(R.id.imgBtnUserborderA);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (isOpen) {
			holder.tvBetAmount.setVisibility(View.VISIBLE);
			holder.tvBetTime.setVisibility(View.GONE);
			holder.btnAcceptBet.setVisibility(View.VISIBLE);
		} else {
			holder.tvBetAmount.setVisibility(View.VISIBLE);
			holder.tvBetTime.setVisibility(View.VISIBLE);
			holder.btnAcceptBet.setVisibility(View.GONE);
		}

		OpenBets openBets = mOpenBetsList.get(position);
		Match match = openBets.getMatch();
		InitiatorUser user = openBets.getInitiatorUser();
		Team teamA = match.getTeamA();
		Team teamB = match.getTeamB();

		if (!isOpen) {
			if (match.getEndTime() != null) {
				if (match.getEndTime().equalsIgnoreCase("null")) {
					holder.tvBetTime.setText(calculateDifference(0));
				} else {
					holder.tvBetTime.setText(calculateDifference(Long
							.parseLong(match.getEndTime())));
				}
			}

		}

		if (openBets.getBetTeamID().equalsIgnoreCase(teamA.getID())) {
			holder.imgBtnUserBorderB
			.setBackgroundResource(R.drawable.image_border);
			holder.imgBtnUserBorderA
			.setBackgroundResource(R.drawable.image_border_white);
		} else if (openBets.getBetTeamID().equalsIgnoreCase(teamB.getID())) {
			holder.imgBtnUserBorderA
			.setBackgroundResource(R.drawable.image_border);
			holder.imgBtnUserBorderB
			.setBackgroundResource(R.drawable.image_border_white);
		}

		if (isOpen) {
			if(openBets.getInitiatorUserID().equalsIgnoreCase(UserInfo.getInstance().getID()) &&
					openBets.getAcceptedUser() != null){
				holder.btnAcceptBet.setBackgroundResource(R.drawable.spinner_small2);
//				holder.btnAcceptBet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ball, 0, R.drawable.spinner_right, 0);
//				holder.btnAcceptBet.setPadding(10, 0, 0, 0);
				holder.btnAcceptBet.setVisibility(View.VISIBLE);
				holder.btnAcceptBet.setText(openBets.getAmount());
				
				
			}else{
				holder.btnAcceptBet.setText("Accept");
				holder.btnAcceptBet.setBackgroundResource(R.drawable.bg_sign_in);
				holder.btnAcceptBet.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}}

		holder.tvTeamNameA.setText(teamA.getName());
		holder.tvTeamNameB.setText(teamB.getName());
		holder.tvBetAmount.setText(openBets.getAmount());
		if (teamA.getLogoImage() != null)
			imageLoader.displayImage(teamA.getLogoImage().trim(),
					holder.imgBtnTeamLogoA, options, animateFirstListener);
		if (teamB.getLogoImage() != null)
			imageLoader.displayImage(teamB.getLogoImage().trim(),
					holder.imgBtnTeamLogoB, options, animateFirstListener);
		if (UserInfo.getInstance().getThumbNailURL() != null)
			imageLoader.displayImage(UserInfo.getInstance().getThumbNailURL()
					.trim(), holder.imgBtnUserLogoA, options,
					animateFirstListener);
		if (user.getThumbNail() != null)
			imageLoader.displayImage(user.getThumbNail().trim(),
					holder.imgBtnUserLogoB, options, animateFirstListener);
		System.out.println("Image url = " + teamA.getLogoImage());
		// holder.imgTeamA.setImageBitmap(teamA.getLogoImage());
		// holder.spBet.setAdapter(new ArrayAdapter<Integer>(context,
		// android.R.layout.simple_spinner_dropdown_item, getBetList()));

		holder.btnAcceptBet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView btn = (TextView) v; 
				if(btn.getText().equals("Accept")){

				}else{
					int betAmount = Integer.parseInt(btn.getText().toString());
					int totalAmount = UserInfo.getInstance().getUserStatistics().getBallsAvailable();
					onCreateDialog(totalAmount, betAmount, new spinnerChangeListner() {
						
						@Override
						public void textChanged(int value) {
							holder.btnAcceptBet.setText(""+value);
						}
					}).show();
				}
			}
		});
		return convertView;
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

				if (imageView.getId() == R.id.imgBtnUserLogoA
						|| imageView.getId() == R.id.imgBtnUserLogoB) {
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

	static class ViewHolder {
		ImageButton imgBtnTeamLogoA, imgBtnUserLogoA, imgBtnTeamLogoB,
		imgBtnUserLogoB, imgBtnUserBorderB, imgBtnUserBorderA;
		TextView tvTeamNameA, tvTeamNameB, tvBetAmount, tvBetTime;
		TextView btnAcceptBet;
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage, boolean isSmall) {

		int targetWidth = 0;
		int targetHeight = 0;
		if (isSmall) {
			targetWidth = 40;
			targetHeight = 40;
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

	private ArrayList<Integer> getBetList() {

		ArrayList<Integer> betCountList = new ArrayList<Integer>();
		for (int i = 0; i <= UserInfo.getInstance().getUserStatistics()
				.getBallsAvailable(); i = i + 5) {
			betCountList.add(i);
		}
		return betCountList;
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

	public Dialog onCreateDialog(int totalAmount, int betAmount, final spinnerChangeListner listner) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String[] nums = new String[totalAmount / 5];

		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i * 5);

		final NumberPicker np = new NumberPicker(context);// (NumberPicker)
		np.setMaxValue(nums.length - 1);
		np.setMinValue(0);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(nums);
		np.setValue(betAmount/5);
		np.setLayoutParams(new FrameLayout.LayoutParams(50, 50, Gravity.CENTER));

		builder.setView(np)
	    // Add action buttons
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listner.textChanged(np.getValue()*5);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
         	   dialog.cancel();
            }
        });      
		return builder.create();
	}
}
