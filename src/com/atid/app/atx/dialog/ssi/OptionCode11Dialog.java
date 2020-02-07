package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.Code11CheckDigitVerification;
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

public class OptionCode11Dialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionCode11Dialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearLenght1;
	private LinearLayout linearLenght2;
	
	private TextView txtLengthType;
	private TextView txtLength1;
	private TextView txtLength2;
	private TextView txtCode11CheckDigitVerification;
	private CheckBox chkTransmitCode11CheckDigit;
	
	private EnumListDialog dlgLengthType;
	private NumberDialog dlgLength1;
	private NumberDialog dlgLength2;
	private EnumListDialog dlgCode11CheckDigitVerification;
	
	private CodeLength mLength;
	private Code11CheckDigitVerification mCode11CheckDigitVerification;
	private boolean mIsTransmitCode11CheckDigit;
	
	private Context mContext;
	
	public OptionCode11Dialog(){
		super();
		
		mLength = new CodeLength(4, 55);
		mCode11CheckDigitVerification = Code11CheckDigitVerification.Disable;
		mIsTransmitCode11CheckDigit = false;
		
		mContext = null;
	}
	
	public CodeLength getLength(){
		return mLength;
	}
	
	public void setLength(final CodeLength value) {
		mLength.setLength(value.getLength1(), value.getLength2());

		if(mLength.getLength1() ==0 && mLength.getLength2() == 0){	
			mLength.setLengthType(CodeLengthType.AnyLength);
		} else if( mLength.getLength1() !=0 && mLength.getLength2() == 0 ) { 
			mLength.setLengthType(CodeLengthType.OneLength);
		} else if( mLength.getLength1() > mLength.getLength2() ) {
			mLength.setLengthType(CodeLengthType.TwoLength);
		} else { 
			mLength.setLengthType(CodeLengthType.Range);
		}		
	}
	
	public Code11CheckDigitVerification getCode11CheckDigitVerification(){
		return mCode11CheckDigitVerification;
	}
	
	public void setCode11CheckDigitVerification(Code11CheckDigitVerification value){
		mCode11CheckDigitVerification = value;
	}
	
	public boolean getTransmitCode11CheckDigit(){
		return mIsTransmitCode11CheckDigit;
	}
	
	public void setTransmitCode11CheckDigit(boolean value){
		mIsTransmitCode11CheckDigit = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_code11, null);
		
		linearLenght1 = (LinearLayout) root.findViewById(R.id.linear_length1);
		linearLenght2 = (LinearLayout) root.findViewById(R.id.linear_length2);
		
		txtLengthType = (TextView) root.findViewById(R.id.length_type);
		txtLengthType.setOnClickListener(this);
		txtLength1 = (TextView) root.findViewById(R.id.length1);
		txtLength1.setOnClickListener(this);
		txtLength2 = (TextView) root.findViewById(R.id.length2);
		txtLength2.setOnClickListener(this);
		txtCode11CheckDigitVerification = (TextView) root.findViewById(R.id.code11_check_digit_verification); 
		txtCode11CheckDigitVerification.setOnClickListener(this);
		chkTransmitCode11CheckDigit = (CheckBox) root.findViewById(R.id.transmit_code11_check_digit);
		chkTransmitCode11CheckDigit.setOnCheckedChangeListener(this);

		dlgLengthType = new EnumListDialog(txtLengthType, CodeLengthType.values());
		dlgLengthType.setValue(mLength.getLengthType());
		dlgLength1 = new NumberDialog(txtLength1);
		dlgLength1.setValue(mLength.getLength1());
		dlgLength2 = new NumberDialog(txtLength2);
		dlgLength2.setValue(mLength.getLength2());
		dlgCode11CheckDigitVerification = new EnumListDialog(txtCode11CheckDigitVerification , Code11CheckDigitVerification.values());
		dlgCode11CheckDigitVerification.setValue(mCode11CheckDigitVerification);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
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
				
				mCode11CheckDigitVerification = (Code11CheckDigitVerification) dlgCode11CheckDigitVerification.getValue();
				mIsTransmitCode11CheckDigit = chkTransmitCode11CheckDigit.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionCode11Dialog.this);
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionCode11Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionCode11Dialog.this);
				
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
				
				dlgCode11CheckDigitVerification.display();
				chkTransmitCode11CheckDigit.setChecked(mIsTransmitCode11CheckDigit);
				
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
			dlgLengthType.showDialog(mContext, R.string.length_type , new BaseDialog.IValueChangedListener() {
				
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
		case R.id.code11_check_digit_verification:
			dlgCode11CheckDigitVerification.showDialog(mContext, R.string.code11_check_digit_verification);
			break;
		
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.transmit_code11_check_digit:
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
