package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.CodeLength;
import com.atid.lib.module.barcode.ssi.type.CodeLengthType;
import com.atid.lib.module.barcode.ssi.type.I2of5CheckDigitVerification;
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

public class OptionSSI2DI2of5Dialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionSSI2DI2of5Dialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearLenght1;
	private LinearLayout linearLenght2;
	
	private TextView txtLengthType;
	private TextView txtLength1;
	private TextView txtLength2;
	private TextView txtI2of5CheckDigitVerification;
	private CheckBox chkTransmitI2of5CheckDigit;
	private CheckBox chkConvertI2of5ToEan13;
	private CheckBox chkI2of5ReducedQuietZone;
	
	private EnumListDialog dlgLengthType;
	private NumberDialog dlgLength1;
	private NumberDialog dlgLength2;
	private EnumListDialog dlgI2of5CheckDigitVerification;
	
	private CodeLength mLength;
	private I2of5CheckDigitVerification mI2of5CheckDigitVerification;
	private boolean mIsTransmitI2of5CheckDigit;
	private boolean mIsConvertI2of5ToEan13;
	private boolean mIsI2of5ReducedQuietZone;
	
	private Context mContext;
	
	public OptionSSI2DI2of5Dialog(){
		super();
		
		mLength = new CodeLength(14, 0);
		mI2of5CheckDigitVerification = I2of5CheckDigitVerification.Disable;
		mIsTransmitI2of5CheckDigit = false;
		mIsConvertI2of5ToEan13 = false;
		mIsI2of5ReducedQuietZone = false;
		
		mContext = null;
	}
	
	public CodeLength getLength() {
		return mLength;
	}
	
	public void setLength(final CodeLength value){
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
	
	public I2of5CheckDigitVerification getI2of5CheckDigitVerification(){
		return mI2of5CheckDigitVerification;
	}
	
	public void setI2of5CheckDigitVerification(I2of5CheckDigitVerification value){
		mI2of5CheckDigitVerification = value;
	}
	
	public boolean getTransmitI2of5CheckDigit(){
		return mIsTransmitI2of5CheckDigit;
	}
	
	public void setTransmitI2of5CheckDigit(boolean value){
		mIsTransmitI2of5CheckDigit = value;
	}
	
	public boolean getConvertI2of5ToEan13(){
		return mIsConvertI2of5ToEan13;
	}
	
	public void setConvertI2of5ToEan13(boolean value){
		mIsConvertI2of5ToEan13 = value;
	}
	
	public boolean getI2of5ReducedQuietZone(){
		return mIsI2of5ReducedQuietZone;
	}
	
	public void setI2of5ReducedQuietZone(boolean value){
		mIsI2of5ReducedQuietZone = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_i2of5, null);
		
		linearLenght1 = (LinearLayout) root.findViewById(R.id.linear_length1);
		linearLenght2 = (LinearLayout) root.findViewById(R.id.linear_length2);
		
		txtLengthType = (TextView) root.findViewById(R.id.length_type);
		txtLengthType.setOnClickListener(this);
		txtLength1 = (TextView) root.findViewById(R.id.length1);       
		txtLength1.setOnClickListener(this);
		txtLength2 = (TextView) root.findViewById(R.id.length2);  
		txtLength2.setOnClickListener(this);
		txtI2of5CheckDigitVerification = (TextView) root.findViewById(R.id.i2of5_check_digit_verification);
		txtI2of5CheckDigitVerification.setOnClickListener(this);
		chkTransmitI2of5CheckDigit = (CheckBox) root.findViewById(R.id.transmit_i2of5_check_digit);
		chkTransmitI2of5CheckDigit.setOnCheckedChangeListener(this);
		chkConvertI2of5ToEan13 = (CheckBox) root.findViewById(R.id.convert_i2of5_to_ean13);      
		chkConvertI2of5ToEan13.setOnCheckedChangeListener(this);
		chkI2of5ReducedQuietZone = (CheckBox) root.findViewById(R.id.i2of5_reduced_quiet_zone);      
		chkI2of5ReducedQuietZone.setOnCheckedChangeListener(this);
		
		dlgLengthType = new EnumListDialog(txtLengthType, CodeLengthType.values());
		dlgLengthType.setValue(mLength.getLengthType());
		dlgLength1 = new NumberDialog(txtLength1);
		dlgLength1.setValue(mLength.getLength1());
		dlgLength2 = new NumberDialog(txtLength2);
		dlgLength2.setValue(mLength.getLength2());
		
		dlgI2of5CheckDigitVerification = new EnumListDialog(txtI2of5CheckDigitVerification, I2of5CheckDigitVerification.values());
		dlgI2of5CheckDigitVerification.setValue(mI2of5CheckDigitVerification);
		
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

				mI2of5CheckDigitVerification = (I2of5CheckDigitVerification) dlgI2of5CheckDigitVerification.getValue();
				mIsTransmitI2of5CheckDigit = chkTransmitI2of5CheckDigit.isChecked();
				mIsConvertI2of5ToEan13 = chkConvertI2of5ToEan13.isChecked();
				mIsI2of5ReducedQuietZone = chkI2of5ReducedQuietZone.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionSSI2DI2of5Dialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI2DI2of5Dialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().onCancel()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionSSI2DI2of5Dialog.this); 
				
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
				
				dlgI2of5CheckDigitVerification.display();
				
				chkTransmitI2of5CheckDigit.setChecked(mIsTransmitI2of5CheckDigit);
				chkConvertI2of5ToEan13.setChecked(mIsConvertI2of5ToEan13);
				chkI2of5ReducedQuietZone.setChecked(mIsI2of5ReducedQuietZone);
				
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
		case R.id.i2of5_check_digit_verification:
			dlgI2of5CheckDigitVerification.showDialog(mContext, R.string.i2of5_check_digit_verification);
			break;

		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.transmit_i2of5_check_digit:
			break;
		case R.id.convert_i2of5_to_ean13:
			break;
		case R.id.i2of5_reduced_quiet_zone:
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
