package com.atid.app.atx.dialog.ssi;

import com.atid.app.atx.dialog.BaseDialog;
import com.atid.app.atx.dialog.EnumListDialog;
import com.atid.app.atx.dialog.NumberDialog;
import com.atid.app.atx.R;
import com.atid.lib.module.barcode.ssi.type.CodeLength;
import com.atid.lib.module.barcode.ssi.type.CodeLengthType;
import com.atid.lib.module.barcode.ssi.type.MSICheckDigitAlgorithm;
import com.atid.lib.module.barcode.ssi.type.MSICheckDigits;
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

public class OptionMsiDialog extends BaseDialog implements OnClickListener, OnCheckedChangeListener{
	private static final String TAG = OptionMsiDialog.class.getSimpleName();
	private static final int INFO = ATLog.L2;
	
	private LinearLayout linearLenght1;
	private LinearLayout linearLenght2;
	
	private TextView txtLengthType;
	private TextView txtLength1;
	private TextView txtLength2;
	private TextView txtMsiCheckDigit;
	private CheckBox chkTransmitMsiCheckDigit;
	private TextView txtMsiCheckDigitAlgorithm;
	
	private EnumListDialog dlgLengthType;
	private NumberDialog dlgLength1;
	private NumberDialog dlgLength2;
	private EnumListDialog dlgMsiCheckDigit;
	private EnumListDialog dlgMsiCheckDigitAlgorithm;
	
	private CodeLength mLength;
	private MSICheckDigits mMsiCheckDigit;
	private boolean mIsTransmitMsiCheckDigit;
	private MSICheckDigitAlgorithm mMsiCheckDigitAlgorithm;
	
	private Context mContext;
	
	public OptionMsiDialog(){
		super();
		
		mLength = new CodeLength(4, 55);
		mMsiCheckDigit = MSICheckDigits.OneCheckDigit;
		mIsTransmitMsiCheckDigit = false;
		mMsiCheckDigitAlgorithm = MSICheckDigitAlgorithm.Mod10;
		
		mContext = null;
	}
	
	public CodeLength getLength(){
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
	
	public MSICheckDigits getMSICheckDigit(){
		return mMsiCheckDigit;
	}
	
	public void setMSICheckDigits(MSICheckDigits value){
		mMsiCheckDigit = value;
	}
	
	public boolean getTransmitMsiCheckDigit(){
		return mIsTransmitMsiCheckDigit;
	}
	
	public void setTransmitMsiCheckDigit(boolean value){
		mIsTransmitMsiCheckDigit = value;
	}
	
	public MSICheckDigitAlgorithm getMSICheckDigitAlgorithm(){
		return mMsiCheckDigitAlgorithm;
	}
	
	public void setMSICheckDigitAlgorithm(MSICheckDigitAlgorithm value){
		mMsiCheckDigitAlgorithm = value;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public void showDialog(Context context, String title, final IValueChangedListener changedListener,
			final ICancelListener cancelListener) {
		
		mContext = context;
		
		LinearLayout root = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_barcode_option_ssi_2d_msi, null);
		
		linearLenght1 = (LinearLayout) root.findViewById(R.id.linear_length1);
		linearLenght2 = (LinearLayout) root.findViewById(R.id.linear_length2);
		
		txtLengthType = (TextView) root.findViewById(R.id.length_type);      
		txtLengthType.setOnClickListener(this);
		txtLength1 = (TextView) root.findViewById(R.id.length1);    
		txtLength1.setOnClickListener(this);
		txtLength2 = (TextView) root.findViewById(R.id.length2);  
		txtLength2.setOnClickListener(this);
		txtMsiCheckDigit = (TextView) root.findViewById(R.id.msi_check_digit);   
		txtMsiCheckDigit.setOnClickListener(this);
		chkTransmitMsiCheckDigit = (CheckBox) root.findViewById(R.id.transmit_msi_check_digit); 
		chkTransmitMsiCheckDigit.setOnCheckedChangeListener(this);
		txtMsiCheckDigitAlgorithm = (TextView) root.findViewById(R.id.msi_check_digit_algorithm);
		txtMsiCheckDigitAlgorithm.setOnClickListener(this);
		
		dlgLengthType = new EnumListDialog(txtLengthType, CodeLengthType.values());
		dlgLengthType.setValue(mLength.getLengthType());
		dlgLength1 = new NumberDialog(txtLength1);
		dlgLength1.setValue(mLength.getLength1());
		dlgLength2 = new NumberDialog(txtLength2);
		dlgLength2.setValue(mLength.getLength2());
		dlgMsiCheckDigit = new EnumListDialog(txtMsiCheckDigit, MSICheckDigits.values());
		dlgMsiCheckDigit.setValue(mMsiCheckDigit);
		dlgMsiCheckDigitAlgorithm = new EnumListDialog(txtMsiCheckDigitAlgorithm, MSICheckDigitAlgorithm.values());
		dlgMsiCheckDigitAlgorithm.setValue(mMsiCheckDigitAlgorithm);
		
		chkTransmitMsiCheckDigit.setChecked(mIsTransmitMsiCheckDigit);
		
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

				mMsiCheckDigit = (MSICheckDigits)dlgMsiCheckDigit.getValue();
				mMsiCheckDigitAlgorithm = (MSICheckDigitAlgorithm)dlgMsiCheckDigitAlgorithm.getValue();
				
				mIsTransmitMsiCheckDigit = chkTransmitMsiCheckDigit.isChecked();
				
				if(changedListener != null)
					changedListener.onValueChanged(OptionMsiDialog.this);
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$PositiveButton.onClick()");
			}
		});
		
		builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionMsiDialog.this); 
				
				ATLog.i(TAG, INFO, "INFO. showDialog().$NegativeButton.onClick()");
			}
		});
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				if(cancelListener != null)
					cancelListener.onCanceled(OptionMsiDialog.this); 
				
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
				
				dlgMsiCheckDigit.display();
				dlgMsiCheckDigitAlgorithm.display();
				
				chkTransmitMsiCheckDigit.setChecked(mIsTransmitMsiCheckDigit);
				
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
		case R.id.msi_check_digit:
			dlgMsiCheckDigit.showDialog(mContext, R.string.msi_check_digit);
			break;
		case R.id.msi_check_digit_algorithm:
			dlgMsiCheckDigitAlgorithm.showDialog(mContext, R.string.msi_check_digit_algorithm);
			break;
				
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch(view.getId()){
		case R.id.transmit_msi_check_digit:
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
