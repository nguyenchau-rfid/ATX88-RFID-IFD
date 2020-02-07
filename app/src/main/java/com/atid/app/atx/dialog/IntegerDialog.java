package com.atid.app.atx.dialog;

import java.util.Locale;

import android.widget.TextView;

public abstract class IntegerDialog extends BaseDialog {

	protected int mValue;
	
	public IntegerDialog() {
		super();
		mValue = 0;
	}
	
	public IntegerDialog(TextView view) {
		super(view);
		mValue = 0;
	}
	
	public int getValue() {
		return mValue;
	}
	
	public void setValue(int value) {
		mValue = value;
	}

	@Override
	public void display() {
		
		if (txtValue == null)
			return;
		
		txtValue.setText(String.format(Locale.US, "%d", mValue));
	}
}
