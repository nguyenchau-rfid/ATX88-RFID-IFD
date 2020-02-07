package com.atid.app.atx.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class CheckableLinearLayout extends LinearLayout implements Checkable {

	private final String NS = "http://schemas.android.com/apk/read/com.atid.app.atx.widgets";
	private final String ATTR = "checkable";
	
	private int mCheckableId;
	private Checkable mCheckable;
	
	public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		
		mCheckableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
	}

	public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mCheckableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
	}

	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mCheckableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
	}

	@Override
	public void setChecked(boolean checked) {
		mCheckable = (Checkable) findViewById(mCheckableId);
		if (mCheckable == null)
			return;
		mCheckable.setChecked(checked);
	}

	@Override
	public boolean isChecked() {
		mCheckable = (Checkable) findViewById(mCheckableId);
		if (mCheckable == null)
			return false;
		return mCheckable.isChecked();
	}

	@Override
	public void toggle() {
		mCheckable = (Checkable) findViewById(mCheckableId);
		if (mCheckable == null)
			return;
		mCheckable.toggle();
	}
}
