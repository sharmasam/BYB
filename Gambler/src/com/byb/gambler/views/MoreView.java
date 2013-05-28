package com.byb.gambler.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.byb.gambler.R;

public class MoreView extends RelativeLayout {

	View rootLayout;
	LayoutInflater linflator;
	private static MoreView ref;
	
	public static MoreView getView(Context context) {
//		if (ref == null)
			ref = new MoreView(context);
		return ref;
	}

	private MoreView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (linflator == null)
			linflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rootLayout = linflator.inflate(R.layout.home,(ViewGroup) findViewById(R.id.rlHome));
		addView(rootLayout);
	}
	
	public static void nullifyView() {
		ref = null;
	}

}
