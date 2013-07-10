package com.tcloud.sample;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconView extends LinearLayout {

	private ImageView icon;
	private TextView fileName;
	
	public IconView(Context context, int iconResId, String name) {
		super(context);
		
		setOrientation(VERTICAL);
		setPadding(3, 3, 3, 3);
		setGravity(Gravity.CENTER_HORIZONTAL);
		
		icon = new ImageView(context);
		icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		icon.setImageResource(iconResId);
		addView(icon, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		fileName = new TextView(context);
		fileName.setSingleLine();
		fileName.setEllipsize(TextUtils.TruncateAt.END);
		fileName.setText(name);
		addView(fileName, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void select() {
		fileName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
	}
	
	public void deselect() {
		fileName.setEllipsize(TextUtils.TruncateAt.END);
	}
	
	public void setIconResId(int resId) {
		icon.setImageResource(resId);
	}
	
	public void setFileName(String name) {
		fileName.setText(name);
	}
}
