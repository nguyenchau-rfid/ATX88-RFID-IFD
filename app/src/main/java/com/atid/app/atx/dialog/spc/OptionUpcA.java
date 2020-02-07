package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
//import com.atid.app.atx.dialog.NumberDialog;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
//import android.widget.TextView;

public class OptionUpcA extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionUpcA.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkUPCACheckDigit;
	private CheckBox chkUPCANumberSystem;
	private CheckBox chkUPCAAddenda2Digit;
	private CheckBox chkUPCAAddenda5Digit;
	private CheckBox chkUPCAAddendaRequired;
//	private TextView txtAddendaTimeout;
	private CheckBox chkUPCAAddendaSeparator;
	
//	private NumberDialog dlgAddendaTimeout;
	
	private boolean mIsUPCACheckDigit;
	private boolean mIsUPCANumberSystem;
	private boolean mIsUPCAAddenda2Digit;
	private boolean mIsUPCAAddenda5Digit;
	private boolean mIsUPCAAddendaRequired;
//	private int mAddendaTimeout;
	private boolean mIsUPCAAddendaSeparator;
	
	@SuppressWarnings("unused")
	private Context mContext;

	public OptionUpcA() {
		super();
		
		mIsUPCACheckDigit = true;     
		mIsUPCANumberSystem = true;   
		mIsUPCAAddenda2Digit = false;  
		mIsUPCAAddenda5Digit = false;  
		mIsUPCAAddendaRequired = false;
		//mAddendaTimeout = 100;             
		mIsUPCAAddendaSeparator = true;  
		
		mContext = null;
	}
	
	public boolean getUPCACheckDigit() {
		return mIsUPCACheckDigit;
	}
	
	public void setUPCACheckDigit(boolean value){
		mIsUPCACheckDigit = value;
	}
	
	public boolean getUPCANumberSystem() {
		return mIsUPCANumberSystem;
	}
	
	public void setUPCANumberSystem(boolean value){
		mIsUPCANumberSystem = value;
	}
	
	public boolean getUPCAAddenda2Digit() {
		return mIsUPCAAddenda2Digit;
	}
	
	public void setUPCAAddenda2Digit(boolean value){
		mIsUPCAAddenda2Digit = value;
	}
	public boolean getUPCAAddenda5Digit() {
		return mIsUPCAAddenda5Digit;
	}
	
	public void setUPCAAddenda5Digit(boolean value){
		mIsUPCAAddenda5Digit = value;
	}
	public boolean getUPCAAddendaRequired() {
		return mIsUPCAAddendaRequired;
	}
	
	public void setUPCAAddendaRequired(boolean value){
		mIsUPCAAddendaRequired = value;
	}
	
//	public int getAddendaTimeout() {
//		return mAddendaTimeout;
//	}
//	
//	public void setAddendaTimeout(int value){
//		mAddendaTimeout = value;
//	}
	
	public boolean getUPCAAddendaSeparator() {
		return mIsUPCAAddendaSeparator;
	}
	
	public void setUPCAAddendaSeparator(boolean value){
		mIsUPCAAddendaSeparator = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_upca, null);
		
		chkUPCACheckDigit = (CheckBox) root.findViewById(R.id.upca_check_digit);
		chkUPCACheckDigit.setOnCheckedChangeListener(this);
		chkUPCANumberSystem = (CheckBox) root.findViewById(R.id.upca_number_system);
		chkUPCANumberSystem.setOnCheckedChangeListener(this);
		chkUPCAAddenda2Digit = (CheckBox) root.findViewById(R.id.upca_addenda_2_digit);
		chkUPCAAddenda2Digit.setOnCheckedChangeListener(this);
		chkUPCAAddenda5Digit = (CheckBox) root.findViewById(R.id.upca_addenda_5_digit);
		chkUPCAAddenda5Digit.setOnCheckedChangeListener(this);
		chkUPCAAddendaRequired = (CheckBox) root.findViewById(R.id.upca_addenda_required);
		chkUPCAAddendaRequired.setOnCheckedChangeListener(this);
//		txtAddendaTimeout = (TextView) root.findViewById(R.id.addenda_timeout);
//		txtAddendaTimeout.setOnClickListener(this);
		chkUPCAAddendaSeparator = (CheckBox) root.findViewById(R.id.upca_addenda_separator);
		chkUPCAAddendaSeparator.setOnCheckedChangeListener(this);
		
//		dlgAddendaTimeout = new NumberDialog(txtAddendaTimeout);
//		dlgAddendaTimeout.setValue(mAddendaTimeout);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
//				mAddendaTimeout = dlgAddendaTimeout.getValue();
				
				mIsUPCACheckDigit = chkUPCACheckDigit.isChecked();
				mIsUPCANumberSystem = chkUPCANumberSystem.isChecked();
				mIsUPCAAddenda2Digit = chkUPCAAddenda2Digit.isChecked();
				mIsUPCAAddenda5Digit = chkUPCAAddenda5Digit.isChecked();
				mIsUPCAAddendaRequired = chkUPCAAddendaRequired.isChecked();
				mIsUPCAAddendaSeparator = chkUPCAAddendaSeparator.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionUpcA.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionUpcA.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionUpcA.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
//				dlgAddendaTimeout.display();
				
				chkUPCACheckDigit.setChecked(mIsUPCACheckDigit);
				chkUPCANumberSystem.setChecked(mIsUPCANumberSystem);
				chkUPCAAddenda2Digit.setChecked(mIsUPCAAddenda2Digit);
				chkUPCAAddenda5Digit.setChecked(mIsUPCAAddenda5Digit);
				chkUPCAAddendaRequired.setChecked(mIsUPCAAddendaRequired);
				chkUPCAAddendaSeparator.setChecked(mIsUPCAAddendaSeparator);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
//		case R.id.addenda_timeout:
//			dlgAddendaTimeout.showDialog(mContext, R.string.addenda_timeout);
//			break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.upca_check_digit:
			break;
		case R.id.upca_number_system:
			break;
		case R.id.upca_addenda_2_digit:
			break;
		case R.id.upca_addenda_5_digit:
			break;
		case R.id.upca_addenda_required:
			break;
		case R.id.upca_addenda_separator:
			break;
		}
		
	}

}
