package com.atid.app.atx.dialog.spc;

import com.atid.app.atx.R;
import com.atid.app.atx.dialog.BaseDialog;
import com.atid.lib.util.diagnotics.ATLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class OptionEanJan8 extends BaseDialog implements OnCheckedChangeListener{
	private static final String TAG = OptionEanJan8.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private CheckBox chkEANJAN8CheckDigit;
	private CheckBox chkEANJAN8Addenda2Digit;
	private CheckBox chkEANJAN8Addenda5Digit;
	private CheckBox chkEANJAN8AddendaRequired;
	private CheckBox chkEANJAN8AddendaSeparator;

	private boolean mIsEANJAN8CheckDigit;
	private boolean mIsEANJAN8Addenda2Digit;
	private boolean mIsEANJAN8Addenda5Digit;
	private boolean mIsEANJAN8AddendaRequired;
	private boolean mIsEANJAN8AddendaSeparator;
	
	@SuppressWarnings("unused")
	private Context mContext;
	
	public OptionEanJan8() {
		super();
		
		mIsEANJAN8CheckDigit = true;      
		mIsEANJAN8Addenda2Digit = false;   
		mIsEANJAN8Addenda5Digit = false;  
		mIsEANJAN8AddendaRequired = false; 
		mIsEANJAN8AddendaSeparator = true;
		
		mContext = null;
	}
	
	public boolean getEANJAN8CheckDigit(){
		return mIsEANJAN8CheckDigit;
	}
	
	public void setEANJAN8CheckDigit(boolean value){
		mIsEANJAN8CheckDigit = value;
	}

	public boolean getEANJAN8Addenda2Digit(){
		return mIsEANJAN8Addenda2Digit;
	}
	
	public void setEANJAN8Addenda2Digit(boolean value){
		mIsEANJAN8Addenda2Digit = value;
	}

	public boolean getEANJAN8Addenda5Digit(){
		return mIsEANJAN8Addenda5Digit;
	}
	
	public void setEANJAN8Addenda5Digit(boolean value){
		mIsEANJAN8Addenda5Digit = value;
	}

	public boolean getEANJAN8AddendaRequired(){
		return mIsEANJAN8AddendaRequired;
	}
	
	public void setEANJAN8AddendaRequired(boolean value){
		mIsEANJAN8AddendaRequired = value;
	}

	public boolean getEANJAN8AddendaSeparator(){
		return mIsEANJAN8AddendaSeparator;
	}
	
	public void setEANJAN8AddendaSeparator(boolean value){
		mIsEANJAN8AddendaSeparator = value;
	}

	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {

		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_spc_2d_ean_jan8, null);
		
		chkEANJAN8CheckDigit = (CheckBox) root.findViewById(R.id.ean_jan8_check_digit);     
		chkEANJAN8CheckDigit.setOnCheckedChangeListener(this);
		chkEANJAN8Addenda2Digit = (CheckBox) root.findViewById(R.id.ean_jan8_ddenda_2_digit);
		chkEANJAN8Addenda2Digit.setOnCheckedChangeListener(this);
		chkEANJAN8Addenda5Digit = (CheckBox) root.findViewById(R.id.ean_jan8_addenda_5_digit);   
		chkEANJAN8Addenda5Digit.setOnCheckedChangeListener(this);
		chkEANJAN8AddendaRequired = (CheckBox) root.findViewById(R.id.ean_jan8_addenda_required);
		chkEANJAN8AddendaRequired.setOnCheckedChangeListener(this);
		chkEANJAN8AddendaSeparator = (CheckBox) root.findViewById(R.id.ean_jan8_addenda_separator);
		chkEANJAN8AddendaSeparator.setOnCheckedChangeListener(this);

		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				mIsEANJAN8CheckDigit = chkEANJAN8CheckDigit.isChecked();
				mIsEANJAN8Addenda2Digit = chkEANJAN8Addenda2Digit.isChecked();
				mIsEANJAN8Addenda5Digit = chkEANJAN8Addenda5Digit.isChecked();
				mIsEANJAN8AddendaRequired =chkEANJAN8AddendaRequired.isChecked();
				mIsEANJAN8AddendaSeparator = chkEANJAN8AddendaSeparator.isChecked();
				
				if(changedListener != null){
					changedListener.onValueChanged(OptionEanJan8.this);
				}
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(cancelListener != null){
					cancelListener.onCanceled(OptionEanJan8.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				if(cancelListener != null) {
					cancelListener.onCanceled(OptionEanJan8.this);
				}
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
			
				chkEANJAN8CheckDigit.setChecked(mIsEANJAN8CheckDigit); 
				chkEANJAN8Addenda2Digit.setChecked(mIsEANJAN8Addenda2Digit);   
				chkEANJAN8Addenda5Digit.setChecked(mIsEANJAN8Addenda5Digit);
				chkEANJAN8AddendaRequired.setChecked(mIsEANJAN8AddendaRequired);
				chkEANJAN8AddendaSeparator.setChecked(mIsEANJAN8AddendaSeparator);

				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.ean_jan8_check_digit:
			break;
		case R.id.ean_jan8_ddenda_2_digit:
			break;
		case R.id.ean_jan8_addenda_5_digit:
			break;
		case R.id.ean_jan8_addenda_required:
			break;
		case R.id.ean_jan8_addenda_separator:
			break;
		}
		
	}

}
