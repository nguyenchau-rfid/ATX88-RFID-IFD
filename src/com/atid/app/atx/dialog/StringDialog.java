package com.atid.app.atx.dialog;

import android.widget.TextView;

public abstract class StringDialog extends BaseDialog {

	protected String mValue;
	
	public StringDialog() {
		super();
		mValue = "";
	}
	
	public StringDialog(TextView view) {
		super(view);
		mValue = "";
	}

	public String getValue() {
		return mValue;
	}
	
	public void setValue(String value) {
		mValue = value;
	}
	
	@Override
	public void display() {
		if (txtValue == null)
			return;
		txtValue.setText(mValue);
	}
}
