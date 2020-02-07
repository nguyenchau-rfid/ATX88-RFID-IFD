package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.CodeLength;
import com.atid.lib.module.barcode.ssi.type.CodeLengthType;
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
import android.widget.TextView;

public class OptionSSI1DCode39Dialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionSSI1DCode39Dialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearLenght1;
	private LinearLayout linearLenght2;
	
	private CheckBox chkCode32Prefix;
	private TextView txtLengthType;
	private TextView txtLength1;
	private TextView txtLength2;
	private CheckBox chkCode39CheckDigitVerification;
	private CheckBox chkTransmitCode39CheckDigit;
	private CheckBox chkCode39FullAsciiConversion;
	
	private EnumListDialog dlgLengthType;
	private NumberDialog dlgLength1;
	private NumberDialog dlgLength2;
	
	private boolean mIsCode32Prefix;
	private CodeLength mLength;
	private boolean mIsCode39CheckDigitVerification;
	private boolean mIsTransmitCode39CheckDigit;
	private boolean mIsCode39FullAsciiConversion;
	
	private Context mContext;
	
	public OptionSSI1DCode39Dialog(){
		super();
		
		mIsCode32Prefix = false;
		mLength = new CodeLength(2, 55);
		mIsCode39CheckDigitVerification = false;
		mIsTransmitCode39CheckDigit = false;
		mIsCode39FullAsciiConversion = false;
		
		mContext = null;
	}
	
	public boolean getCode32Prefix(){
		return mIsCode32Prefix;
	}
	
	public void setCode32Prefix(boolean value){
		mIsCode32Prefix = value;
	}
	
	public CodeLength getLength(){
		return mLength;
	}
	
	public void setLength(final CodeLength value){
		mLength.setLength(value.getLength1(), value.getLength2());

		if(mLength.getLength1() == 0 && mLength.getLength2() == 0){	
			mLength.setLengthType(CodeLengthType.AnyLength);
		} else if( mLength.getLength1() !=0 && mLength.getLength2() == 0 ) { 
			mLength.setLengthType(CodeLengthType.OneLength);
		} else if( mLength.getLength1() > mLength.getLength2() ) {
			mLength.setLengthType(CodeLengthType.TwoLength);
		} else { 
			mLength.setLengthType(CodeLengthType.Range);
		}		
	}

	public boolean getCode39CheckDigitVerification(){
		return mIsCode39CheckDigitVerification;
	}
	
	public void setCode39CheckDigitVerification(boolean value){
		mIsCode39CheckDigitVerification = value;
	}

	public boolean getTransmitCode39CheckDigit(){
		return mIsTransmitCode39CheckDigit;
	}
	
	public void setTransmitCode39CheckDigit(boolean value){
		mIsTransmitCode39CheckDigit = value;
	}

	public boolean getCode39FullAsciiConversion(){
		return mIsCode39FullAsciiConversion;
	}
	
	public void setCode39FullAsciiConversion(boolean value){
		mIsCode39FullAsciiConversion = value;
	}

	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_1d_code39, null);
		
		linearLenght1 = (LinearLayout) root.findViewById(R.id.linear_length1);
		linearLenght2 = (LinearLayout) root.findViewById(R.id.linear_length2);
		
		chkCode32Prefix = (CheckBox) root.findViewById(R.id.code32_prefix);  
		chkCode32Prefix.setOnCheckedChangeListener(this);
		txtLengthType = (TextView) root.findViewById(R.id.length_type);
		txtLengthType.setOnClickListener(this);
		txtLength1 = (TextView) root.findViewById(R.id.length1);
		txtLength1.setOnClickListener(this);
		txtLength2 = (TextView) root.findViewById(R.id.length2);
		txtLength2.setOnClickListener(this);
		chkCode39CheckDigitVerification = (CheckBox) root.findViewById(R.id.code39_check_digit_verification);
		chkCode39CheckDigitVerification.setOnCheckedChangeListener(this);
		chkTransmitCode39CheckDigit = (CheckBox) root.findViewById(R.id.transmit_code39_check_digit);
		chkTransmitCode39CheckDigit.setOnCheckedChangeListener(this);
		chkCode39FullAsciiConversion = (CheckBox) root.findViewById(R.id.code39_full_ascii_conversion);
		chkCode39FullAsciiConversion.setOnCheckedChangeListener(this);
		
		dlgLengthType = new EnumListDialog(txtLengthType, CodeLengthType.values());
		dlgLengthType.setValue(mLength.getLengthType());
		dlgLength1 = new NumberDialog(txtLength1);
		dlgLength1.setValue(mLength.getLength1());
		dlgLength2 = new NumberDialog(txtLength2);
		dlgLength2.setValue(mLength.getLength2());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.code39);
		builder.setView(root);
		builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mLength.setLengthType((CodeLengthType)dlgLengthType.getValue());
				if(dlgLengthType.getValue() == CodeLengthType.AnyLength) {
					mLength.setLength(0, 0);
				} else if(dlgLengthType.getValue() == CodeLengthType.OneLength) {
					mLength.setLength(dlgLength1.getValue(), 0);
				} else {
					mLength.setLength(dlgLength1.getValue(), dlgLength2.getValue());	
				}
				
				mIsCode32Prefix = chkCode32Prefix.isChecked();
				mIsCode39CheckDigitVerification = chkCode39CheckDigitVerification.isChecked();
				mIsTransmitCode39CheckDigit = chkTransmitCode39CheckDigit.isChecked();
				mIsCode39FullAsciiConversion = chkCode39FullAsciiConversion.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionSSI1DCode39Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI1DCode39Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI1DCode39Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				
				dlgLengthType.display();
				dlgLength1.display();
				dlgLength2.display();
				displayLength();
				
				chkCode32Prefix.setChecked(mIsCode32Prefix);
				chkCode39CheckDigitVerification.setChecked(mIsCode39CheckDigitVerification);
				chkTransmitCode39CheckDigit.setChecked(mIsTransmitCode39CheckDigit);
				chkCode39FullAsciiConversion.setChecked(mIsCode39FullAsciiConversion);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onShow()");
			}
		});
		dialog.show();
		
		ATLog.i(TAG, INFO, "INFO. showDialog()");
	}

	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.length_type:
			dlgLengthType.showDialog(mContext, R.string.length_type, new BaseDialog.IValueChangedListener() {
				
				@Override
				public void onValueChanged(BaseDialog dialog) {
					displayLength();
				}
			});
			break;
		case R.id.length1:
			dlgLength1.showDialog(mContext, R.string.length1);
			break;
		case R.id.length2:
			dlgLength2.showDialog(mContext, R.string.length2);
			break;
		
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.code32_prefix:
			break;
		case R.id.code39_check_digit_verification:
			break;
		case R.id.transmit_code39_check_digit:
			break;
		case R.id.code39_full_ascii_conversion:
			break;
		case R.id.code39_buffering_scan_store:
			break;
		case R.id.code39_reduced_quiet_zone:
			break;

		}
	}
	
	private void displayLength(){
		switch((CodeLengthType) dlgLengthType.getValue()){
		case AnyLength:
			linearLenght1.setVisibility(View.GONE);
			linearLenght2.setVisibility(View.GONE);
			break;
		case OneLength:
			linearLenght1.setVisibility(View.VISIBLE);
			linearLenght2.setVisibility(View.GONE);
			break;
		case TwoLength:
			linearLenght1.setVisibility(View.VISIBLE);
			linearLenght2.setVisibility(View.VISIBLE);
			break;
		case Range:
			linearLenght1.setVisibility(View.VISIBLE);
			linearLenght2.setVisibility(View.VISIBLE);
			break;
		}		
	}
}
